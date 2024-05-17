package com.example.store2.activites

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.store2.R
import com.example.store2.databinding.ActivityShoppingBinding
import com.example.store2.util.Resource
import com.example.store2.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<CartViewModel>()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

      val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.shoppingHostFragment) as NavHostFragment
      val  navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        lifecycleScope.launch {
            viewModel.cartProd.collectLatest {
                when(it){
                    is Resource.Success ->{
                        val count = it.data?.size ?: 0
                        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNav)
                        bottomNavigation.getOrCreateBadge(R.id.cardFragment).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.mutedTeal)
                        }
                    }
                    else -> Unit
                }
            }
        }

    }
}