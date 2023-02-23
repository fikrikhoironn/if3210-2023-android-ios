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
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

private const val ARG_PARAM1 = "param1"

class HeaderFragment : Fragment(), SensorEventListener {
    private var param1: String? = null
    private var temperatureSensor: Sensor? = null
    private lateinit var sensorManager: SensorManager
    private var isSupportSensor: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) == null) {
            isSupportSensor = false
            Log.e("SUHU", "Perangkat tidak memiliki sensor suhu")
        } else {
            isSupportSensor = true
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val layout = when (param1) {
            "daftar_makanan" -> {
                R.layout.fragment_header_daftar_makanan
            }
            "payment" -> {
                R.layout.fragment_header_payment
            }
            else -> {
                R.layout.fragment_header
            }
        }
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = when (param1) {
            "daftar_makanan" -> {
                "Menu"
            }
            "keranjang" -> {
                "Keranjang"
            }
            "cabang_restoran" -> {
                "Cabang Restoran"
            }
            "twibbon" -> {
                "Twibbon"
            }
            "payment" -> {
                "Payment"
            }
            else -> {
                R.layout.fragment_header
            }
        }
        view.findViewById<TextView>(R.id.tvHeader).text = title.toString()

        if (!isSupportSensor) {
            view?.findViewById<TextView>(R.id.tvSuhu)?.text = "Suhu: -"
        } else {
            if (param1 == "daftar_makanan") {
                view?.findViewById<TextView>(R.id.tvSuhu)?.text = "Suhu: 0°C"
            }
        }

        if (param1 == "payment") {
            view.findViewById<ImageButton>(R.id.ibBack).setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    companion object {
        @JvmStatic fun newInstance(param1: String) =
                HeaderFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        // Update text pada TextView tvSuhu
        if (param1 == "daftar_makanan") {
            view?.findViewById<TextView>(R.id.tvSuhu)?.text = "Suhu: ${p0?.values?.get(0)}°C"
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // Tidak perlu diimplementasikan
    }
}