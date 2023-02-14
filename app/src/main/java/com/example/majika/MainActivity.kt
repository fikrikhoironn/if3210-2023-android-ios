package com.example.majika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cabangRestoranFragment = CabangRestoranFragment()
        val testFragment = TestFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, cabangRestoranFragment)
            commit()
        }

        val btnCabangRestoranFragment = findViewById<View>(R.id.btnCabangRestoran)
        val btnTestFragment = findViewById<View>(R.id.btnTestFragment)

        btnCabangRestoranFragment.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, cabangRestoranFragment)
                addToBackStack(null)
                commit()
            }
        }

        btnTestFragment.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, testFragment)
                addToBackStack(null)
                commit()
            }
        }
    }
}