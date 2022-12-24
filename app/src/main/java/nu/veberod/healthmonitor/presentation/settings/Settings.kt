package nu.veberod.healthmonitor.presentation.settings

import android.app.Activity
import android.content.Context

class Settings {
    fun writeSetting(a: Activity, key: String, value: Int){
        val pref = a?.getPreferences(Context.MODE_PRIVATE)
        with(pref.edit()){
            putInt(key, value)
            apply()
        }

    }

    fun readSetting(a: Activity, key:String): Int{
        val pref = a?.getPreferences(Context.MODE_PRIVATE)
        return pref.getInt(key, -1)
    }
}