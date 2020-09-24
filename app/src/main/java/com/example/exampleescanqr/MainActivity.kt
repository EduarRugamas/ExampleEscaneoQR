package com.example.exampleescanqr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.view.SurfaceView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val MY_PERMISSION_CAMERA: Int = 100
    private lateinit var detector : BarcodeDetector
    private lateinit var cameraSource : CameraSource





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
        setUpControl()

    }

    private fun setUpControl(){
        detector = BarcodeDetector.Builder(this).build()
        cameraSource = CameraSource.Builder(this, detector).setRequestedPreviewSize(1920, 1080).setRequestedFps(25f).setAutoFocusEnabled(true).build()

        camera_view.holder.addCallback(surfaceCallback)
        detector.setProcessor(processor)
    }

    private val surfaceCallback = object : Callback{
        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                try {
                    cameraSource.start(surfaceHolder)
                }catch (exception: Exception){
                    Toast.makeText(this@MainActivity, "Se necesitan permisos de camara", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            cameraSource.stop()
        }

    }

    private val processor = object : Detector.Processor<Barcode>{
        override fun release() {}

        override fun receiveDetections(detections: Detections<Barcode>?) {

            if (detections != null){
                val qrCodes: SparseArray<Barcode> = detections.detectedItems
                val code = qrCodes.valueAt(0)
                qr_result.text = code.displayValue.toString()
            }else{
                Toast.makeText(this@MainActivity, "Codigo qr no leido", Toast.LENGTH_SHORT).show()
            }


        }


    }
}




