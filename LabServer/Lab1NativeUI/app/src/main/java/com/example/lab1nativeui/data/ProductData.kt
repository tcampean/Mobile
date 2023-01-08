package com.example.lab1nativeui.data

import com.example.lab1nativeui.R
import com.example.lab1nativeui.model.Product

object ProductData {

    private val typeList = listOf("Bakery", "Cake", "Cupcake", "Cookie", "Donut")

    fun loadProducts(): ArrayList<Product> {
        return arrayListOf(
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