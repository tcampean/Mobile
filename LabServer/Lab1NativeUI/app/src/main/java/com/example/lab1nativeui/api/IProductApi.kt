package com.example.lab1nativeui.api

import com.example.lab1nativeui.model.ProductEntity
import retrofit2.Call
import retrofit2.http.*

interface IProductApi {

    @GET("/products")
    fun getProducts() : Call<List<ProductEntity>>

    @DELETE("/products/{id}")
    fun deleteProduct(@Path("id") id: Int) : Call<ProductEntity>

    @POST("/products")
    fun addProduct(@Body product: ProductEntity) : Call<ProductEntity>

    @POST("/productsid")
    fun addProductWithId(@Body product: ProductEntity) : Call<ProductEntity>

    @PUT("/products/{id}")
    fun updateProduct(@Path("id") id: Int, @Body product: ProductEntity) : Call<ProductEntity>
}