package com.example.majika

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.adapter.CabangRestoranAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.majika.models.CabangRestoranModel
import com.example.majika.network.CabangRestoranApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CabangRestoranFragment : Fragment() {

    private val cabangRestoranList: MutableList<CabangRestoranModel?> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cabang_restoran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)
    }

    private suspend fun getCabangRestoranData(): List<CabangRestoranModel?>? {
        val menu = CabangRestoranApi.retrofitService.getCabangRestoran()

        return menu.data
    }

    private fun initRecyclerView(view: View) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val cabangRestoranAdapter = CabangRestoranAdapter(cabangRestoranList)
        recyclerView.adapter = cabangRestoranAdapter

        CoroutineScope(Dispatchers.Main).launch {
            try {
                cabangRestoranList.addAll(getCabangRestoranData()!!)
                Log.d("Cabang Restoran List :", cabangRestoranList.toString())

                cabangRestoranAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
            }
        }
    }
}