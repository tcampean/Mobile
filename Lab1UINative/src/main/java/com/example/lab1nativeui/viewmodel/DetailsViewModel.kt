package com.example.lab1nativeui.viewmodel

import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab1nativeui.model.Product
import com.example.lab1nativeui.util.AppConstants

class DetailsViewModel: ViewModel() {

    lateinit var product: Product

    private val _productName = MutableLiveData<String>()
    val productName: LiveData<String>
        get() = _productName

    private val _type = MutableLiveData<String>()
    val type: LiveData<String>
        get() = _type

    private val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    private val _quantity = MutableLiveData<String>()
    val quantity: LiveData<String>
        get() = _quantity

    private val _price = MutableLiveData<String>()
    val price: LiveData<String>
        get() = _price

    fun setupFields(p: Product) {
        product = p
        _productName.postValue(p.name)
        _type.postValue(p.type)
        _description.postValue(p.description)
        _quantity.postValue(p.quantity.toString())
        _price.postValue(p.price.toString())
    }

    fun setupReceiverIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(AppConstants.UPDATE_OBJECT)

        return intentFilter
    }
}