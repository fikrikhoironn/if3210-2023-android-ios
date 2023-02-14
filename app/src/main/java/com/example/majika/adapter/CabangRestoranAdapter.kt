package com.example.majika.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.CabangRestoranFragment
import com.example.majika.R

class CabangRestoranAdapter: RecyclerView.Adapter<CabangRestoranAdapter.ViewHolder>() {

    private val name = arrayOf(
        "Restoran A", "Restoran B", "Restoran C", "Restoran D", "Restoran E"
    )

    private val address = arrayOf(
        "Jalan A", "Jalan B", "Jalan C", "Jalan D", "Jalan E"
    )

    private val phone_number = arrayOf(
        "1234", "1236", "3131", "3121", "5242"
    )

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var restoranName: TextView
        var restoranAddress: TextView
        var restoranPhone: TextView

        init {
            restoranName = itemView.findViewById(R.id.restoranName)
            restoranAddress = itemView.findViewById(R.id.restoranAddress)
            restoranPhone = itemView.findViewById(R.id.restoranPhone)

            itemView.setOnClickListener {
                var position: Int = bindingAdapterPosition
                val context = itemView.context
                val intent = Intent(context, CabangRestoranFragment::class.java).apply {
                    putExtra("NUMBER", position)
                    putExtra("NAME", restoranName.text)
                    putExtra("ADDRESS", restoranAddress.text)
                    putExtra("PHONE", restoranPhone.text)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_cabang_restoran, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.restoranName.text = name[i]
        viewHolder.restoranAddress.text = address[i]
        viewHolder.restoranPhone.text = phone_number[i]

    }

    override fun getItemCount(): Int {
        return name.size
    }

}