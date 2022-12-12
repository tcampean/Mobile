package com.example.lab1nativeui.viewmodel

import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab1nativeui.data.ProductData
import com.example.lab1nativeui.database.ProductDao
import com.example.lab1nativeui.model.Product
import com.example.lab1nativeui.util.AppConstants

class MainViewModel(val database: ProductDao) : ViewModel() {

    var position: Int = 0

    private val _productList =  MutableLiveData(ProductData.loadProducts())
    val productList: LiveData<ArrayList<Product>>
        get() = _productList

    private val _action = MutableLiveData(AppConstants.ACTION_INIT)
    val action: LiveData<String>
        get() = _action

    fun addProduct(product: Product): Boolean {

        for(p: Product in _productList.value!!) {
            if(p.name == product.name)
                return false
        }

        _productList.value?.add(product)
        _action.value = AppConstants.ACTION_CREATE
        _productList.value = _productList.value
        return true
    }

    fun setupReceiverIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(AppConstants.CREATE_OBJECT)
        intentFilter.addAction(AppConstants.UPDATE_OBJECT)
        intentFilter.addAction(AppConstants.DELETE_OBJECT)

        return intentFilter
    }

    fun updateProduct(product: Product): Boolean {
        var position = -1
        var i = 0

        for(p: Product in _productList.value!!) {
            if(p.id == product.id)
                position = i
            else if(p.name == product.name)
                return false
            i++
        }
        _productList.value!![position] = product
        _action.value = AppConstants.ACTION_UPDATE
        this.position = position
        _productList.value = _productList.value
        return true
    }

    fun deleteProduct(id: String): Boolean {
        var position = 0
        for(p: Product in _productList.value!!) {
            if(p.id.toString() == id) {
                _productList.value!!.remove(p)
                _action.value = AppConstants.ACTION_DELETE
                this.position = position
                _productList.value = _productList.value
                return true
            }
            position++
        }
        return false
    }

}