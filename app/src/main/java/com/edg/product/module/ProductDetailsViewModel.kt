package com.edg.product.module

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edg.product.model.ProductList
import com.edg.product.repository.UserPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(@ApplicationContext mContext : Context, val userPreferences: UserPreferences)  : ViewModel() {

    suspend fun saveAsFavorite(product: ProductList, productList: ArrayList<ProductList>, isChecked: Boolean){

        for (i in productList){
            if (product.id==i.id){
                if (isChecked && !i.isFavorite){
                    i.isFavorite=true
                    productList.add(i)
                    saveFavoriteList(productList)
                    return

                }else if (!isChecked && i.isFavorite){
                    i.isFavorite=false
                    productList.remove(i)
                    saveFavoriteList(productList)
                    return
                }
            }
        }
        if (isChecked){
            product.isFavorite=true
            productList.add(product)
            saveFavoriteList(productList)
        }

    }

    suspend fun saveFavoriteList(list : ArrayList<ProductList>) {
        val gson = Gson()

        val listString = gson.toJson(
            list,
            object : TypeToken<ArrayList<ProductList?>?>() {}.type
        )
        val jsonArray = JSONArray(listString)
        println("JSON ARRAY == >> $jsonArray")
        viewModelScope.launch {
            userPreferences.clear()
            userPreferences.saveFavoriteList(jsonArray.toString())
        }

    }
}