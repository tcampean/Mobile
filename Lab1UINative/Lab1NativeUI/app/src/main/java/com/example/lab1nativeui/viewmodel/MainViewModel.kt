package com.example.lab1nativeui.viewmodel

import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab1nativeui.data.ProductData
import com.example.lab1nativeui.model.Product

class MainViewModel : ViewModel() {

    private var position: Int = 0

    private val _productList =  MutableLiveData(ProductData().loadProducts())
    val productList: LiveData<ArrayList<Product>>
        get() = _productList


    fun addProduct(p: Product) {
        _productList.value?.add(p)
    }

    fun setupReceiverIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction("CreateObject")

        return intentFilter
    }

}