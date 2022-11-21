package com.edg.product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.edg.product.databinding.AdapterProductBinding
import com.edg.product.model.ProductList
import com.edg.product.module.ProductDetailsActivity
import com.edg.product.utils.CommonUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class MainAdapter @Inject constructor(
    @ApplicationContext val mContext: Context
) : RecyclerView.Adapter<MainViewHolder>() {

   //private val appContext = mContext.applicationContext
    var movies = mutableListOf<ProductList>()
    private var listener: OnItemaddedListner? = null
    private var listenerProduct: OnProductClickedListner? = null
    //private val userPreferences = UserPreferences(mContext)

    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(movies: ArrayList<ProductList>) {
        this.movies = movies.toMutableList()
        notifyDataSetChanged()
    }
    fun setListener(listener: OnItemaddedListner) {
        this.listener = listener
    }
    fun setProductClickedListener(listener: OnProductClickedListner) {
        this.listenerProduct = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = AdapterProductBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val movie = movies[position]
       holder.binding.name.text = movie.title
        holder.binding.buttonFavorite.isChecked=movie.isFavorite
        holder.binding.buttonAddToCart.isChecked=movie.isAddedCart
        holder.binding.price.text = "Price : "+movie.price[0].value.toString()
        Glide.with(holder.itemView.context).load(movie.imageURL).into(holder.binding.imageview)

        holder.binding.buttonFavorite.setOnClickListener {
            movies[position].isFavorite = !movie.isFavorite
            notifyDataSetChanged()

            if(movies[position].isFavorite){
                listener?.onPosition(position,true,movie.id)
            }else{
                listener?.onPosition(position,false,movie.id)
            }

        }
        holder.binding.buttonAddToCart.setOnClickListener {
            movies[position].isAddedCart = !movie.isAddedCart
            notifyDataSetChanged()
        }
        holder.binding.llProduct.setOnClickListener {
            listenerProduct?.OnClickAction(movie)

            val product= ProductList(movie.title,movie.imageURL,movie.brand,movie.id,movie.ratingCount,movie.isFavorite,movie.isAddedCart,movie.price)

            val intent = Intent(mContext, ProductDetailsActivity::class.java)
            intent.putExtra(CommonUtils.ID,movie.id)
            intent.putExtra(CommonUtils.NAME,movie.title)
            intent.putExtra(CommonUtils.PRICE,movie.price[0].value.toString())
            intent.putExtra(CommonUtils.RATING,movie.ratingCount)
            intent.putExtra(CommonUtils.IMAGE,movie.imageURL)
            intent.putExtra(CommonUtils.PRODUCT,product as java.io.Serializable)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}

class MainViewHolder(val binding: AdapterProductBinding) : RecyclerView.ViewHolder(binding.root) {

}

interface OnItemaddedListner {
    fun onPosition(position: Int,isChecked : Boolean,id:String)
}
interface OnProductClickedListner{
    fun OnClickAction(product : ProductList)
}
