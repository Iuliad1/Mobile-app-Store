package com.example.store2.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store2.R
import com.example.store2.adapters.BestProductAdapter
import com.example.store2.databinding.FragmentBaseBinding
import com.example.store2.databinding.ProductRvItemBinding
import com.example.store2.util.showBottomNavView

open class BaseCategory: Fragment(R.layout.fragment_base) {
    private lateinit var binding: FragmentBaseBinding
    protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter() }
    protected val OurProductsAdapter: BestProductAdapter by lazy { BestProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOffer()
        setupOurProd()

        OurProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        offerAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }


        binding.OferteSpeciale.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!recyclerView.canScrollVertically(1)&& dx !=0){
                    onOfferPagingRequest()
                }
            }
        })

        binding.ScrollViewBaseCat.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v, _, scrollY, _, _ ->
            if(v.getChildAt(0).bottom <= v.height + scrollY){
                onOurProdPagingRequest()
            }

        })
    }

    fun showOfferLoading(){
        binding.OfferProdProgBar.visibility = View.VISIBLE
    }

    fun hideOfferLoading(){
        binding.OfferProdProgBar.visibility = View.GONE
    }

    fun showOurProdLoading(){
        binding.OurProdProgBar.visibility = View.VISIBLE
    }

    fun hideOurProdLoading(){
        binding.OurProdProgBar.visibility = View.GONE
    }

    open fun onOfferPagingRequest(){

    }

    open fun onOurProdPagingRequest(){

    }

    private fun setupOurProd() {
        binding.Bproducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter = OurProductsAdapter
        }
    }

    private fun setupOffer() {
        binding.OferteSpeciale.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        showBottomNavView()
    }
}
