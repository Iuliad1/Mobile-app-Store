package com.example.store2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store2.data.CartProduct
import com.example.store2.farebase.FirebaseCommon
import com.example.store2.helper.getProductPrice
import com.example.store2.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {

    private val _cartProd = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProd = _cartProd.asStateFlow()

    private var cartProdDoc = emptyList<DocumentSnapshot>()



    val productsPrice = cartProd.map {
        when(it){
            is Resource.Success ->{
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }

    private  val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    fun deleteCardProduct(cartProduct: CartProduct){
        val index = cartProd.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {
            val documentId = cartProdDoc[index].id
        firestore.collection("user").document(auth.uid!!).collection("cart")
         .document(documentId).delete()
        }
    }


    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)*cartProduct.quantity).toDouble()
        }.toFloat()
    }


    init {
        getCartProducts()
    }

    private fun getCartProducts() {
        viewModelScope.launch { _cartProd.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch { _cartProd.emit(Resource.Error(error?.message.toString())) }
                } else {
                    cartProdDoc = value.documents
                    val cartProducts = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch {_cartProd.emit(Resource.Success(cartProducts)) }
                }
            }
    }

    fun changeQuant(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.quantityChanging
    ) {

        val index = cartProd.value.data?.indexOf(cartProduct)

        /**
         * index could be equal to -1 if the function [getCartProduct] delays which also delay the result we
         * expect to be inside the [_cartProd] and to prevent the app from chrashing we make check
         */
        if (index != null && index != -1) {
            val documentId = cartProdDoc[index].id
            when(quantityChanging){
                FirebaseCommon.quantityChanging.INCREASE ->{
                    viewModelScope.launch {_cartProd.emit(Resource.Loading())}
                    increaseQuantity(documentId)
                }
                FirebaseCommon.quantityChanging.DECREASE ->{
                    if (cartProduct.quantity == 1){
                        viewModelScope.launch { _deleteDialog.emit(cartProduct) }
                        return
                    }
                    viewModelScope.launch {_cartProd.emit(Resource.Loading())}
                    decreaseQuantity(documentId)
                }
            }
        } else {
            //
        }
    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantity(documentId){result , e ->
            if (e != null)
                viewModelScope.launch { _cartProd.emit(Resource.Error(e.message.toString())) }
        }
    }

    private fun increaseQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId){result , e ->
            if (e != null)
                viewModelScope.launch {
                    _cartProd.emit(Resource.Error(e.message.toString()))
                }
        }
    }
}
