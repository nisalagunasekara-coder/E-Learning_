package com.example.e_learning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This project uses XML layouts. Use an existing simple layout to avoid Compose.
        setContentView(R.layout.activity_splash)
    }
}