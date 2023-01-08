package com.example.lab1nativeui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val type: String,
    val description: String,
    val quantity: Int,
    val price: Double
): Parcelable
