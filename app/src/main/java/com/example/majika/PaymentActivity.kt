package com.example.majika

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.majika.network.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PaymentActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        replaceHeader("payment")

        val retryButton = findViewById<TextView>(R.id.btn_retry)
        retryButton.visibility = View.GONE

        val price = intent.getStringExtra("totalPrice")
        Log.d("Main", "Total Price: $price")
        val textView = findViewById<TextView>(R.id.tv_textView)
        textView.text = "Total Price: Rp ${intent.getStringExtra("totalPrice")}"
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
//                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        Log.v("Main", "Scan result: ${it.text}")
                        val response = Api.retrofitService.postPayment(it.text)
                        val imageViewSuccess = findViewById<ImageView>(R.id.imageViewSuccess)
                        val imageViewFailed = findViewById<ImageView>(R.id.imageViewFailed)
                        if (response.status == "SUCCESS"){
                            Log.v("Main", "Payment Success")
                            this@PaymentActivity.runOnUiThread(java.lang.Runnable {
                                val statusText = findViewById<TextView>(R.id.status_text)
                                imageViewFailed.visibility = ImageView.GONE
                                imageViewSuccess.visibility = ImageView.VISIBLE
                                statusText.text = "Payment Success"
                                val handler = android.os.Handler()
                                handler.postDelayed({
                                    val intent = Intent(this@PaymentActivity, MainActivity::class.java)
                                    startActivity(intent)
                                }, 5000)
                            })
                        } else {
                            Log.v("Main", "Payment Failed")
                            this@PaymentActivity.runOnUiThread(java.lang.Runnable {
                                val statusText = findViewById<TextView>(R.id.status_text)
                                imageViewFailed.visibility = ImageView.VISIBLE
                                statusText.text = "Payment Failed"
                                retryButton.visibility = View.VISIBLE
                            })
                        }
                    } catch (e: Exception) {
                        Log.e("Main", "Error: ${e.message}")
                    }
                }


            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        retryButton.setOnClickListener {
            codeScanner.startPreview()
            retryButton.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun replaceHeader(fragment: String) {
        val headerFragment: Fragment = HeaderFragment.newInstance(fragment)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.header_frame_layout, headerFragment)
        fragmentTransaction.commit()
    }
}