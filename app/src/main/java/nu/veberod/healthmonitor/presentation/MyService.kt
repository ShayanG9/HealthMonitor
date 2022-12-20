package nu.veberod.healthmonitor.presentation

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.*
import java.text.SimpleDateFormat
import java.util.*

class MyService : Service(){

    //Sensor variables
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor; private lateinit var heartBeat: Sensor;
    private lateinit var heartRate: Sensor;     private lateinit var stepCounter: Sensor
    private lateinit var gyroScope: Sensor;     private lateinit var sensorName: String
    private var SensorData: MutableList<Float> = mutableListOf<Float>()

    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    private val currentDate = sdf.format(Date())
    //Thread variables
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    override fun onCreate() {
        /**
         * Start up the thread running the service. We also make it
         * background priority so CPU-intensive work will not disrupt our UI.
         */
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /** ----------------DEBUGGING---------------- */
        println("Current thread " + Thread.currentThread().name)
        /** ----------------DEBUGGING---------------- */

        serviceHandler?.initializeSensors()
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        /**
         * Not in use.
         */
        return null
    }

    // Handler that represents the class for the thread.
    private inner class ServiceHandler(looper: Looper) : Handler(looper), SensorEventListener{

        fun initializeSensors()
        {
            // Get a reference to the SensorManager
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

            // Get a list of available sensors
            val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)

            // Find the available sensors
            for (sensor in sensorList) {
                //Test Gyro and Acc
                if (sensor.type == Sensor.TYPE_GYROSCOPE) {
                    gyroScope = sensor
                }
                if (sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    accelerometer = sensor
                }
                /** --UNCOMMENT IF USING SMARTWATCH--
                if (sensor.type == Sensor.TYPE_HEART_BEAT) {
                heartBeat = sensor
                }
                if (sensor.type == Sensor.TYPE_HEART_RATE) {
                heartRate = sensor
                }
                if (sensor.type == Sensor.TYPE_STEP_COUNTER) {
                stepCounter = sensor
                }
                 */
            }
            registerListener()
        }

        fun registerListener(){
            // Register the SensorEventListener to receive updates from the sensors
            sensorManager.registerListener(this, gyroScope, SensorManager.SENSOR_DELAY_NORMAL, serviceHandler)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, serviceHandler)
            /** --UNCOMMENT IF USING SMARTWATCH--
             * sensorManager.registerListener(this, heartBeat, SensorManager.SENSOR_DELAY_NORMAL, serviceHandler)
             * sensorManager.registerListener(this, heartRate, SensorManager.SENSOR_DELAY_NORMAL, serviceHandler)
             * sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL, serviceHandler)
             */

        }

        override fun onSensorChanged(p0: SensorEvent?) {
            /** ----------------DEBUGGING----------------
             * println("Current thread " + Thread.currentThread().name)
             * ----------------DEBUGGING---------------- */

            if (p0 != null) {
                sensorName = p0.sensor.name
            }

            if ("Accelerometer" in sensorName)
            {
                SensorData.add(p0!!.values[0])
                SensorData.add(p0.values[1])
                SensorData.add(p0.values[2])
                saveSensorData("accelerometer", SensorData)
            }
            else if ("Gyroscope" in sensorName)
            {
                SensorData.add(p0!!.values[0])
                SensorData.add(p0.values[1])
                SensorData.add(p0.values[2])
                saveSensorData("gyroscope", SensorData)
            }
            /** UNCOMMENT IF ON SMARTWATCH
            else if ("Heart Beat" in sensorName)
            {
            println(sensorName)
            }
            else if ("Heart Rate" in sensorName)
            {
            println(sensorName)
            }
            else if ("Step Counter" in sensorName)
            {
            println(sensorName)
            }
             */
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

        fun saveSensorData(name: String, data: MutableList<Float>)
        {
            println(name + " x: " + data[0] + " y: " + data[1] + " z: " + data[2] + " " + currentDate)
            /**
             * Save the data in a text file.
             */
            //Clear the data.
            SensorData.clear()
        }

    }
}