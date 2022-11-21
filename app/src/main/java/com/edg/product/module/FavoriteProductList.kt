package com.edg.product.module

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.edg.product.MainAdapter
import com.edg.product.OnItemaddedListner
import com.edg.product.model.ProductList
import com.edg.product.R
import com.edg.product.base.BaseFragment
import com.edg.product.databinding.FragmentFavoriteListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.reflect.Type

@AndroidEntryPoint
class FavoriteProductList : BaseFragment<FragmentFavoriteListBinding>(){

    private var listOfFavorite : ArrayList<ProductList> = ArrayList()
    private val viewModel by viewModels<FavoriteListViewModel>()


    override val layoutId: Int = R.layout.fragment_favorite_list
    override fun doTransaction() {

        val adapter = MainAdapter(requireContext())
        val layoutManager = LinearLayoutManager(requireContext())

        mBinding.recyclerview.adapter = adapter
        mBinding.recyclerview.layoutManager = layoutManager


       viewModel.userPreferences.favoriteList.asLiveData().observe(this, Observer {
            if (it!= null) {
                println(" Product List   ===>> $it")
                val gson = Gson()
                val type: Type = object : TypeToken<ArrayList<ProductList?>?>() {}.type
                val favoriteList: ArrayList<ProductList> = gson.fromJson(it, type)
                listOfFavorite=favoriteList
                adapter.setProductList(listOfFavorite)
            }
        })

        adapter.setListener(object : OnItemaddedListner {

            override fun onPosition(position: Int, isChecked: Boolean, id: String) {

                for(i in listOfFavorite){
                    if (i.id==id){
                        if(!isChecked){
                            listOfFavorite.remove(i)
                            break
                        }
                    }
                }
                lifecycleScope.launch {
                    viewModel.saveFavoriteList(listOfFavorite)
                }
            }

        })

    }

}