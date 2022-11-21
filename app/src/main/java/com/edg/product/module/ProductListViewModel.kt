package com.edg.product.module

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edg.product.Product
import com.edg.product.model.ProductList
import com.edg.product.repository.MainRepository
import com.edg.product.repository.UserPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    @ApplicationContext val mContext: Context,
    private val repository: MainRepository,
    val userPreferences: UserPreferences
) : ViewModel(){

    val productList = MutableLiveData<Product>()
    val errorMessage = MutableLiveData<String>()

    fun getAllProducts() {

        val response = repository.getAllMovies()
        response.enqueue(object  : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                productList.postValue(response.body())
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                errorMessage.postValue(t.message)
            }

        })
    }

        suspend fun saveFavorite(list : String) {
            userPreferences.clear()
            userPreferences.saveFavoriteList(list)
    }

    suspend fun saveFavoriteList(list : ArrayList<ProductList>) {
        val gson = Gson()

        val listString = gson.toJson(
            list,
            object : TypeToken<ArrayList<ProductList?>?>() {}.type
        )
        val jsonArray = JSONArray(listString)
        viewModelScope.launch {
            userPreferences.clear()
            userPreferences.saveFavoriteList(jsonArray.toString())
        }

    }

}