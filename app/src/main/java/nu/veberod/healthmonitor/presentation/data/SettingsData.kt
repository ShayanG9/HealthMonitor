package nu.veberod.healthmonitor.presentation.data

import android.net.Uri

object SettingsData {
    var emergencyNumber: Uri?
        get() {
            return emergencyNumber
        }
        set(value) {
            emergencyNumber = value
        }

    var shareHeatmapLocation: Boolean = false
}
