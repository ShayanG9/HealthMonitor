package nu.veberod.healthmonitor.presentation.data

import nu.veberod.healthmonitor.presentation.graphs.Point

data class SensorData(
    var heartrate: Float = 0f,
    var steps: Float = 0f,
    var calories: Float = 0f,
    var fall: Boolean = false,
    var points: MutableList<Point> = mutableListOf(Point(0.0f, 0.toFloat()))
)