package com.example.lab1nativeui.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab1nativeui.model.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            var value = instance
            if(value == null) {
                value = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bakery_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance = value
            }

            return value
        }
    }
}