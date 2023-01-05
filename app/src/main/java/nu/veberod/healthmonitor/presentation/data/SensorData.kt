package nu.veberod.healthmonitor.presentation.data

data class SensorUnit(
    val sensor : Float = 0f
)

data class SensorData(
    val heartrate: SensorUnit = SensorUnit(),
    val steps: SensorUnit = SensorUnit(),
    val calories: SensorUnit = SensorUnit(),
) {
    fun getHeartrate() = heartrate.sensor
    fun getSteps() = steps.sensor
    fun getCalories() = steps.sensor
}