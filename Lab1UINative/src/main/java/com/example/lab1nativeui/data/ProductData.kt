package com.example.lab1nativeui.data

import com.example.lab1nativeui.R
import com.example.lab1nativeui.model.Product

object ProductData {

    private val typeList = listOf("Bakery", "Cake", "Cupcake", "Cookie", "Donut")

    fun loadProducts(): ArrayList<Product> {
        return arrayListOf(
            Product(name = "Chocolate Cupcake", type = "Cupcake", description = "Delicious cupcakes with chocolate chips and chocolate cream filling", quantity = 10, price = 1.99),
            Product(name = "Garlic Bread", type = "Bakery", description = "Tasteful bread with scrumptious garlic bits", quantity = 20, price = 5.33),
            Product(name = "Strawberry Cake", type = "Cake", description = "Sweet cake with strawberries and vanilla filling", quantity = 20, price = 20.33),
            Product(name = "Vanilla Cookie", type = "Cookie", description = "Amazing cookie with vanilla bits and crusty exterior", quantity = 20, price = 2.99),
            Product(name = "Duo Donut", type = "Donut", description = "Donut with vanilla and chocolate filling and with sprinkles on top ", quantity = 20, price = 1.09),
            Product(name = "Gluten-free Cupcake", type = "Cupcake", description = "Simple gluten-free cupcake with lemon", quantity = 20, price = 2.39)
        )
    }

    fun getPosType(type: String): Int {
        return typeList.indexOf(type)
    }

    fun getTypeList(): List<String> {
        return typeList
    }

    fun getAppropriatePicture(type: String): Int {
        return when(type) {
            "Bakery" -> R.drawable.bakery
            "Cake" -> R.drawable.cakeslice
            "Cupcake" -> R.drawable.cupcake
            "Cookie" -> R.drawable.cookies
            else -> R.drawable.donut
        }
    }
}