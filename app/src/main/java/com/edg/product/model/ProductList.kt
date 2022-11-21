package com.edg.product.model

import com.edg.product.model.Price
import java.io.Serializable

data class ProductList(val title: String, val imageURL: String, val brand: String, val id: String,val ratingCount:String, var isFavorite : Boolean, var isAddedCart : Boolean,val price : ArrayList<Price>) :
    Serializable
