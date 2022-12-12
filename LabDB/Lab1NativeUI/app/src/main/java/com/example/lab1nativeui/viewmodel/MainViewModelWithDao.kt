package com.example.lab1nativeui.viewmodel

import android.content.IntentFilter
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab1nativeui.data.ProductRepository
import com.example.lab1nativeui.database.ProductDao
import com.example.lab1nativeui.model.Product
import com.example.lab1nativeui.model.ProductEntity
import com.example.lab1nativeui.util.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModelWithDao: ViewModel() {

    var position: Int = 0

    private lateinit var productDao: ProductDao
    private lateinit var repository: ProductRepository
    lateinit var productList: LiveData<List<ProductEntity>>

    private val _action = MutableLiveData(AppConstants.ACTION_INIT)
    val action: LiveData<String>
        get() = _action

    private val _successful = MutableLiveData<Long>()
    val successful: LiveData<Long>
        get() = _successful

    fun setRepository(repo: ProductRepository) {
        repository = repo
        productList = repository.productList
    }

    fun setDao(dao: ProductDao) {
        productDao = dao
    }

    fun insert(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        _action.postValue(AppConstants.ACTION_CREATE)
        try {
            _successful.postValue(repository.insert(ProductEntity(product.name, product.type, product.description, product.quantity, product.price)))
        }catch (e: SQLiteConstraintException) {
            _successful.postValue(-1)
            Log.e("Database Error", "Error inserting a new product \n" + e.message)
        }
    }

    fun delete(productEntity: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        getProductPosition(productEntity)
        _action.postValue(AppConstants.ACTION_DELETE)
        try {
            _successful.postValue(repository.delete(productEntity).toLong())
        }catch (e: SQLiteException) {
            _successful.postValue(-1)
            Log.e("Database Error", "Error deleting a product \n" + e.message)
        }
    }

    fun update(productEntity: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        getProductPosition(productEntity)
        _action.postValue(AppConstants.ACTION_UPDATE)
        try {
            _successful.postValue(repository.update(productEntity).toLong())
        }catch (e: SQLiteConstraintException) {
            _successful.postValue(-1)
            Log.e("Database Error", "Error updating a product \n" + e.message)
        }
    }

    fun setupReceiverIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(AppConstants.CREATE_OBJECT)
        intentFilter.addAction(AppConstants.UPDATE_OBJECT)
        intentFilter.addAction(AppConstants.DELETE_OBJECT)

        return intentFilter
    }

    fun convertToProduct(productEntity: ProductEntity): Product {
        return Product(id = productEntity.id,
            name = productEntity.name!!,
            description = productEntity.description!!,
            type = productEntity.type!!,
            quantity = productEntity.quantity!!,
            price = productEntity.price!!
        )
    }

    fun convertToEntity(product: Product): ProductEntity {
        return ProductEntity(id = product.id,
            name = product.name,
            description = product.description,
            type = product.type,
            quantity = product.quantity,
            price = product.price
        )
    }

    fun convertListToProduct(): ArrayList<Product> {
        val resultList: ArrayList<Product> = arrayListOf()
        for(prod: ProductEntity in productList.value!!) {
            resultList.add(convertToProduct(prod))
        }
        return resultList
    }

    fun getProductPosition(productEntity: ProductEntity) {
        var pos = 0
        var found = false
        for(p: ProductEntity in productList.value!!) {
            if(p.id == productEntity.id) {
               position = pos
                found = true
            }
            pos++
        }
        if(!found)
            position = -1
    }
}