package com.edg.product.module

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.edg.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext mContext : Context)  : ViewModel() {

    val productList = MutableLiveData<Product>()
    val errorMessage = MutableLiveData<String>()


}