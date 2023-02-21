package com.example.majika

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.majika.adapter.CartListAdapter
import com.example.majika.adapter.MenuListAdapter
import com.example.majika.data.MajikaRoomDatabase
import com.example.majika.data.entity.Cart
import com.example.majika.databinding.FragmentDaftarMakananBinding
import com.example.majika.databinding.FragmentKeranjangBinding
import com.example.majika.models.MenuModel
import com.example.majika.network.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KeranjangFragment : Fragment(), CartListAdapter.CartListClickListener {
    private var itemsInTheCartList: MutableList<Cart?> = mutableListOf()
    private val cartList: MutableList<Cart?> = mutableListOf()
    private var totalCartPrice: Int? = 0

    private lateinit var binding: FragmentKeranjangBinding
    private lateinit var database: MajikaRoomDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKeranjangBinding.inflate(inflater, container, false)
        database = MajikaRoomDatabase.getInstance(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCartCheckout.setOnClickListener {
            handleCheckout()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewCart.adapter = CartListAdapter(cartList, this)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                itemsInTheCartList.clear()
                itemsInTheCartList.addAll(database.cartDao().getAll())

                cartList.clear()
                cartList.addAll(itemsInTheCartList)

                totalCartPrice = database.cartDao().calculatePrice()

                if (totalCartPrice == null) {
                    totalCartPrice = 0
                    binding.tvCartCheckout.visibility = View.GONE
                }
                binding.tvCartTotal.text = totalCartPrice.toString()

                (binding.recyclerViewCart.adapter as CartListAdapter).notifyDataSetChanged()
            } catch (e: Exception) {
            }
        }
    }

    private fun handleCheckout() {
        val intent = Intent(activity, PaymentActivity::class.java)
        activity?.startActivity(intent)

    }

    override fun updateCartClickListener(cart: Cart) {
        CoroutineScope(Dispatchers.Main).launch {
            database.cartDao().update(
                cart.name!!,
                cart.totalInCart!!,
                cart.priceInCart!!
            )

            initRecyclerView()
        }
    }

    override fun removeFromCartClickListener(cart: Cart) {
        CoroutineScope(Dispatchers.Main).launch {
            database.cartDao().delete(
                Cart(
                    cart.name!!,
                    cart.description,
                    cart.currency,
                    cart.price,
                    cart.sold,
                    cart.type,
                    cart.totalInCart,
                    cart.priceInCart
                )
            )

            initRecyclerView()
        }
    }
}