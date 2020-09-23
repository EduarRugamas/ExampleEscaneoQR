package com.example.exampleescanqr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.view.SurfaceView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val MY_PERMISSION_CAMERA: Int = 100
    private var barcodeDetector : BarcodeDetector? = null
    private var cameraSource : CameraSource? = null
    private var cameraSurfaceView : SurfaceView? = null
    private var qrResult: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= 23){
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                    Toast.makeText(this, "Esta app requiere permiso de camara", Toast.LENGTH_LONG).show()
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_PERMISSION_CAMERA)
            }
        }

            //texto con el resultado qr
                qrResult = findViewById(R.id.qr_result)
            //vista de la camara
            cameraSurfaceView = findViewById(R.id.camera_view)

            //creacion de lector qr
            barcodeDetector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build()

            //creacion de camara
            cameraSource = CameraSource.Builder(this, barcodeDetector).build()

            //prepara el lector de qr
            cameraSurfaceView?.holder?.addCallback(object: Callback{

                //verificar si existen los permisos dados
                override fun surfaceCreated(holder: SurfaceHolder) {
                    if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    {
                        try {
                            cameraSource?.start(cameraSurfaceView?.holder)
                        }catch (e: IOException){
                           Log.e("Error", e.message.toString())
                        }
                    }
                }

                override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    cameraSource?.stop()
                }

            })

        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detections<Barcode>?) {
                val barcodes = detections?.detectedItems

                if (barcodes!!.size() != 0){
                    qr_result.post( Runnable {
                            fun run () {
                                qr_result.text = (barcodes.valueAt(0).displayValue.toString())
                            }
                    })

                    barcodeDetector?.release()

                }
            }

        })


    }
}




