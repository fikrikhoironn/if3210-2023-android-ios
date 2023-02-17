package com.example.majika

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Camera
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.SessionConfiguration
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class TwibbonFragment : Fragment() {

    private lateinit var handler: Handler
    private lateinit var handlerThread: HandlerThread
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var textureView: TextureView
    private lateinit var captureRequest: CaptureRequest
    private lateinit var capReq: CaptureRequest.Builder
    private var cameraDevice: CameraDevice ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_twibbon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPermissions()

        textureView = view.findViewById(R.id.textureView)
        cameraManager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler((handlerThread).looper)
        cameraId = getCameraId(CameraCharacteristics.LENS_FACING_FRONT)

        textureView.surfaceTextureListener = object: TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
                //
            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                cameraDevice?.close()
                cameraDevice = null
                return true
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                //
            }
        }
    }

    private fun openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            cameraManager.openCamera(cameraId, object: CameraDevice.StateCallback() {
                override fun onOpened(p0: CameraDevice) {
                    cameraDevice = p0
                    capReq = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    var surface = Surface(textureView.surfaceTexture)
                    capReq.addTarget(surface)

                    cameraDevice!!.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback() {
                        override fun onConfigured(p0: CameraCaptureSession) {
                            cameraCaptureSession = p0
                            cameraCaptureSession.setRepeatingRequest(capReq.build(), null, null)
                        }

                        override fun onConfigureFailed(p0: CameraCaptureSession) {

                        }
                    }, handler)
                }

                override fun onDisconnected(p0: CameraDevice) {
                    cameraDevice?.close()
                    cameraDevice = null
                }

                override fun onError(p0: CameraDevice, p1: Int) {
                    cameraDevice?.close()
                    cameraDevice = null
                }
            }, null)
        } catch (err: CameraAccessException) {
            //
        }
    }

    private fun getPermissions() {
        var permissionList = mutableListOf<String>()

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.CAMERA)
        }

        if (permissionList.size > 0) {
            ActivityCompat.requestPermissions(requireActivity(), permissionList.toTypedArray(), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                getPermissions()
            }
        }
    }

    private fun getCameraId(facing: Int): String {
        for (id in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(id)
            if (characteristics.get(CameraCharacteristics.LENS_FACING) == facing) {
                return id
            }
        }
        throw RuntimeException("Camera orientation does not exist.")
    }
}