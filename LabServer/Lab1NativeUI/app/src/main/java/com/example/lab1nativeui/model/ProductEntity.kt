package com.example.lab1nativeui.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Product", indices = [Index(value = ["name"], unique = true)])
data class ProductEntity(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "quantity") val quantity: Int?,
    @ColumnInfo(name = "price") val price: Double?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)