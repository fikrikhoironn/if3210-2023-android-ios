package com.example.majika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.majika.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.cabang_restoran -> {
                    val actionBar: ActionBar? = supportActionBar
                    actionBar?.setTitle("Cabang Restoran")
                    actionBar?.setDisplayHomeAsUpEnabled(true)
                    replaceFragment(CabangRestoranFragment())
                    true
                }
                R.id.daftar_makanan -> {
                    val actionBar: ActionBar? = supportActionBar
                    actionBar?.setTitle("Daftar Menu")
                    actionBar?.setDisplayHomeAsUpEnabled(true)
                    replaceFragment(DaftarMakananFragment())
                    true
                }
                R.id.twibbon -> {
                    val actionBar: ActionBar? = supportActionBar
                    actionBar?.setTitle("Twibbon")
                    actionBar?.setDisplayHomeAsUpEnabled(true)
                    replaceFragment(TwibbonFragment())
                    true
                }
                R.id.keranjang -> {
                    val actionBar: ActionBar? = supportActionBar
                    actionBar?.setTitle("Keranjang")
                    actionBar?.setDisplayHomeAsUpEnabled(true)
                    replaceFragment(KeranjangFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}