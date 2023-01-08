package nu.veberod.healthmonitor.presentation.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ApplicationViewModel() : ViewModel() {

    var sensorsState = mutableStateOf(SensorData())

    fun updateSensors(a: Float, b:Float , c:Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = a,
            steps = b,
            calories = c,
            fall = sensorsState.value.fall
        )
    }

    fun updateHeartrate(newValue: Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = newValue,
            steps = sensorsState.value.steps,
            calories = sensorsState.value.calories,
            fall = sensorsState.value.fall
        )
    }

    fun updateSteps(newValue: Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = sensorsState.value.heartrate,
            steps = newValue,
            calories = sensorsState.value.calories,
            fall = sensorsState.value.fall
        )
    }

    fun updateCalories(newValue: Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = sensorsState.value.heartrate,
            steps = sensorsState.value.steps,
            calories = newValue,
            fall = sensorsState.value.fall
        )
    }
    fun updateFall(newValue: Boolean) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = sensorsState.value.heartrate,
            steps = sensorsState.value.steps,
            calories = sensorsState.value.calories,
            fall = newValue
        )
    }

}