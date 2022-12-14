package com.example.lab1nativeui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab1nativeui.screen.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MainFragment())
            .commit()
    }
}