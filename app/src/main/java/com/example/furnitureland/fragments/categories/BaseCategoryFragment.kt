package com.example.furnitureland.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureland.R
import com.example.furnitureland.adapters.BestProductAdapter
import com.example.furnitureland.databinding.FragmentBaseCategoryBinding
import com.example.furnitureland.util.showBottomNavigationView

open class BaseCategoryFragment: Fragment(R.layout.fragment_base_category) {

    private lateinit var binding: FragmentBaseCategoryBinding
    protected val bestProductAdapter: BestProductAdapter by lazy { BestProductAdapter() }
    protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOfferRv()
        setupBestProductsRv()

        bestProductAdapter.onClick ={
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        offerAdapter.onClick ={
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        binding.rvOffer.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!recyclerView.canScrollVertically(1) && dx !=0){
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{
                v,_,scrollY,_,_ ->
            if(v.getChildAt(0).bottom <= v.height + scrollY){
              onBestProductsPagingRequest()
            }
        })
    }

    fun showBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.INVISIBLE
    }

    open fun onOfferPagingRequest(){

    }

    open fun onBestProductsPagingRequest(){

    }


    private fun setupBestProductsRv() {

        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter=bestProductAdapter
        }
    }

    private fun setupOfferRv() {

        binding.rvOffer.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = offerAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }
}