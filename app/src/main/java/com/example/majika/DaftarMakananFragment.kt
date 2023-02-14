package com.example.majika

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.adapter.MenuListAdapter
import com.example.majika.models.MenuModel
import com.example.majika.network.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DaftarMakananFragment : Fragment() {
    private val menuFoodList: MutableList<MenuModel?> = mutableListOf()
    private val menuDrinkList: MutableList<MenuModel?> = mutableListOf()
    private var itemsInTheCartList: MutableList<MenuModel?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar_makanan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)
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
        var recyclerViewMenuFood = view.findViewById<RecyclerView>(R.id.recyclerViewMenuFood)
        recyclerViewMenuFood.layoutManager = LinearLayoutManager(activity)
        val foodAdapter = MenuListAdapter(menuFoodList)
        recyclerViewMenuFood.adapter = foodAdapter

        var recyclerViewMenuDrink = view.findViewById<RecyclerView>(R.id.recyclerViewMenuDrink)
        recyclerViewMenuDrink.layoutManager = LinearLayoutManager(activity)
        val drinkAdapter = MenuListAdapter(menuDrinkList)
        recyclerViewMenuDrink.adapter = drinkAdapter

        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d("Food Menu List Sebelum:", menuFoodList.toString())
                menuFoodList.addAll(getMenuFoodData()!!)
                Log.d("Food Menu List Sesudah:", menuFoodList.toString())

                Log.d("Drink Menu List Sebelum:", menuDrinkList.toString())
                menuDrinkList.addAll(getMenuDrinkData()!!)
                Log.d("Drink Menu List Sesudah:", menuDrinkList.toString())

                foodAdapter.notifyDataSetChanged()
                drinkAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
            }
        }
    }
}