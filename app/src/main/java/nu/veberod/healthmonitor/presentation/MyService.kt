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

import android.provider.ContactsContract.Data
import android.util.Log
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nu.veberod.healthmonitor.presentation.data.Singleton
import nu.veberod.healthmonitor.presentation.database.AppDatabase
import nu.veberod.healthmonitor.presentation.database.LocalDatabase
import nu.veberod.healthmonitor.presentation.graphs.Point
import nu.veberod.healthmonitor.presentation.graphs.valuesG
import java.lang.Math.pow

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sqrt
import kotlin.random.Random

class MyService : Service(){

    //Database
    private lateinit var appDb: AppDatabase


    //Sensor variables
    var emulator: Boolean = false
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor;     private lateinit var gyroScope: Sensor
    private lateinit var heartRate: Sensor;     private lateinit var stepCounter: Sensor
    private lateinit var fallDetection: Sensor; private lateinit var calorieCounter: Sensor
    private lateinit var sensorName: String
    private var sensorData: MutableList<Float> = mutableListOf<Float>()


    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
    var currentDate: String = sdf.format(Date())
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

        // RESET AND CREATE DATABASE.

        appDb = AppDatabase.getDatabase(this)
        GlobalScope.launch(Dispatchers.IO){
            appDb.localDatabaseDao().deleteAll()
        }

        // INITIALIZE SENSORS.
        serviceHandler?.initializeSensors()

        //serviceHandler?.initializeEmulatorSensors()

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
            initializeEmulatorSensors()
            // Get a list of available sensors
            val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
            // Find the available sensors

            for (sensor in sensorList) {
                if (sensor.type == Sensor.TYPE_HEART_RATE) {
                    heartRate = sensor
                }
                if (sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    stepCounter = sensor
                }
                if ("FallDetection" in sensor.name) {
                    fallDetection = sensor
                }
                if("Samsung FallDetection Sensor" in sensor.name){
                    calorieCounter = sensor
                }
            }
            registerListener()
        }

        fun initializeEmulatorSensors()
        {
            // Get a reference to the SensorManager
            //sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

            // Get a list of available sensors
            val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
            // Find the available sensors

            for (sensor in sensorList) {

                if (sensor.type == Sensor.TYPE_GYROSCOPE) {
                    gyroScope = sensor
                }
                if (sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    //Update the graph in Graphs.kt
                    accelerometer = sensor
                }

            }

            sensorManager.registerListener(this, gyroScope, 5000000, serviceHandler)
            sensorManager.registerListener(this, accelerometer, 5000000, serviceHandler)


            emulator = false


        }

        fun registerListener(){
            // Register the SensorEventListener to receive updates from the sensors
            //sensorManager.registerListener(this, heartRate, 5000000, serviceHandler)
            //sensorManager.registerListener(this, stepCounter, 50000000, serviceHandler)

            //sensorManager.registerListener(this, fallDetection, 50000000, serviceHandler)
           // sensorManager.registerListener(this, calorieCounter, 50000000, serviceHandler)


        }



        override fun onSensorChanged(p0: SensorEvent?) {

            /** |----Name of the sensors----|
             * - LSM6DSO Accelerometer
             * - LSM6DSO Gyroscope
             * - Samsung HR Batch Sensor
             * - Samsung Step Counter
             * - AFE4500S ECG
             *
             * Changed the name of the sensors if using emulator.
             * - Goldfish Heart rate sensor
             * - Goldfish 3-axis Gyroscope
             * - Goldfish 3-axis Accelerometer
             */


            // Get the name of the current sensor.
            if (p0 != null) {
                sensorName = p0.sensor.name
            }

            if(!emulator)
            {
                if ("Samsung HR Batch Sensor" in sensorName)
                {
                    sensorData.add(p0!!.values[0])
                    println(sensorData[0])

                    //Update the graph in Graphs.kt
                    valuesG.add(Point(valuesG.size.toFloat(), sensorData[0]))

                    saveSensorData("Heart", sensorData)
                    Database.sendHeartRate(sdf.format(Date()), p0!!.values[0])

                    Singleton.viewModel.viewModelScope.launch {
                        Singleton.viewModel.updateHeartrate(p0!!.values[0])
                        Singleton.viewModel.updatePoints(p0!!.values[0])
                    }



                }
                else if ("Samsung Step Counter" in sensorName)
                {
                    sensorData.add(p0!!.values[0])

                    saveSensorData("Step", sensorData)
                    Database.sendSteps(sdf.format(Date()), p0!!.values[0].toInt())


                    Singleton.viewModel.viewModelScope.launch {
                        Singleton.viewModel.updateSteps(p0!!.values[0])

                    }

                }else if("Calories" in sensorName){
                    Singleton.viewModel.viewModelScope.launch {
                        Singleton.viewModel.updateCalories(p0!!.values[0])
                    }
                }

            }

            if ("Accelerometer" in sensorName)
            {
                sensorData.add(p0!!.values[0])
                sensorData.add(p0.values[1])
                sensorData.add(p0.values[2])
                detectFall(p0!!.values[0], p0.values[1], p0.values[2])
                saveSensorData("Acc", sensorData)

            }
            else if ("Gyroscope" in sensorName)
            {
                sensorData.add(p0!!.values[0])
                sensorData.add(p0.values[1])
                sensorData.add(p0.values[2])

                //saveSensorData("Gyro", sensorData)

            }


        }

        fun detectFall(ax: Float, ay: Float , az:Float){
            val norm = sqrt(pow(ax.toDouble(), 2.0) + pow(ay.toDouble() ,2.0) + pow(az.toDouble() ,2.0))

            valuesG.add(Point(valuesG.size.toFloat(), 3.0.toFloat()))
            if( norm > 25.0){
                Log.i("fall", norm.toString())
                Singleton.viewModel.viewModelScope.launch {
                    Singleton.viewModel.updateFall(true)
                }
                Database.sendFall(sdf.format(Date()))
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

        fun saveSensorData(name: String, data: MutableList<Float>)
        {
            println(data)
            currentDate = sdf.format(Date())

            //Sends the data to local and remote database.
            /** LOCAL DATABASE --> RESULT IN APP INSPECTION **/
            writeData(name, data)

            /** REMOTE FIREBASE **/

            //Database.sendData(name, data, currentDate)



            //Clear the data.
            sensorData.clear()
        }


        fun writeData(name: String, data: MutableList<Float>){

            val new_data = LocalDatabase(
                null,name, data[0]
            )
            GlobalScope.launch(Dispatchers.IO){
                appDb.localDatabaseDao().insert(new_data)
            }
        }

        fun readData(){

            lateinit var localDatabase: List<LocalDatabase>

            GlobalScope.launch {

                localDatabase = appDb.localDatabaseDao().getAll()
                println(localDatabase)

            }
        }

    }
}