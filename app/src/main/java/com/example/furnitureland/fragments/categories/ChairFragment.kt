package com.example.furnitureland.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.furnitureland.data.Category
import com.example.furnitureland.util.Resource
import com.example.furnitureland.viewmodel.CategoryViewModel
import com.example.furnitureland.viewmodel.factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ChairFragment:BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore, Category.Chair)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        offerAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()

                    }
                    else ->Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        showBestProductsLoading()
                    }
                    is Resource.Success ->{
                        bestProductAdapter.differ.submitList(it.data)
                        hideBestProductsLoading()
                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
                        hideBestProductsLoading()
                    }
                    else ->Unit
                }
            }
        }
    }
}