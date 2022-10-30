package com.example.lab1nativeui.data

import com.example.lab1nativeui.model.Product

class ProductData {

    fun loadProducts(): ArrayList<Product> {
        return arrayListOf(
            Product(name = "Chocolate Cupcakes", type = "Cakes", description = "Cupcakes with chocolate", quantity = 10, price = 1.99)
        )
    }
}