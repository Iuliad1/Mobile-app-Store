package com.example.store2.viewmodel

import android.graphics.pdf.PdfDocument.PageInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store2.data.Product
import com.example.store2.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestDealProducts =
        MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealProducts: StateFlow<Resource<List<Product>>> = _bestDealProducts

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    private val pagingInfo = PagingInfo()

    init {
        fetchSpecialProducts()
        fetchBestDealsProducts()
        fetchBestProducts()
    }

    fun fetchSpecialProducts() {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Special Products")
            .get()
            .addOnSuccessListener { result ->
                val specialProductList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestDealsProducts() {
        viewModelScope.launch {
            _bestDealProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Best Deals")
            .get()
            .addOnSuccessListener { result ->
                val bestDealProducts = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDealProducts.emit(Resource.Success(bestDealProducts))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestDealProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProducts() {
        if (!pagingInfo.isPagingEnd) {
            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
                firestore.collection("Products").limit(pagingInfo.bestProdpage * 4).get()
                    //.whereEqualTo("category", "Best Products")
                    // .get()
                    .addOnSuccessListener { result ->
                        val bestProducts = result.toObjects(Product::class.java)
                        pagingInfo.isPagingEnd = bestProducts == pagingInfo.prevBestProd
                        pagingInfo.prevBestProd = bestProducts
                        viewModelScope.launch {
                            _bestProducts.emit(Resource.Success(bestProducts))
                        }
                        pagingInfo.bestProdpage++
                    }
                    .addOnFailureListener {
                        viewModelScope.launch {
                            _bestProducts.emit(Resource.Error(it.message.toString()))
                        }
                    }
            }
        }
    }
}

internal data class PagingInfo(
    var bestProdpage: Long = 1,
    var prevBestProd : List<Product> = emptyList(),
    var isPagingEnd: Boolean = false
)