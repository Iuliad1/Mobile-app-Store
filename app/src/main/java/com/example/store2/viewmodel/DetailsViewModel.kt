package com.example.store2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store2.data.CartProduct
import com.example.store2.farebase.FirebaseCommon
import com.example.store2.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private val _addCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addCart = _addCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct) {
        viewModelScope.launch { _addCart.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("product.id",cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) { //Add product to cart if it wasn't added
                        addNewProduct(cartProduct)

                    } else {
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cartProduct) { //Add more
                            val documentId = it.first().id
                            increaseQuantity(documentId,cartProduct)

                        }else {//Add one more product
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch { _addCart.emit(Resource.Error(it.message.toString())) }
            }
    }

    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseCommon.addProductToCart(cartProduct) { addedProduct, e ->
            viewModelScope.launch {
                if (e == null) {
                    _addCart.emit(Resource.Success(addedProduct!!))
                } else {
                    _addCart.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }

    private fun increaseQuantity(documentId: String, cartProduct: CartProduct) {
        firebaseCommon.increaseQuantity(documentId) { _, e->
            viewModelScope.launch {
                if (e == null) {
                    _addCart.emit(Resource.Success(cartProduct))
                } else {
                    _addCart.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }
}