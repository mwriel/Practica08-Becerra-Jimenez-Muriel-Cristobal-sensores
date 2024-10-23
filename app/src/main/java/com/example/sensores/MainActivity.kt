package com.example.sensores

import android.annotation.SuppressLint
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var detalle: TextView
    private lateinit var sensorManager: SensorManager
    private var existeSensorProximidad: Boolean = false
    private lateinit var listadoSensores: List<Sensor>
    private lateinit var list: Button
    private lateinit var magnet: Button
    private lateinit var proxi: Button
    private lateinit var rotation: Button
    private lateinit var acceleration: Button
    private lateinit var rotationSensor: Sensor
    private lateinit var linearAccelerationSensor: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        detalle = findViewById(R.id.textView)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        list = findViewById(R.id.btnListado)
        magnet = findViewById(R.id.btnMagnetico)
        proxi = findViewById(R.id.btnProximidad)
        rotation = findViewById(R.id.btnRotation)
        acceleration = findViewById(R.id.btnAcceleration)

        list.setOnClickListener { clickListado(it) }
        magnet.setOnClickListener { clickMangnetico(it) }
        proxi.setOnClickListener { clickProximidad(it) }
        rotation.setOnClickListener { clickRotation(it) }
        acceleration.setOnClickListener { clickAcceleration(it) }
    }

    @SuppressLint("SetTextI18n")
    fun clickListado(view: View?) {
        listadoSensores = sensorManager.getSensorList(Sensor.TYPE_ALL)

        detalle.text = "Lista de sensores del dispositivo"

        for (sensor in listadoSensores) {
            detalle.text = "${detalle.text}\nNombre: ${sensor.name}\nVersión: ${sensor.version}"
        }
    }

    @SuppressLint("SetTextI18n")
    fun clickMangnetico(view: View?) {
        val magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        if (magnetSensor != null) {
            Toast.makeText(applicationContext, "El dispositivo tiene sensor magnético.", Toast.LENGTH_SHORT).show()
            detalle.setBackgroundColor(Color.GRAY)
            detalle.text = "Propiedades del sensor Magnético: \nNombre: ${magnetSensor.name}\nVersión: ${magnetSensor.version}\nFabricante: ${magnetSensor.vendor}"
        } else {
            Toast.makeText(applicationContext, "El dispositivo no cuenta con sensor magnético.", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    fun clickProximidad(view: View?) {
        val proximidadSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if (proximidadSensor != null) {
            existeSensorProximidad = true
            detalle.text = "El dispositivo tiene sensor: ${proximidadSensor.name}"
            detalle.setBackgroundColor(Color.GREEN)
        } else {
            detalle.text = "No se cuenta con sensor de proximidad"
            existeSensorProximidad = false
        }
    }

    // Función para el sensor de rotación
    @SuppressLint("SetTextI18n")
    fun clickRotation(view: View?) {
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)!!
        if (rotationSensor != null) {
            sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL)
            Toast.makeText(applicationContext, "Sensor de rotación activado.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "El dispositivo no cuenta con sensor de rotación.", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para el sensor de aceleración lineal
    @SuppressLint("SetTextI18n")
    fun clickAcceleration(view: View?) {
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)!!
        if (linearAccelerationSensor != null) {
            sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL)
            Toast.makeText(applicationContext, "Sensor de aceleración lineal activado.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "El dispositivo no cuenta con sensor de aceleración lineal.", Toast.LENGTH_SHORT).show()
        }
    }

    // Manejo de los eventos de sensores
    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ROTATION_VECTOR -> {
                val rotationMatrix = FloatArray(9)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

                val orientation = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientation)

                val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat() // Rotación alrededor del eje Z
                val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat() // Rotación alrededor del eje X
                val roll = Math.toDegrees(orientation[2].toDouble()).toFloat() // Rotación alrededor del eje Y

                detalle.text = "Ángulos de rotación:\nAzimut: $azimuth°\nInclinación: $pitch°\nGiro: $roll°"
            }

            Sensor.TYPE_LINEAR_ACCELERATION -> {
                val accelerationX = event.values[0]
                val accelerationY = event.values[1]
                val accelerationZ = event.values[2]

                detalle.text = "Aceleración Lineal:\nX: $accelerationX\nY: $accelerationY\nZ: $accelerationZ"
                detalle.setBackgroundColor(if (accelerationX > 1 || accelerationY > 1 || accelerationZ > 1) Color.RED else Color.WHITE)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No se implementa
    }

    override fun onPause() {
        super.onPause()
        // Detenemos la escucha de los sensores cuando la actividad se pausa
        sensorManager.unregisterListener(this)
    }
}
