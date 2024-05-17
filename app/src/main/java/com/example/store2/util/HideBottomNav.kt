package com.example.store2.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.store2.R
import com.example.store2.activites.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavView(){
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNav
    )
    bottomNavigationView.visibility = View.GONE
}

fun Fragment.showBottomNavView(){
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(
            R.id.bottomNav
        )
    bottomNavigationView.visibility = View.VISIBLE
}