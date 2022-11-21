package com.edg.product.repository

import com.edg.product.network.RetrofitServiceImp
import javax.inject.Inject

class MainRepository @Inject constructor (private val retrofitService: RetrofitServiceImp) {

    fun getAllMovies() = retrofitService.getAllMovies()
}