package com.example.lab1nativeui.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.lab1nativeui.database.ProductDao
import com.example.lab1nativeui.model.ProductEntity

class ProductRepository(private val dao: ProductDao) {

    val productList: LiveData<List<ProductEntity>> = dao.getProducts()

    suspend fun insert(productEntity: ProductEntity): Long {
        return dao.insertProduct(productEntity)
    }

    suspend fun delete(productEntity: ProductEntity): Int {
        return dao.deleteProduct(productEntity)
    }

    suspend fun update(productEntity: ProductEntity): Int {
        return dao.updateProduct(productEntity)
    }
}