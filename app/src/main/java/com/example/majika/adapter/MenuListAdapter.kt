package com.example.majika.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.models.MenuModel

class MenuListAdapter(
    private val menuList: List<MenuModel?>?
) : RecyclerView.Adapter<MenuListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuListAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_menu_list_row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (menuList == null) {
            return 0
        }
        return menuList?.size!!
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(menuList?.get(position)!!)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMenuName: TextView = view.findViewById(R.id.tvMenuName)
        val tvMenuPrice: TextView = view.findViewById(R.id.tvMenuPrice)
        val tvMenuSold: TextView = view.findViewById(R.id.tvMenuSold)
        val tvMenuDescription: TextView = view.findViewById(R.id.tvMenuDescription)
        val tvMenuAddToCart: TextView = view.findViewById(R.id.tvMenuAddToCart)

        fun bind(menu: MenuModel) {
            tvMenuName.text = menu.name
            tvMenuPrice.text = "${menu.currency} ${menu.price}"
            tvMenuSold.text = "${menu.sold} terjual"
            tvMenuDescription.text = menu.description
        }
    }
}