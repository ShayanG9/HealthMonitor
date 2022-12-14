package nu.veberod.healthmonitor.presentation.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.lifecycle.viewModelScope
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import kotlinx.coroutines.launch
import nu.veberod.healthmonitor.presentation.data.Singleton
import nu.veberod.healthmonitor.presentation.fall.sendSMS
//import nu.veberod.healthmonitor.presentation.func


@SuppressLint("MissingPermission")
@Composable
fun Fall(){

    var counter by remember { mutableStateOf("") }

    val context = LocalContext.current

    val v = context.getSystemService(Vibrator::class.java)



    val a = object : CountDownTimer(20000, 1000) {

        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.S)
        override fun onTick(millisUntilFinished: Long) {
            v.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
            counter =  (millisUntilFinished / 1000).toString()
        }

        override fun onFinish() {
            sendSMS(context, "test", "+460793182737")
            Singleton.viewModel.updateFall(false)
        }
    }.start()




    Alert(
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 24.dp, bottom = 52.dp),
        title = {
            Column{
                Text(
                    "A Hard fall was detected", modifier =  Modifier.padding(15.dp),color = Color.White,textAlign = TextAlign.Center
                )
            }

        },
        message = {
            Text(
                "Contacting Help in $counter seconds",color = Color.Red ,textAlign = TextAlign.Center
            )
        }

    ){
        item{
            Chip(
                label = { Text("STOP", color = Color.White) },
                onClick = {
                    Singleton.viewModel.viewModelScope.launch {
                        Singleton.viewModel.updateFall(false)
                    }
                    a.cancel()

                          },
                colors = ChipDefaults.primaryChipColors(backgroundColor = Color.Red),
            )

        }
    }



}