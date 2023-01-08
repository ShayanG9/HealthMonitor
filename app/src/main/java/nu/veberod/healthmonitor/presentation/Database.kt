package nu.veberod.healthmonitor.presentation
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase


class Database{
    companion object {

        fun sendHeatMap(timestamp: String, data: Float){
            val database = FirebaseDatabase.getInstance().reference
            val data_user = database.child("heatmap")
                .child("user")
                .child(androidID!!)

            data_user.child("timestamp").setValue(timestamp)
            data_user.child("location").setValue(LatLng(55.634944, 13.500889))
            data_user.child("steps").setValue(data)

        }

        fun readHeatMapTimestamp(): Task<DataSnapshot>{
            val database = FirebaseDatabase.getInstance().reference
            val data_user = database.child("heatmap")
                .child("user")
                .child(androidID!!).child("timestamp")

            return data_user.get()


        }

      fun sendSteps(timestamp: String, steps: Int){
            val database = FirebaseDatabase.getInstance().reference

            val data_user = database.child("data")
                .child("user")
                .child(androidID!!)
                .child("steps")
                .child("timestamp")
                .child(timestamp)

            data_user.setValue(steps)

        }


        fun sendHeartRate(timestamp: String, rate: Float){
            val database = FirebaseDatabase.getInstance().reference

            val data_user = database.child("data")
                .child("user")
                .child(androidID!!)
                .child("heartRate")
                .child("timestamp")
                .child(timestamp)

            data_user.setValue(rate)

        }
        
        fun readHeatMapData(): MutableList<Pair<LatLng, Int>> {

            val database = FirebaseDatabase.getInstance().reference
            val data_user = database.child("heatmap").child("user")

            val dat = data_user.get()

            while(!dat.isComplete);

            val retData: MutableList<Pair<LatLng, Int>> = mutableListOf()

            if(dat.isSuccessful){
                for (user in dat.result.children) {
                    val key = user.key
                    val value = user.value


                    val lat = user.child("location")
                        .child("latitude").value

                    val lng = user.child("location")
                        .child("longitude").value

                    val steps = user.child("steps").value

                    retData.add(Pair(LatLng(lat as Double, lng as Double), (steps as Long).toInt()))


                }

            }

            return retData

        }

        fun  addFallListener(listener: ChildEventListener){
            val database = FirebaseDatabase.getInstance().reference
            val data_user = database.child("fall")
                .child("user")
                .child(androidID!!)
                .child("timestamp")

            data_user.addChildEventListener(listener)
        }

        fun sendFall(timestamp: String){
            val database = FirebaseDatabase.getInstance().reference
            val data_user = database.child("fall")
                .child("user")
                .child(androidID!!)
                .child("timestamp")
                .child(timestamp).setValue("Fall Detected")

        }


    }
}
