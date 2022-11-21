package com.edg.product.module

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edg.product.repository.MainRepository
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
class FavoriteListViewModel @Inject constructor(
    @ApplicationContext mContext: Context,
    private val repository: MainRepository,
    val userPreferences: UserPreferences
) : ViewModel()
{
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