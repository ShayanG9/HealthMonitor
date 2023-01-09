package nu.veberod.healthmonitor.presentation.settings

import android.app.Activity
import android.content.Context
import com.google.android.gms.maps.model.LatLng

class SettingsSave {
    companion object{
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

        fun writeLocationShare(a:Activity, value: Boolean){
            val pref = a?.getPreferences(Context.MODE_PRIVATE)
            with(pref.edit()){
                putBoolean("locationShare", value)
                apply()
            }
        }
        fun readLocationShare(a:Activity): Boolean{
            val pref = a?.getPreferences(Context.MODE_PRIVATE)
            return pref.getBoolean("locationShare", false  )
        }

        fun writePhoneNumber(a: Activity, number: String){
            val pref = a?.getPreferences(Context.MODE_PRIVATE)
            with(pref.edit()){
                putString("number", number)
                apply()
            }
        }

        fun readLocationString(a: Activity): String{
            val pref = a?.getPreferences(Context.MODE_PRIVATE)
            return pref.getString("location", "Ingen vald")!!
        }

        fun writeLocationString(a: Activity, number: String){
            val pref = a?.getPreferences(Context.MODE_PRIVATE)
            with(pref.edit()){
                putString("location", number)
                apply()
            }
        }

        fun readPhoneNumber(a: Activity, number: String): String{
            val pref = a?.getPreferences(Context.MODE_PRIVATE)
            return pref.getString("number", "")!!
        }

        fun writeLatLng(a: Activity, latLng: LatLng){
            val pref = a?.getPreferences(Context.MODE_PRIVATE)
            with(pref.edit()){
                putLong("lat", latLng.latitude.toLong())
                putLong("lng", latLng.longitude.toLong())
                apply()
            }
        }

        fun readLatLng(a: Activity): LatLng{
            val pref = a?.getPreferences(Context.MODE_PRIVATE)
            return LatLng(pref.getLong("lat",0).toDouble(), pref.getLong("lng",0).toDouble())
        }

    }

}