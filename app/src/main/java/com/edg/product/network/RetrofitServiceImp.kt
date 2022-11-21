package com.edg.product.network

import com.edg.product.Product
import retrofit2.Call
import javax.inject.Inject

class RetrofitServiceImp @Inject constructor() : RetrofitService {
    override fun getAllProduct() {
    }

    override fun getAllMovies(): Call<Product> {
        val retrofitService = RetrofitService.getInstance()
       return retrofitService.getAllMovies()
    }

}