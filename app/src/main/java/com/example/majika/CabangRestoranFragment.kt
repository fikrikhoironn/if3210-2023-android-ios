package com.example.majika

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.adapter.CabangRestoranAdapter
import androidx.recyclerview.widget.LinearLayoutManager

class CabangRestoranFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CabangRestoranAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cabang_restoran, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        var recyclerView = itemView.findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = CabangRestoranAdapter()
        }
    }
}