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
import android.widget.TextView

private const val ARG_PARAM1 = "param1"

class HeaderFragment : Fragment() {
    private var param1: String? = null
    private var temperatureSensorListener: SensorEventListener? = null
    private lateinit var sensorManager: SensorManager
    private lateinit var temperatureSensor: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val layout = when (param1) {
            "daftar_makanan" -> {
                R.layout.fragment_header_daftar_makanan
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
            else -> {
                R.layout.fragment_header
            }
        }
        view.findViewById<TextView>(R.id.tvHeader).text = title.toString()

        if (param1 == "daftar_makanan") {
            sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

            view.findViewById<TextView>(R.id.tvSuhu)?.text = "Suhu: "

            if (temperatureSensor == null) {
                Log.e("SUHU", "Perangkat tidak memiliki sensor suhu")
            } else {
                val temperatureListener = object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent?) {
                        val temperature = event?.values?.get(0)

                        // Update text pada TextView tvSuhu
                        activity?.runOnUiThread {
                            view.findViewById<TextView>(R.id.tvSuhu)?.text = "Suhu: $temperature"
                        }
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

    companion object {
        @JvmStatic fun newInstance(param1: String) =
                HeaderFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
}