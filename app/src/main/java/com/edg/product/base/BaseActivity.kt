package com.edg.product.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.edg.product.R
import com.edg.product.databinding.ActivityBaseBinding

abstract class BaseActivity <T : ViewDataBinding> : AppCompatActivity(){

    protected val TAG: String = javaClass.simpleName

    protected lateinit var mContext: Activity
    lateinit var mBinding: T
    lateinit var mBaseBinding: ActivityBaseBinding

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {

        mContext = this
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        mBaseBinding.flLoading.setOnClickListener {
            // do nothing
        }

        initView()
        doTransaction()
    }

    override fun setContentView(layoutResID: Int) {
//        super.setContentView(layoutResID)
        mBaseBinding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_base, null, false)
        mBinding = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false)

        mBaseBinding.flContent.addView(mBinding.root, 0)
        setContentView(mBaseBinding.root)
    }


    fun showLoading() {
        mBaseBinding.flLoading.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        val interpolator = LinearInterpolator()
        animation.interpolator = interpolator
        mBaseBinding.ivLoading.startAnimation(animation)
    }

    fun dismissLoading() {
        mBaseBinding.flLoading.visibility = View.GONE
        mBaseBinding.ivLoading.clearAnimation()
    }

    override fun onDestroy() {
        mBaseBinding.ivLoading.clearAnimation()
        super.onDestroy()
    }

    protected open fun initView() {}
    abstract fun doTransaction()

}