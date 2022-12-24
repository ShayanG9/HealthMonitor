package nu.veberod.healthmonitor.presentation

import com.google.firebase.database.FirebaseDatabase


class Database{
    companion object {

        fun sendData(iemi: String){
            val database = FirebaseDatabase.getInstance().reference

            val data_user = database.child("data").child("users").child(iemi)
            data_user.child("ax").setValue(0)
            data_user.child("ay").setValue(0)
            data_user.child("az").setValue(0)
            data_user.child("eeg").setValue(0)
            data_user.child("pulse").setValue(0)
            data_user.child("beat").setValue(0)
        }
    }
}
