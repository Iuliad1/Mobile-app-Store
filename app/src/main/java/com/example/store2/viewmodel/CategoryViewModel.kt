package com.example.store2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store2.data.Category
import com.example.store2.data.Product
import com.example.store2.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
): ViewModel() {

    private  val _offerProd = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProd = _offerProd.asStateFlow()

    private  val _ourProd = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val ourProd = _ourProd.asStateFlow()

    init{
        fetchOfferProd()
        fetchOurProd()
    }

    fun fetchOfferProd() {
        viewModelScope.launch {
            _offerProd.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereNotEqualTo("offerPercentage",null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerProd.emit(Resource.Success(products))
                }
            }
            .addOnFailureListener{
                viewModelScope.launch {
                    _offerProd.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchOurProd() {
        viewModelScope.launch {
           _ourProd.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereEqualTo("offerPercentage",null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _ourProd.emit(Resource.Success(products))
                }
            }
            .addOnFailureListener{
                viewModelScope.launch {
                    _ourProd.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}