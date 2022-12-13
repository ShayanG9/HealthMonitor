package nu.veberod.healthmonitor.presentation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

class SecondActivity : ComponentActivity(), SensorEventListener {
    //Sensor variables
    private lateinit var sensorManager: SensorManager
    private lateinit var deviceSensors: List<Sensor>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeSensors()
        setContent {
            MainContent2()
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        TODO("Not yet implemented")
        //Display the data on the watch each time the sensor gets a new value.
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }

    fun saveData(data: String) {
        //Save data on the device.

    }

    private fun initializeSensors()
    {
        //Initialize all required sensors: PULSE, HEART RATE and STEP COUNTER.
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_HEART_BEAT)
    }
}

@Composable
fun MainContent2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            text = "Sensor Data"
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview2() {
    MainContent2()
}