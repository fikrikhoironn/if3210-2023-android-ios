package com.example.majika

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.RoomDatabase
import com.example.majika.adapter.MenuListAdapter
import com.example.majika.data.MajikaRoomDatabase
import com.example.majika.data.entity.Cart
import com.example.majika.databinding.FragmentDaftarMakananBinding
import com.example.majika.models.MenuModel
import com.example.majika.network.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DaftarMakananFragment : Fragment(), MenuListAdapter.MenuListClickListener {
    private val menuFoodList: MutableList<MenuModel?> = mutableListOf()
    private val menuDrinkList: MutableList<MenuModel?> = mutableListOf()
    private var itemsInTheCartList: MutableList<Cart?> = mutableListOf()

    private var searchQuery: String = ""
    private val menuFoodSearchList: MutableList<MenuModel?> = mutableListOf()
    private val menuDrinkSearchList: MutableList<MenuModel?> = mutableListOf()

    private lateinit var binding: FragmentDaftarMakananBinding
    private lateinit var database: MajikaRoomDatabase


    private var temperatureSensorListener: SensorEventListener? = null
    private lateinit var sensorManager: SensorManager
    private lateinit var temperatureSensor: Sensor


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDaftarMakananBinding.inflate(inflater, container, false)
        database = MajikaRoomDatabase.getInstance(requireContext())

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

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        if (temperatureSensor == null) {
            Log.e("SUHU", "Perangkat tidak memiliki sensor suhu")
        } else {
            Log.d("SUHU", "Perangkat memiliki sensor suhu")
            val temperatureListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    val temperature = event?.values?.get(0)
                    Log.d("TAG", "Suhu: $temperature")

                    // Update text pada TextView tvSuhu
//                    activity?.runOnUiThread {
//                        view?.findViewById<TextView>(R.id.tvSuhu)?.text = "Suhu: $temperature"
//                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Tidak perlu diimplementasikan
                }
            }

            sensorManager.registerListener(
                temperatureListener,
                temperatureSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            // Simpan listener di dalam variabel instance agar bisa dimatikan di onDestroyView()
            temperatureSensorListener = temperatureListener
        }
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
        binding.tvMenuFood.visibility = View.GONE
        binding.tvMenuDrink.visibility = View.GONE
        binding.pbMenu?.visibility = View.VISIBLE

        binding.recyclerViewMenuFood.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewMenuFood.adapter = MenuListAdapter(menuFoodSearchList, this)

        binding.recyclerViewMenuDrink.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewMenuDrink.adapter = MenuListAdapter(menuDrinkSearchList, this)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                var menuApiFoodList = getMenuFoodData()!!.associateBy { it?.name }
                var menuApiDrinkList = getMenuDrinkData()!!.associateBy { it?.name }

                itemsInTheCartList.addAll(database.cartDao().getAll())

                for (cartItem in itemsInTheCartList) {
                    menuApiFoodList[cartItem?.name]?.totalInCart = cartItem?.totalInCart
                    menuApiFoodList[cartItem?.name]?.priceInCart = cartItem?.priceInCart

                    menuApiDrinkList[cartItem?.name]?.totalInCart = cartItem?.totalInCart
                    menuApiDrinkList[cartItem?.name]?.priceInCart = cartItem?.priceInCart
                }

                menuFoodList.addAll(menuApiFoodList.values.toList())
                menuFoodSearchList.addAll(menuApiFoodList.values.toList())
                menuDrinkList.addAll(menuApiDrinkList.values.toList())
                menuDrinkSearchList.addAll(menuApiDrinkList.values.toList())

                binding.tvMenuFood.visibility = View.VISIBLE
                binding.tvMenuDrink.visibility = View.VISIBLE
                binding.pbMenu?.visibility = View.GONE
                (binding.recyclerViewMenuDrink.adapter as MenuListAdapter).notifyDataSetChanged()
                (binding.recyclerViewMenuFood.adapter as MenuListAdapter).notifyDataSetChanged()
            } catch (e: Exception) {
            }
        }
    }

    private fun search() {
        menuFoodSearchList.clear()
        menuDrinkSearchList.clear()

        menuFoodList.filterTo(menuFoodSearchList) {
            it?.name!!.contains(
                searchQuery,
                ignoreCase = true
            )
        }
        menuDrinkList.filterTo(menuDrinkSearchList) {
            it?.name!!.contains(
                searchQuery,
                ignoreCase = true
            )
        }

        (binding.recyclerViewMenuDrink.adapter as MenuListAdapter).notifyDataSetChanged()
        (binding.recyclerViewMenuFood.adapter as MenuListAdapter).notifyDataSetChanged()
    }

    override fun addToCartClickListener(menu: MenuModel) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("DaftarMakananFragment", "Added to cart")
            Log.d("Makanan Sebelum", database.cartDao().getAll().toString())
            database.cartDao().insertAll(
                Cart(
                    menu.name!!,
                    menu.description,
                    menu.currency,
                    menu.price,
                    menu.sold,
                    menu.type,
                    menu.totalInCart,
                    menu.priceInCart
                )
            )
            Log.d("Makanan Sesudah", database.cartDao().getAll().toString())
        }
//        TODO("Not yet implemented")
    }

    override fun updateCartClickListener(menu: MenuModel) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("DaftarMakananFragment", "Cart updated")
            Log.d("Makanan Sebelum", database.cartDao().getAll().toString())
            database.cartDao().update(
                menu.name!!,
                menu.totalInCart!!,
                menu.priceInCart!!
            )
            Log.d("Makanan Sesudah", database.cartDao().getAll().toString())
        }
//        TODO("Not yet implemented")
    }

    override fun removeFromCartClickListener(menu: MenuModel) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("DaftarMakananFragment", "Removed from cart")
            Log.d("Makanan Sebelum", database.cartDao().getAll().toString())
            database.cartDao().delete(
                Cart(
                    menu.name!!,
                    menu.description,
                    menu.currency,
                    menu.price,
                    menu.sold,
                    menu.type,
                    menu.totalInCart,
                    menu.priceInCart
                )
            )
            Log.d("Makanan Sesudah", database.cartDao().getAll().toString())
        }
//        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Hapus listener dari SensorManager
        temperatureSensorListener?.let {
            sensorManager.unregisterListener(it)
        }

        // Kosongkan variabel instance yang menyimpan listener
        temperatureSensorListener = null
    }
}