package com.example.majika.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.data.entity.Cart
import com.example.majika.models.MenuModel

class CartListAdapter(
    private val cartList: List<Cart?>?,
    val clickListener: CartListClickListener
) : RecyclerView.Adapter<CartListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartListAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_cart_list_row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (cartList == null) {
            return 0
        }
        return cartList?.size!!
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(cartList?.get(position)!!)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCartName: TextView = view.findViewById(R.id.tvCartName)
        val tvCartPrice: TextView = view.findViewById(R.id.tvCartPrice)
        val tvCartCount: TextView = view.findViewById(R.id.tvCartCount)
        val ivCartImageMinus: ImageView = view.findViewById(R.id.ivCartImageMinus)
        val ivCartimageAddOne: ImageView = view.findViewById(R.id.ivCartimageAddOne)

        fun bind(cart: Cart) {
            tvCartName.text = cart.name
            tvCartPrice.text = "${cart.currency} ${cart.price}"
            tvCartCount.text = cart?.totalInCart.toString()

            ivCartImageMinus.setOnClickListener {
                Log.d("TEST", cart.toString())
                var total: Int = cart.totalInCart!!
                total--

                if (total > 0) {
                    cart.totalInCart = total
                    Log.d("PRICE SEBELUM", cart.priceInCart.toString())
                    cart.priceInCart = cart.price?.times(cart.totalInCart!!)
                    Log.d("PRICE SETELAH", cart.priceInCart.toString())
                    clickListener.updateCartClickListener(cart)
                    tvCartCount.text = cart.totalInCart.toString()
                } else {
                    cart.totalInCart = null
                    Log.d("PRICE SEBELUM", cart.priceInCart.toString())
                    cart.priceInCart = null
                    Log.d("PRICE SETELAH", cart.priceInCart.toString())
                    clickListener.removeFromCartClickListener(cart)
                }
            }

            ivCartimageAddOne.setOnClickListener {
                Log.d("TEST", cart.toString())
                var total: Int = cart.totalInCart!!
                total++

                cart.totalInCart = total
                Log.d("PRICE SEBELUM", cart.priceInCart.toString())
                cart.priceInCart = cart.price?.times(cart.totalInCart!!)
                Log.d("PRICE SETELAH", cart.priceInCart.toString())
                clickListener.updateCartClickListener(cart)
                tvCartCount.text = cart.totalInCart.toString()
            }
        }
    }

    interface CartListClickListener {
        fun updateCartClickListener(cart: Cart)
        fun removeFromCartClickListener(cart: Cart)
    }
}
