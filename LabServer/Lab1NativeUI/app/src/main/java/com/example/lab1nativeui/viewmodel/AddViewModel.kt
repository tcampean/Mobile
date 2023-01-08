package com.example.lab1nativeui.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddViewModel: ViewModel() {

    private var _selectedType = MutableLiveData<String>()
    val selectedType: LiveData<String>
        get() = _selectedType

    val errorFlags = ObservableArrayList<ErrorFlags>()
    var productName = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var quantity = MutableLiveData<String>()
    var price = MutableLiveData<String>()

    fun setSelectedType(str: String) {
        _selectedType.postValue(str)
    }

    fun isProductNameValid(): Boolean {
        if(productName.value.isNullOrEmpty())
            errorFlags.add(ErrorFlags.MISSING_PRODUCT_NAME)
        else {
            removeFlags(ErrorFlags.MISSING_PRODUCT_NAME)
            if(!productName.value!![0].isUpperCase()) {
                errorFlags.add(ErrorFlags.INVALID_PRODUCT_NAME)
            } else {
                if(productName.value!!.length < 3) {
                    errorFlags.add(ErrorFlags.INVALID_PRODUCT_NAME)
                } else {
                    removeFlags(ErrorFlags.INVALID_PRODUCT_NAME)
                }
            }
        }

        return errorFlags.isEmpty()
    }

    fun isDescriptionValid(): Boolean {
        if(description.value.isNullOrEmpty()) {
            errorFlags.add(ErrorFlags.MISSING_DESCRIPTION)
        }
        else {
            removeFlags(ErrorFlags.MISSING_DESCRIPTION)
            if (description.value!!.length < 10) {
                errorFlags.add(ErrorFlags.INVALID_DESCRIPTION)
            }
            else {
                removeFlags(ErrorFlags.INVALID_DESCRIPTION)
            }
        }

        return errorFlags.isEmpty()
    }

    fun isQuantityValid(): Boolean {
        if(quantity.value.isNullOrEmpty())
            errorFlags.add(ErrorFlags.MISSING_QUANTITY)
        else
            removeFlags(ErrorFlags.MISSING_QUANTITY)

        return errorFlags.isEmpty()
    }

    fun isPriceValid(): Boolean {
        if(price.value.isNullOrEmpty())
            errorFlags.add(ErrorFlags.MISSING_PRICE)
        else
            removeFlags(ErrorFlags.MISSING_PRICE)

        return errorFlags.isEmpty()
    }

    private fun removeFlags(flag: Enum<ErrorFlags>) {
        while(errorFlags.contains(flag))
            errorFlags.remove(flag)
    }

    fun isAllCorrect(): Boolean {
        if(isProductNameValid() && isDescriptionValid() && isQuantityValid() && isPriceValid())
            return true
        return false
    }

    enum class ErrorFlags {
        MISSING_PRODUCT_NAME,
        INVALID_PRODUCT_NAME,
        MISSING_DESCRIPTION,
        INVALID_DESCRIPTION,
        MISSING_QUANTITY,
        MISSING_PRICE,
    }

}