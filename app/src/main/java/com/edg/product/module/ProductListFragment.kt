package com.edg.product.module

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.edg.product.*
import com.edg.product.base.BaseActivity
import com.edg.product.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.edg.product.databinding.FragmentProductListBinding
import com.edg.product.model.ProductList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.reflect.Type


@AndroidEntryPoint
class ProductListFragment : BaseFragment<FragmentProductListBinding>() {

    private val viewModel by viewModels<ProductListViewModel>()
    private var listOfFavorite : ArrayList<ProductList> = ArrayList()
    private var listOfProduct : ArrayList<ProductList> = ArrayList()
    private var listener: OnSelectItem? = null

    override val layoutId: Int = R.layout.fragment_product_list

    @SuppressLint("NotifyDataSetChanged")
    override fun doTransaction() {

        val adapter = MainAdapter(requireContext())
        val layoutManager = LinearLayoutManager(requireContext())

        mBinding.recyclerview.adapter = adapter
        mBinding.recyclerview.layoutManager = layoutManager

        viewModel.getAllProducts()
        viewModel.productList.observe(this, Observer {
            Log.d(TAG, "onCreate: $it")
            (activity as? BaseActivity<*>)?.dismissLoading()
            listOfProduct=it.products
            listOfProduct.forEachIndexed { index, productList ->
                for (favoriteItem in listOfFavorite) {
                    if(productList.id==favoriteItem.id){
                        listOfProduct[index].isFavorite=true
                    }
                }
            }
            adapter.setProductList(listOfProduct)
        })

        viewModel.errorMessage.observe(this, Observer {

        })

        viewModel.userPreferences.favoriteList.asLiveData().observe(this, Observer {
            if (it!= null) {
                println()
                val gson = Gson()
                val type: Type = object : TypeToken<ArrayList<ProductList?>?>() {}.type
                val favoriteList: ArrayList<ProductList> = gson.fromJson(it, type)
                listOfFavorite=favoriteList
                if (listOfProduct.isNotEmpty()){
                    listOfProduct.forEachIndexed { index, productList ->
                        productList.isFavorite=false
                        for (favoriteItem in listOfFavorite) {
                            if(productList.id==favoriteItem.id){
                                listOfProduct[index].isFavorite=true
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        })
        for (i in listOfFavorite){
            i.isFavorite=true
        }

        adapter.setListener(object : OnItemaddedListner {

            override fun onPosition(position: Int, isChecked: Boolean, id: String) {

                listOfProduct.forEachIndexed { index, productList ->

                    if(position==index){
                        if(isChecked){
                            listOfFavorite.add(productList)
                        }else{
                            for(i in listOfFavorite){
                                if(i.id==id){
                                    listOfFavorite.remove(i)
                                    break
                                }
                            }
                        }
                        lifecycleScope.launch {
                            viewModel.saveFavoriteList(listOfFavorite)
                        }
                        return
                    }
                }
            }

        })

        adapter.setProductClickedListener(object : OnProductClickedListner {
            override fun OnClickAction(product: ProductList) {
                listener?.selectedItem(product)
            }

        })

    }

    fun setItemClicked(listner : OnSelectItem){
        this.listener = listner
    }

}