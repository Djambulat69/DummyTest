package com.isaev.dummyjson.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaev.dummyjson.Product
import com.isaev.dummyjson.network.DummyJsonServiceHelper
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    private val network = DummyJsonServiceHelper

    private var loadingProducts = false

    val products: LiveData<List<Product>> = _products

    fun getMoreProducts() {
        if (loadingProducts) return

        viewModelScope.launch {
            loadingProducts = true

            _products.value =
                _products.value.orEmpty() + network.getProducts(skip = _products.value?.size ?: 0)

            loadingProducts = false
        }
    }

}