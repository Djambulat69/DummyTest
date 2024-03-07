package com.isaev.dummyjson.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaev.dummyjson.DataState
import com.isaev.dummyjson.Product
import com.isaev.dummyjson.network.DummyJsonServiceHelper
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    private val _pagingState = MutableLiveData<DataState>()

    private val network = DummyJsonServiceHelper


    val pagingState: LiveData<DataState> = _pagingState
    val products: LiveData<List<Product>> = _products

    init {
        getMoreProducts()
    }

    fun getMoreProducts() {
        if (pagingState.value == DataState.LOADING) return

        viewModelScope.launch {
            try {
                _pagingState.value = DataState.LOADING

                _products.value = _products.value.orEmpty() + network.getProducts(
                    skip = _products.value?.size ?: 0
                )

                _pagingState.value = DataState.SUCCESS
            } catch (e: Exception) {
                _pagingState.value = DataState.FAILURE
            }
        }
    }

}