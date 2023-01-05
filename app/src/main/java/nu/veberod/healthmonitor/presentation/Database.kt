package nu.veberod.healthmonitor.presentation

import android.annotation.SuppressLint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Database{

    companion object {
        @SuppressLint("StaticFieldLeak")
        private val db = Firebase.firestore

        fun sendData(name: String, data: MutableList<Float>, time: String){
            var datapoint = hashMapOf<Any?, Any?>()


            val d1: Float = data[0]
            datapoint = hashMapOf(
                name to d1,
            )

            db.collection(name).document(time).set(datapoint)


            // Removed this because of lag.
           /* .addOnSuccessListener {
                    println("success!")
                }
                .addOnFailureListener {
                    println("failed!")
                }
           */

        }
    }
}
