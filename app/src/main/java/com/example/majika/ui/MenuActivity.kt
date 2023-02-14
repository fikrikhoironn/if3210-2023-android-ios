package com.example.majika.ui

import android.os.Bundle
import android.util.Log
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.adapter.MenuListAdapter
import com.example.majika.databinding.ActivityMenuBinding
import com.example.majika.models.MenuApiModel
import com.example.majika.models.MenuModel
import com.example.majika.network.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private val menuList: MutableList<MenuModel?> = mutableListOf()
    private var itemsInTheCartList: MutableList<MenuModel?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initRecyclerView()
    }

    private suspend fun getMenuData(): List<MenuModel?>? {
        val menu = Api.retrofitService.getMenuFood()

        return menu.data
    }

    private fun initRecyclerView() {
        binding.recyclerViewMenu.layoutManager = LinearLayoutManager(this)
        val adapter = MenuListAdapter(menuList)
        binding.recyclerViewMenu.adapter = adapter

        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d("Menu List Sebelum:", menuList.toString())
                menuList.addAll(getMenuData()!!)
                Log.d("Menu List Sesudah:", menuList.toString())

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
            }
        }
    }
}