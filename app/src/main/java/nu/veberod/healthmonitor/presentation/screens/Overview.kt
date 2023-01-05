package nu.veberod.healthmonitor.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import nu.veberod.healthmonitor.R
import nu.veberod.healthmonitor.presentation.MainActivity
import nu.veberod.healthmonitor.presentation.Screen
import nu.veberod.healthmonitor.presentation.data.SensorData
import nu.veberod.healthmonitor.presentation.graphs.values
import nu.veberod.healthmonitor.presentation.theme.*

@Composable
fun Overview(navController: NavController) {

//    var heartrate: Float by remember {
//        mutableStateOf(SensorData.hej)
//    }

    var heartrate = 0f
    var distance : Double = 3.5
    var calories : Int = 349



    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp, vertical = 20.dp)
    ) {

        Text(text = "Overview", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        // ----------
        // HEARTRATE
        // ----------

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painterResource(id = R.drawable.heartcircle),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "${heartrate} bpm", fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        // ----------
        // SOME OTHER INFO
        // ----------

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painterResource(id = R.drawable.pointer),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "$distance steg", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ----------
        // SOME OTHER INFO
        // ----------

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painterResource(id = R.drawable.flashcircle),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "$calories kcals", fontWeight = FontWeight.Bold)
        }

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
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
