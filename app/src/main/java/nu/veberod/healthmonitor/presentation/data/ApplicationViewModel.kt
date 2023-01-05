package nu.veberod.healthmonitor.presentation.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ApplicationViewModel() : ViewModel() {

    var sensorsState = mutableStateOf(SensorData())

    fun updateSensors(a: Float, b:Float , c:Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = a,
            steps = b,
            calories = c
        )
    }

    fun updateHeartrate(newValue: Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = newValue,
            steps = sensorsState.value.steps,
            calories = sensorsState.value.calories
        )
    }

    fun updateSteps(newValue: Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = sensorsState.value.heartrate,
            steps = newValue,
            calories = sensorsState.value.calories
        )
    }

    fun updateCalories(newValue: Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = sensorsState.value.calories,
            steps = sensorsState.value.steps,
            calories = newValue
        )
    }
}