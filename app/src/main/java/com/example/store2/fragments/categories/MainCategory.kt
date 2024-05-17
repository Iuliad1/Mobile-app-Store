package com.example.store2.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store2.R
import com.example.store2.adapters.BestDealsAdapter
import com.example.store2.adapters.BestProductAdapter
import com.example.store2.adapters.SpecialProductAdapter
import com.example.store2.databinding.FragmentLoginBinding
import com.example.store2.databinding.FragmentMainBinding
import com.example.store2.util.Resource
import com.example.store2.util.showBottomNavView
import com.example.store2.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.log


private val TAG = "MainCategory"
@AndroidEntryPoint
class MainCategory: Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private lateinit var specialProductAdapter: SpecialProductAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var OurProductsAdapter: BestProductAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductsRv()
        setupBestDealsRv()
        setupBestProductsRv()

        specialProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        bestDealsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        OurProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        lifecycleScope.launch {
            viewModel.specialProducts.collect{
                when(it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        specialProductAdapter.differ.submitList(it.data)
                        hideloading()
                    }
                    is Resource.Error -> {
                        hideloading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }

        }

        lifecycleScope.launch {
            viewModel.bestDealProducts.collect{
                when(it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideloading()
                    }
                    is Resource.Error -> {
                        hideloading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }

        }

        lifecycleScope.launch {
            viewModel.bestProducts.collect{
                when(it) {
                    is Resource.Loading -> {
                        binding.BestProdProgBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        OurProductsAdapter.differ.submitList(it.data)
                        binding.BestProdProgBar.visibility = View.GONE

                    }
                    is Resource.Error -> {
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.BestProdProgBar.visibility = View.GONE

                    }
                    else -> Unit
                }
            }

        }

        binding.scrollMainCatigory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{v,_,scrollY,_,_ ->
            if(v.getChildAt(0).bottom <= v.height + scrollY){
                viewModel.fetchBestProducts()
            }

        })
    }



    private fun hideloading() {
        binding.mainCatProgBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCatProgBar.visibility = View.VISIBLE
    }

    private fun setupSpecialProductsRv() {
        specialProductAdapter = SpecialProductAdapter()
        binding.Sproducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = specialProductAdapter

        }
    }

    private fun setupBestProductsRv() {
        OurProductsAdapter = BestProductAdapter()
        binding.Bproducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = OurProductsAdapter
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter= BestDealsAdapter()
        binding.Bdeals.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = bestDealsAdapter

        }
    }

    override fun onResume() {
        super.onResume()

        showBottomNavView()
    }
}