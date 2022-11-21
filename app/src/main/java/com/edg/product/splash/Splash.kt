package com.edg.product.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.edg.product.base.BaseActivity
import com.edg.product.module.MainActivity
import com.edg.product.R
import com.edg.product.databinding.SplashActivityBinding


class Splash : BaseActivity<SplashActivityBinding>(){

    override val layoutId: Int = R.layout.splash_activity
    override fun doTransaction() {

        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 2500)
    }
}