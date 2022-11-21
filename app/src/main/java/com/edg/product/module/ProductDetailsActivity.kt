package com.edg.product.module

import android.os.Build
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.edg.product.model.ProductList
import com.edg.product.R
import com.edg.product.base.BaseActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.edg.product.databinding.ActivityDetailsBinding
import com.edg.product.utils.CommonUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable
import java.lang.reflect.Type

@AndroidEntryPoint
class ProductDetailsActivity : BaseActivity<ActivityDetailsBinding>() {

    override val layoutId: Int = R.layout.activity_details
    var id : String=""
    var name : String=""
    var price : String=""
    var rating : String=""
    var image : String=""
    lateinit var list : ArrayList<ProductList>
    private val viewModel by viewModels<ProductDetailsViewModel>()
    private var listOfFavorite : ArrayList<ProductList> = ArrayList()
    private lateinit var product : ProductList
    override fun doTransaction() {

        initUi()

        viewModel.userPreferences.favoriteList.asLiveData().observe(this, Observer {
            if (it!= null) {
                println(" Product List   ===>> $it")
                val gson = Gson()
                val type: Type = object : TypeToken<ArrayList<ProductList?>?>() {}.type
                val favoriteList: ArrayList<ProductList> = gson.fromJson(it, type)
                listOfFavorite=favoriteList
            }
        })

        mBinding.ivBack.setOnClickListener {
            finish()
        }
        mBinding.tbFavorite.setOnClickListener {

            lifecycleScope.launch {
                viewModel.saveAsFavorite(product,listOfFavorite,mBinding.tbFavorite.isChecked)
            }
        }

    }

    fun initUi() {

        product= getSerializable(CommonUtils.PRODUCT, ProductList::class.java)


        id = product.id
        name = product.title
        price = product.price[0].value.toString()
        rating = product.ratingCount
        image = product.imageURL

        mBinding.tvProductName.text = name
        mBinding.tvProductPrice.text = "Price :$price"
        mBinding.tvProductRating.text = "Rating :$rating"
        mBinding.tbFavorite.isChecked = product.isFavorite
        Glide.with(this).load(image).into(mBinding.ivProduct)
    }
    fun <T : Serializable?> getSerializable(name: String, clazz: Class<T>): T
    {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(name, clazz)!!
        else
            intent.getSerializableExtra(name) as T
    }
    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onPause() {
        super.onPause()
    }
}