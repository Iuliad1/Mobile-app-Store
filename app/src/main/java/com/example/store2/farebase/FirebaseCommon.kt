package com.example.store2.farebase

import com.example.store2.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class FirebaseCommon (
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){

    private  val cartCollection =
        firestore.collection("user").document(auth.uid!!).collection("cart")

    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit) {
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
            onResult(cartProduct,null)
        }
            .addOnFailureListener {
                onResult(null, it)
            }
    }

    fun increaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transaction ->
            val docRef = cartCollection.document(documentId)
            val document = transaction.get(docRef)
            val productObj = document.toObject(CartProduct::class.java)
            productObj?.let {cartPeoduct ->
                val newQuant = cartPeoduct.quantity + 1
                val newProdObj = cartPeoduct.copy(quantity = newQuant)
                transaction.set(docRef,newProdObj)
            }
        }
            .addOnSuccessListener {
                onResult(documentId,null)

            }
            .addOnFailureListener {
                onResult(null,it)
            }
    }

    fun decreaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transaction ->
            val docRef = cartCollection.document(documentId)
            val document = transaction.get(docRef)
            val productObj = document.toObject(CartProduct::class.java)
            productObj?.let {cartPeoduct ->
                val newQuant = cartPeoduct.quantity - 1
                val newProdObj = cartPeoduct.copy(quantity = newQuant)
                transaction.set(docRef,newProdObj)
            }
        }
            .addOnSuccessListener {
                onResult(documentId,null)

            }
            .addOnFailureListener {
                onResult(null,it)
            }
    }

    enum class quantityChanging{
        INCREASE,DECREASE
    }


}