package com.example.store2.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store2.R
import com.example.store2.adapters.AddressAdapter
import com.example.store2.adapters.BillingProductAdapter
import com.example.store2.data.Address
import com.example.store2.data.CartProduct
import com.example.store2.data.order.Order
import com.example.store2.data.order.OrderStatus
import com.example.store2.databinding.FragmentBillingBinding
import com.example.store2.util.HorizontalItemDec
import com.example.store2.util.Resource
import com.example.store2.viewmodel.BillingViewModel
import com.example.store2.viewmodel.OrderViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment: Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductAdapter by lazy { BillingProductAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f
    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        products = args.cardProducts.toList()
        totalPrice = args.totalPrice
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBillProducts()
        setupAddressRv()

        if (!args.payment){
            binding.apply {
                buttonPlaceOrder.visibility = View.INVISIBLE
                totalBoxContainer.visibility = View.INVISIBLE
                middleLine.visibility = View.INVISIBLE
                bottomLine.visibility = View.INVISIBLE
            }
        }

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
       }

         lifecycleScope.launch{
             billingViewModel.address.collectLatest {
                 when(it){
                     is Resource.Loading ->{
                         binding.progressbarAddress.visibility = View.VISIBLE
                     }
                     is Resource.Success ->{
                         addressAdapter.differ.submitList(it.data)
                         binding.progressbarAddress.visibility = View.GONE
                     }
                     is Resource.Error ->{
                         binding.progressbarAddress.visibility = View.GONE
                         Toast.makeText(requireContext(),"Error ${it.message}", Toast.LENGTH_SHORT).show()
                     }
                     else -> Unit
                 }
             }
         }

        lifecycleScope.launch {
            orderViewModel.order.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonPlaceOrder.stopAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(requireView(),"Comanda ta a fost plasata",Snackbar.LENGTH_LONG).show()

                    }

                    is Resource.Error -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> Unit
                }
            }
        }

        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }

        billingProductAdapter.differ.submitList(products)
        binding.tvTotalPrice.text = String.format("%.2f mdl", totalPrice)

        addressAdapter.onClick = {
            selectedAddress = it
            if (!args.payment) {
                val b = Bundle().apply { putParcelable("address", selectedAddress) }
                findNavController().navigate(R.id.action_billingFragment_to_addressFragment, b)
            }
        }

        binding.buttonPlaceOrder.setOnClickListener{
            if (selectedAddress == null){
                Toast.makeText(requireContext(),"Va rugam sa selectati adresa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showOrderConfDialog()
        }
    }



    private fun showOrderConfDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Produsele spre comanda")
            setMessage("Doriți să comandați produsele din coșul dvs.?")
            setNegativeButton("Anulează") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Da") { dialog, _ ->
                val order = Order(
                    OrderStatus.Ordered.status,
                    totalPrice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDec())
        }
    }

    private fun setupBillProducts() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL, false)
            adapter = billingProductAdapter
            addItemDecoration(HorizontalItemDec())

        }
    }

}