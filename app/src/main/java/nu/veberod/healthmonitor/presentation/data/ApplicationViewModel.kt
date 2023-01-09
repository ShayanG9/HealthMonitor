package nu.veberod.healthmonitor.presentation.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import nu.veberod.healthmonitor.presentation.graphs.Point
import nu.veberod.healthmonitor.presentation.graphs.valuesG

class ApplicationViewModel() : ViewModel() {

    var sensorsState = mutableStateOf(SensorData())

    fun updateSensors(a: Float, b:Float , c:Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = a,
            steps = b,
            calories = c,
            fall = sensorsState.value.fall,
            points = sensorsState.value.points
        )
    }

    fun updateHeartrate(newValue: Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = newValue,
            steps = sensorsState.value.steps,
            calories = sensorsState.value.calories,
            fall = sensorsState.value.fall,
            points = sensorsState.value.points
        )

    }

    fun updateSteps(newValue: Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = sensorsState.value.heartrate,
            steps = newValue,
            calories = sensorsState.value.calories,
            fall = sensorsState.value.fall,
            points = sensorsState.value.points
        )
    }

    fun updatePoints(newValue: Float){
        sensorsState.value.points.add(Point( sensorsState.value.points.size.toFloat(), newValue))
        sensorsState.value = sensorsState.value.copy(
            heartrate = sensorsState.value.heartrate,
            steps = sensorsState.value.steps,
            calories = sensorsState.value.calories,
            fall = sensorsState.value.fall,
            points = sensorsState.value.points
        )
    }

    fun updateCalories(newValue: Float) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = sensorsState.value.heartrate,
            steps = sensorsState.value.steps,
            calories = newValue,
            fall = sensorsState.value.fall,
            points = sensorsState.value.points
        )
    }
    fun updateFall(newValue: Boolean) {
        sensorsState.value = sensorsState.value.copy(
            heartrate = sensorsState.value.heartrate,
            steps = sensorsState.value.steps,
            calories = sensorsState.value.calories,
            fall = newValue,
            points = sensorsState.value.points
        )
    }

}