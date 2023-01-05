package nu.veberod.healthmonitor.presentation.data

object SettingsData {
    var emergencyNumber: String
        get() {
            return emergencyNumber
        }
        set(value) {
            emergencyNumber = value
        }

    var shareHeatmapLocation: Boolean = false
}
