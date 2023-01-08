package nu.veberod.healthmonitor.presentation.data

data class SensorData(
    var heartrate: Float = 0f,
    var steps: Float = 0f,
    var calories: Float = 0f,
    var fall: Boolean = false
)