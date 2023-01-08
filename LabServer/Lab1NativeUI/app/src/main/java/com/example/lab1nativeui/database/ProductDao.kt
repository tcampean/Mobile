package com.example.lab1nativeui.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lab1nativeui.model.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun getProducts(): LiveData<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertProduct(productEntity: ProductEntity): Long

    @Update
    fun updateProduct(productEntity: ProductEntity): Int

    @Delete
    fun deleteProduct(productEntity: ProductEntity): Int


}