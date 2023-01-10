package nu.veberod.healthmonitor.presentation.fall

import android.app.appsearch.StorageInfo
import android.content.Context
import android.telephony.SmsManager


fun sendSMS(context: Context, msg: String, phoneNo: String){
    val sms_manage = context.getSystemService(SmsManager::class.java)
    sms_manage?.sendTextMessage(phoneNo,null, msg, null, null)
}
