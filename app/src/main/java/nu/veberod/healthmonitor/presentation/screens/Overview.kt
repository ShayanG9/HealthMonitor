package nu.veberod.healthmonitor.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import nu.veberod.healthmonitor.R
import nu.veberod.healthmonitor.presentation.Screen
import nu.veberod.healthmonitor.presentation.data.ApplicationViewModel
import nu.veberod.healthmonitor.presentation.data.SensorData
import nu.veberod.healthmonitor.presentation.data.Singleton
import nu.veberod.healthmonitor.presentation.theme.*

@Composable
fun Overview(navController: NavController, viewModel : ApplicationViewModel = Singleton.viewModel) {

    val sensorsState = remember {
        mutableStateOf( viewModel.sensorsState )
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 15.dp)
    ) {

        Text(text = "Översikt", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        sensorRow(value = "${sensorsState.value.value.heartrate.toInt()} bpm", icon = R.drawable.heartcircle)

        Spacer(modifier = Modifier.height(8.dp))

        sensorRow(value = "${sensorsState.value.value.steps.toInt()} steg", icon = R.drawable.pointer)

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Settings.route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = transparent),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painterResource(id = R.drawable.settings),
                contentDescription = "",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun sensorRow(value: String, icon: Int) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(.95f)
    ) {
        Image(
            painterResource(id = icon),
            contentDescription = "",
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 17.sp)
    }

}
