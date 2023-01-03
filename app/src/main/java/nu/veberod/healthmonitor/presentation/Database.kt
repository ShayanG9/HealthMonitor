package nu.veberod.healthmonitor.presentation

import android.annotation.SuppressLint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Database{

    companion object {
        @SuppressLint("StaticFieldLeak")
        private val db = Firebase.firestore

        fun sendData(name: String, data: MutableList<Float>, time: String){
            //Firebase.database.setPersistenceEnabled(true)
            /** ----------------DEBUGGING---------------- */
            println("Current thread " + Thread.currentThread().name)
            /** ----------------DEBUGGING---------------- */

            var datapoint = hashMapOf<Any?, Any?>()
            val d1: Float; val d2: Float; val d3: Float

            if ("Acc" in name)
            {
                d1 = data[0]; d2 = data[1]; d3 = data[2]

                datapoint = hashMapOf(
                    "ax" to d1,
                    "ay" to d2,
                    "az" to d3
                )
            }
            else if ("Gyro" in name)
            {
                d1 = data[0]; d2 = data[1]; d3 = data[2]

                datapoint = hashMapOf(
                    "gx" to d1,
                    "gy" to d2,
                    "gz" to d3
                )
            }
            else if ("Heart" in name)
            {
                d1 = data[0]
                datapoint = hashMapOf(
                    "Heart Rate" to d1,
                )
            }
            else if ("Step" in name)
            {
                d1 = data[0]
                datapoint = hashMapOf(
                    "Step Count" to d1,
                )
            }

            db.collection(name).document(time)
                .set(datapoint)
                .addOnSuccessListener {
                    println("success!")
                }
                .addOnFailureListener {
                    println("failed!")
                }

        }
    }
}
