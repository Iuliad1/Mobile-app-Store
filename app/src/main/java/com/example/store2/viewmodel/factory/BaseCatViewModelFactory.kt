package com.example.store2.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.store2.data.Category
import com.example.store2.viewmodel.CategoryViewModel
import com.google.firebase.firestore.FirebaseFirestore

class BaseCatViewModelFactory(
    private  val firestore: FirebaseFirestore,
    private val category: Category
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(firestore,category) as T
    }
}