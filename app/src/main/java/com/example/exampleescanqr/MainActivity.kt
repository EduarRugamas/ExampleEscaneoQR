package com.example.exampleescanqr

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder

import android.view.SurfaceHolder.Callback
import android.view.SurfaceView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector


class MainActivity : AppCompatActivity() {

    private val MY_PERMISSION_CAMERA: Int = 100
    private var barcodeDetector : BarcodeDetector? = null
    private var cameraSource : CameraSource? = null
    private var cameraSurfaceView : SurfaceView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= 23){
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA))
                    Toast.makeText(this, "Esta app requiere permiso de camara", Toast.LENGTH_LONG).show()
                    requestPermissions(arrayOf(android.Manifest.permission.CAMERA), MY_PERMISSION_CAMERA)
            }
        }

            //texto con el resultado qr

            //vista de la camara
            cameraSurfaceView = findViewById(R.id.camera_view)

            //creacion de lector qr
            barcodeDetector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build()

            //creacion de camara
            cameraSource = CameraSource.Builder(this, barcodeDetector).build()

            //prepara el lector de qr
            cameraSurfaceView?.holder?.addCallback(object: Callback{
                override fun surfaceCreated(p0: SurfaceHolder) {
                    TODO("Not yet implemented")
                }

                override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
                    TODO("Not yet implemented")
                }

                override fun surfaceDestroyed(p0: SurfaceHolder) {
                    TODO("Not yet implemented")
                }

            })
        }

    
}