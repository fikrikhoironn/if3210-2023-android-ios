package com.example.majika.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.CabangRestoranFragment
import com.example.majika.R
import com.example.majika.models.CabangRestoranApiModel
import com.example.majika.models.CabangRestoranModel
import com.example.majika.network.CabangRestoranApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CabangRestoranAdapter(
    private val cabangRestoranList: List<CabangRestoranModel?>?
): RecyclerView.Adapter<CabangRestoranAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CabangRestoranAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_cabang_restoran, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (cabangRestoranList == null) {
            return 0
        }
        return cabangRestoranList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cabangRestoranList?.get(position)!!)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restoranName: TextView = view.findViewById(R.id.restoranName)
        val popularFood: TextView = view.findViewById(R.id.popularFood)
        val restoranAddress: TextView = view.findViewById(R.id.restoranAddress)
        val contactPerson: TextView = view.findViewById(R.id.contactPerson)
        val btnMap: Button = view.findViewById(R.id.btnMap)
        val context = view.context

        fun bind(cabangRestoran: CabangRestoranModel) {
            restoranName.text = cabangRestoran.name
            popularFood.text = cabangRestoran.popular_food
            restoranAddress.text = cabangRestoran.address
            contactPerson.text = "Contact ${cabangRestoran.contact_person} (${cabangRestoran.phone_number})"
            btnMap.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:${cabangRestoran.latitude},${cabangRestoran.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            }
        }
    }


}