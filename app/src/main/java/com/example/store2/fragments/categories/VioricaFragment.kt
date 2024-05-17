package com.example.store2.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.store2.data.Category
import com.example.store2.util.Resource
import com.example.store2.viewmodel.CategoryViewModel
import com.example.store2.viewmodel.factory.BaseCatViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VioricaFragment : BaseCategory() {

        @Inject
        lateinit var firestore: FirebaseFirestore

        val viewModel by viewModels<CategoryViewModel> {
            BaseCatViewModelFactory(firestore, Category.Viorica)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            lifecycleScope.launch {
                viewModel.offerProd.collectLatest {
                    when(it){
                        is Resource.Loading ->{
                            showOfferLoading()
                        }
                        is Resource.Success ->{
                            offerAdapter.differ.submitList(it.data)
                            hideOfferLoading()
                        }

                        is Resource.Error ->{
                            Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG)
                                .show()
                            hideOfferLoading()
                        }
                        else -> Unit
                    }
                }
            }

            lifecycleScope.launch {
                viewModel.ourProd.collectLatest {
                    when(it){
                        is Resource.Loading ->{
                            showOurProdLoading()
                        }
                        is Resource.Success ->{
                            OurProductsAdapter.differ.submitList(it.data)
                            hideOurProdLoading()
                        }

                        is Resource.Error ->{
                            Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG)
                                .show()
                            hideOurProdLoading()
                        }
                        else -> Unit
                    }
                }
            }
        }

        override fun onOurProdPagingRequest() {

        }

        override fun onOfferPagingRequest() {

        }
    }

