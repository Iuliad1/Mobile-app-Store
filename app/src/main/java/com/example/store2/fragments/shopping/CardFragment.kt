package com.example.store2.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store2.R
import com.example.store2.adapters.CartProductAdapter
import com.example.store2.databinding.FragmentCartBinding
import com.example.store2.farebase.FirebaseCommon
import com.example.store2.util.Resource
import com.example.store2.util.VerticaleItemDec
import com.example.store2.viewmodel.CartViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CardFragment: Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartProductAdapter() }
    private val viewModel by activityViewModels<CartViewModel>()
    private var isDeleteDialogShown = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRView()

        var totalProce = 0f

        lifecycleScope.launch {
            viewModel.productsPrice.collectLatest { price ->
                price?.let {
                    totalProce = it
                    binding.tvTotalPrice.text = "$price mdl"
                }
            }
        }

        cartAdapter.onProductClick = {
           val b = Bundle().apply {  putParcelable("product",it.product)}
           findNavController().navigate(R.id.action_cardFragment_to_productDetailsFragment,b)
        }

        cartAdapter.onPlusClick = {
            viewModel.changeQuant(it,FirebaseCommon.quantityChanging.INCREASE)
        }

        cartAdapter.onMinusClick = {
            viewModel.changeQuant(it,FirebaseCommon.quantityChanging.DECREASE)
        }

        binding.buttonCheckout.setOnClickListener{
            val action = CardFragmentDirections
                .actionCardFragmentToBillingFragment(totalProce,
                    cartAdapter.differ.currentList.toTypedArray(),true)
            findNavController().navigate(action)

        }


// În colectarea de evenimente
        lifecycleScope.launch {
            viewModel.deleteDialog.collectLatest { cartProduct ->
                if (!isDeleteDialogShown) {
                    isDeleteDialogShown = true
                    val alertDialog = AlertDialog.Builder(requireContext()).apply {
                        setTitle("Ştergere produsul")
                        setMessage("Vrei să elimini produsul?")
                        setNegativeButton("Anulează") { dialog, _ ->
                            dialog.dismiss()
                        }
                        setPositiveButton("Da") { dialog, _ ->
                            viewModel.deleteCardProduct(cartProduct)
                            dialog.dismiss()
                        }
                    }
                    alertDialog.create()
                    alertDialog.show()
                }
            }
        }


        lifecycleScope.launch {
            viewModel.cartProd.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressbarCart.visibility = View.VISIBLE
                    }
                    is  Resource.Success ->{
                        binding.progressbarCart.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()) {
                            showEmptyCart()
                            hideOtherViews()
                        }else{
                            hideEmptyCard()
                            showOtherViews()
                            cartAdapter.differ.submitList(it.data)
                        }
                    }
                    is  Resource.Error ->{
                        binding.progressbarCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.imageCloseCart1.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun showOtherViews() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility =View.VISIBLE
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility =View.GONE
        }
    }

    private fun hideEmptyCard() {
        binding.apply {
            layoutCartEmpty.visibility = View.GONE
        }
    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.VISIBLE
        }
    }

    private fun setupCartRView() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter = cartAdapter
            addItemDecoration((VerticaleItemDec()))
        }
    }

}