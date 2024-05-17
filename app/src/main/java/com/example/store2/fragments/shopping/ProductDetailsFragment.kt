package com.example.store2.fragments.shopping

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
import com.example.store2.R
import com.example.store2.activites.ShoppingActivity
import com.example.store2.adapters.ColorsAdapter
import com.example.store2.adapters.SizesAdapter
import com.example.store2.adapters.ViewPageForImages
import com.example.store2.data.CartProduct
import com.example.store2.databinding.FragmentProductDetailsBinding
import com.example.store2.util.Resource
import com.example.store2.util.hideBottomNavView
import com.example.store2.viewmodel.DetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment:  Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPageForImages() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val viewModel by viewModels<DetailsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       hideBottomNavView()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product

        setupSizesRv()
        setupColorsRv()
        setupViewPager()

        binding.DetailsClose.setOnClickListener {
            findNavController().navigateUp()
        }

        sizesAdapter.onItemClick = {
            selectedSize = it
        }

        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        binding.addCard.setOnClickListener {
            // Verificați dacă produsul are opțiuni de culoare și / sau mărime
            val hasColors = !product.colors.isNullOrEmpty()
            val hasSizes = !product.sizes.isNullOrEmpty()

            if (!hasColors && !hasSizes) {
                // Dacă produsul nu are nici opțiuni de culoare, nici de mărime, adăugați-l direct în coș
                viewModel.addUpdateProductInCart(CartProduct(product, 1, selectedColor, selectedSize))
            } else {
                // Dacă produsul are cel puțin o opțiune de culoare sau de mărime, verificați selecția
                if ((selectedColor != null || !hasColors) && (selectedSize != null || !hasSizes)) {
                    // Dacă sunt selectate atât culoarea cât și mărimea, sau produsul nu are opțiuni de culoare / mărime,
                    // adăugați produsul în coș
                    viewModel.addUpdateProductInCart(CartProduct(product, 1, selectedColor, selectedSize))
                } else {
                    // Dacă lipsesc una sau ambele dintre culoare și mărime, afișați un mesaj de eroare
                    Toast.makeText(requireContext(), "Selectați culoarea sau mărimea", Toast.LENGTH_SHORT).show()
                }
            }
        }


        lifecycleScope.launch {
            viewModel.addCart.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.addCard.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.addCard.revertAnimation()
                        Toast.makeText(requireContext(),"Produsul a fost adaugat cu succes", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        binding.addCard.stopAnimation()
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


        binding.apply {
            ProductName.text = product.name
            ProductPrice.text = "${product.price} mdl"
            ProductDescription.text = product.description

            if(product.colors.isNullOrEmpty())
                ProductColor.visibility = View.GONE
            if(product.sizes.isNullOrEmpty())
                ProductSize.visibility = View.GONE
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let {
            colorsAdapter.differ.submitList(it)
        }

        product.sizes?.let {
            sizesAdapter.differ.submitList(it)
        }
    }

    private fun setupViewPager() {
        binding.apply {
            ViewProductImages.adapter = viewPagerAdapter
        }
    }

    private fun setupColorsRv() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupSizesRv() {
        binding.rvSize.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }
}