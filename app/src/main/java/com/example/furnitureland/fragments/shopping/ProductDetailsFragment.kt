package com.example.furnitureland.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.furnitureland.R
import com.example.furnitureland.activities.ShoppingActivity
import com.example.furnitureland.adapters.ColorsAdapter
import com.example.furnitureland.adapters.ViewPager2Images
import com.example.furnitureland.data.CartProduct
import com.example.furnitureland.databinding.FragmentProductDetailsBinding
import com.example.furnitureland.util.Resource
import com.example.furnitureland.util.hideBottomNavigationView
import com.example.furnitureland.viewmodel.DetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment: Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding : FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy{ ViewPager2Images()}
    private val colorsAdapter by lazy { ColorsAdapter()}
    private var selectedColor: Int? = null
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setUpColorsRv()
        setUpViewpager()

        binding.imageClose.setOnClickListener{
            findNavController().navigateUp()
        }

        colorsAdapter.onItemClick={
            selectedColor=it
        }

        binding.buttonAddToCart.setOnClickListener{
            viewModel.addUpdateProductInCart(CartProduct(product,1,selectedColor))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.buttonAddToCart.startAnimation()
                    }
                    is Resource.Success ->{
                        binding.buttonAddToCart.revertAnimation()
                        binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.g_blue_gray200))
                    }
                    is Resource.Error ->{
                        binding.buttonAddToCart.revertAnimation()
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                    else ->Unit
                }
            }
        }

        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let { colorsAdapter.differ.submitList(it) }
    }

    private fun setUpViewpager() {
        binding.apply {
            viewPagerProductImages.adapter = viewPagerAdapter
        }
    }

    private fun setUpColorsRv() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }
    }
}