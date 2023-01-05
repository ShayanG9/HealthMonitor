package nu.veberod.healthmonitor.presentation.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ApplicationViewModel() : ViewModel() {

    var sensorsState = mutableStateOf(SensorData())

    fun updateSensors(a: Float, b:Float , c:Float) {
        Log.d("VIEWMODEL", this.toString())

        val heartrate = SensorUnit(
            sensor = a
        )

        val steps = SensorUnit(
            sensor = b
        )

        val calories = SensorUnit(
            sensor = c
        )

        sensorsState.value = sensorsState.value.copy(
            heartrate = heartrate,
            steps = steps,
            calories = calories
        )
    }

}