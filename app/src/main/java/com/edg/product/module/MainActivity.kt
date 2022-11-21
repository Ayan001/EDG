package com.edg.product.module

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.edg.product.*
import com.edg.product.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.edg.product.databinding.ActivityMainBinding
import com.edg.product.network.RetrofitService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int = R.layout.activity_main

    //lateinit var viewModel: MainViewModel

    private val viewModel by viewModels<MainViewModel>()

    private val retrofitService = RetrofitService.getInstance()
    private val adapter = MainAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun doTransaction() {

        showLoading()
        val layoutManager = LinearLayoutManager(this)
        mBinding.recyclerview.adapter = adapter
        mBinding.recyclerview.layoutManager = layoutManager

        mBinding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 2
            }

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) {
                    ProductListFragment()
                } else {
                    FavoriteProductList()
                }
            }
        }
        mBinding.viewPager.currentItem = 0
        mBinding.viewPager.adapter?.notifyDataSetChanged()


        TabLayoutMediator(
            mBinding.tabLayout, mBinding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> tab.text = "Product"
                1 -> tab.text = "Favorite"
            }
        }.attach()
    }
}