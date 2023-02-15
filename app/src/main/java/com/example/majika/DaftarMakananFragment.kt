package com.example.majika

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.adapter.MenuListAdapter
import com.example.majika.databinding.FragmentDaftarMakananBinding
import com.example.majika.models.MenuModel
import com.example.majika.network.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DaftarMakananFragment : Fragment(), MenuListAdapter.MenuListClickListener {
    private val menuFoodList: MutableList<MenuModel?> = mutableListOf()
    private val menuDrinkList: MutableList<MenuModel?> = mutableListOf()
    private var itemsInTheCartList: MutableList<MenuModel?>? = null

    private var searchQuery: String = ""
    private val menuFoodSearchList: MutableList<MenuModel?> = mutableListOf()
    private val menuDrinkSearchList: MutableList<MenuModel?> = mutableListOf()

    private lateinit var binding: FragmentDaftarMakananBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDaftarMakananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)

        binding.svMenu.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                searchQuery = query ?: ""
                search()
                return true
            }

        })
    }

    private suspend fun getMenuFoodData(): List<MenuModel?>? {
        val menu = Api.retrofitService.getMenuFood()

        return menu.data
    }

    private suspend fun getMenuDrinkData(): List<MenuModel?>? {
        val menu = Api.retrofitService.getMenuDrink()

        return menu.data
    }

    private fun initRecyclerView(view: View) {
        binding.recyclerViewMenuFood.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewMenuFood.adapter = MenuListAdapter(menuFoodSearchList, this)

        binding.recyclerViewMenuDrink.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewMenuDrink.adapter = MenuListAdapter(menuDrinkSearchList, this)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val menuApiFoodList = getMenuFoodData()!!
                val menuApiDrinkList = getMenuDrinkData()!!

                menuFoodList.addAll(menuApiFoodList)
                menuFoodSearchList.addAll(menuApiFoodList)
                menuDrinkList.addAll(menuApiDrinkList)
                menuDrinkSearchList.addAll(menuApiDrinkList)

                (binding.recyclerViewMenuDrink.adapter as MenuListAdapter).notifyDataSetChanged()
                (binding.recyclerViewMenuFood.adapter as MenuListAdapter).notifyDataSetChanged()
            } catch (e: Exception) {
            }
        }
    }

    private fun search() {
        menuFoodSearchList.clear()
        menuDrinkSearchList.clear()

        menuFoodList.filterTo(menuFoodSearchList) { it?.name!!.contains(searchQuery, ignoreCase = true) }
        menuDrinkList.filterTo(menuDrinkSearchList) { it?.name!!.contains(searchQuery, ignoreCase = true) }

        (binding.recyclerViewMenuDrink.adapter as MenuListAdapter).notifyDataSetChanged()
        (binding.recyclerViewMenuFood.adapter as MenuListAdapter).notifyDataSetChanged()
    }

    override fun addToCartClickListener(menu: MenuModel) {
        TODO("Not yet implemented")
    }

    override fun updateCartClickListener(menu: MenuModel) {
        TODO("Not yet implemented")
    }

    override fun removeFromCartClickListener(menu: MenuModel) {
        TODO("Not yet implemented")
    }
}