package com.isaev.dummyjson.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaev.dummyjson.Product
import com.isaev.dummyjson.ui.theme.network.DummyJsonServiceHelper
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    private val network = DummyJsonServiceHelper

    val products: LiveData<List<Product>> = _products

    init {
        viewModelScope.launch {
            _products.value = network.getProducts().products
        }
    }

}