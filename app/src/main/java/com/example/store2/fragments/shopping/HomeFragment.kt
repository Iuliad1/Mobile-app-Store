package com.example.store2.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.store2.R
import com.example.store2.adapters.HomeViewPagerAdapter
import com.example.store2.databinding.FragmentHomeBinding
import com.example.store2.fragments.categories.IuvasFragment
import com.example.store2.fragments.categories.MaicomFragment
import com.example.store2.fragments.categories.VioricaFragment
import com.example.store2.fragments.categories.CristinaFragment
import com.example.store2.fragments.categories.Category5
import com.example.store2.fragments.categories.Category6
import com.example.store2.fragments.categories.MainCategory
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf(
            MainCategory(),
            IuvasFragment(),
            MaicomFragment(),
            VioricaFragment(),
            CristinaFragment(),
            Category5() ,
            Category6()

            )

        binding.ViewPagerHome.isUserInputEnabled = false

        val viewPager2Adapter = HomeViewPagerAdapter(categoriesFragments,childFragmentManager,lifecycle)
        binding.ViewPagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.ViewPagerHome) { tab,position ->
            when(position) {
                0 -> tab.text = "Despre"
                1 -> tab.text = "Iuvas"
                2 -> tab.text = "Maicom"
                3 -> tab.text = "Viorica"
                4 -> tab.text = "Cristina"

            }
        }.attach()
    }
}