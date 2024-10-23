package com.example.sensores

import android.annotation.SuppressLint
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var detalle: TextView
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private var existeSensorProximidad: Boolean = false
    private lateinit var listadoSensores: List<Sensor>
    private lateinit var list:Button
    private lateinit var magnet:Button
    private lateinit var proxi:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        detalle = findViewById(R.id.textView)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        list=findViewById(R.id.btnListado)
        magnet=findViewById(R.id.btnMagnetico)
        proxi=findViewById(R.id.btnProximidad)
        list.setOnClickListener { clickListado(it) }
        magnet.setOnClickListener { clickMangnetico(it) }
        proxi.setOnClickListener { clickProximidad(it) }

    }

    @SuppressLint("SetTextI18n")
    fun clickListado(view: View?){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        listadoSensores = sensorManager.getSensorList(Sensor.TYPE_ALL)

        detalle.text = "Lista de sensores del dispositivo"

        for (sensor in listadoSensores){
            detalle.text = "${detalle.text}\nNombre: ${sensor.name}\nVersión: ${sensor.version}"
        }
    }

    @SuppressLint("SetTextI18n")
    fun clickMangnetico(view: View?){
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            Toast.makeText(applicationContext, "El dispositivo tiene sensor magnético.",Toast.LENGTH_SHORT).show()

            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!

            detalle.setBackgroundColor(Color.GRAY)

            detalle.text = "Propiedades del sensor Magnétivo: \nNombre: ${sensor.name.toString()}" +
                    "\nVersión: ${sensor.version}\nFabricante: ${sensor.vendor.toString()}"
        }else {
            Toast.makeText(applicationContext, "El dispositivo no cuenta con sensor magnético.",Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    fun clickProximidad(view: View?){
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
            val proximidadSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            existeSensorProximidad = true
            detalle.text = "El dispositivo tiene sensor: ${proximidadSensor!!.name}"
            detalle.setBackgroundColor(Color.GREEN)
        }else{
            detalle.text = "No se cuenta con sensor de proximidad"
            existeSensorProximidad = false
        }
    }

    @SuppressLint("SetTextI18n")
    fun onSensorChanged(event: SensorEvent?){
        val valorCambio: Float

        if (existeSensorProximidad){
            valorCambio = event!!.values[0]
            if (valorCambio < 1.0){
                detalle.textSize = 30f
                detalle.setBackgroundColor(Color.BLUE)
                detalle.setTextColor(Color.WHITE)
                detalle.text = "\nCERCA ${valorCambio}"
            } else {
                detalle.textSize = 14f
                detalle.setBackgroundColor(Color.GREEN)
                detalle.setTextColor(Color.BLACK)
                detalle.text = "\nLEJOS ${valorCambio}"
            }
        } else {
            Toast.makeText(applicationContext, "Sin cambios", Toast.LENGTH_SHORT).show()
        }
    }

}