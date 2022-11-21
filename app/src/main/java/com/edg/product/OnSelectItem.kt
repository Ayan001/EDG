package com.edg.product

import com.edg.product.model.ProductList

interface OnSelectItem {
  fun  selectedItem(productList: ProductList)
}