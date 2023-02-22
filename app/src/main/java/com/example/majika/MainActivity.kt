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
        replaceFragment(DaftarMakananFragment())
        replaceHeader("daftar_makanan")

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.cabang_restoran -> {
                    replaceFragment(CabangRestoranFragment())
                    replaceHeader("cabang_restoran")
                    true
                }
                R.id.daftar_makanan -> {
                    replaceFragment(DaftarMakananFragment())
                    replaceHeader("daftar_makanan")
                    true
                }
                R.id.twibbon -> {
                    replaceFragment(TwibbonFragment())
                    replaceHeader("twibbon")
                    true
                }
                R.id.keranjang -> {
                    replaceFragment(KeranjangFragment())
                    replaceHeader("keranjang")
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

    private fun replaceHeader(fragment: String) {
        val headerFragment: Fragment = HeaderFragment.newInstance(fragment)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.header_frame_layout, headerFragment)
        fragmentTransaction.commit()
    }
}