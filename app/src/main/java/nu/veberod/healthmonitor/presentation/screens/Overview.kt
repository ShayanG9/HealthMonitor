package nu.veberod.healthmonitor.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import nu.veberod.healthmonitor.presentation.Screen
import nu.veberod.healthmonitor.presentation.theme.*

@Composable
fun Overview(navController: NavController) {

    Column (
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // ----------
        // HEARTRATE
        // ----------

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = "",
                colorFilter = ColorFilter.tint(primary)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "Heartrate")
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        // ----------
        // SOME OTHER INFO
        // ----------

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                imageVector = Icons.Rounded.Face,
                contentDescription = "",
                colorFilter = ColorFilter.tint(primary)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "Mood")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ----------
        // SOME OTHER INFO
        // ----------

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                imageVector = Icons.Rounded.Info,
                contentDescription = "",
                colorFilter = ColorFilter.tint(primary)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "TBA")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Settings.route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = grays_darker),
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Image(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "",
                colorFilter = ColorFilter.tint(grays_lightest)
            )
        }
    }
}