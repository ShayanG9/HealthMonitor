package nu.veberod.healthmonitor.presentation.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import nu.veberod.healthmonitor.R
import nu.veberod.healthmonitor.presentation.Screen
import nu.veberod.healthmonitor.presentation.theme.*


@Composable
fun Settings(navController: NavController) {
    val mContext = LocalContext.current
    val packageName = mContext.packageName

    Column (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {

        Text(text = "Settings", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))
        
        // SETTINGS

        Button(
            onClick = {
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    it.data = Uri.parse("package:$packageName")
                }

                startActivity(mContext, intent, null)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF363636)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.infocircle),
                    contentDescription = "",
                    modifier = Modifier
                        .size(18.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Information")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Settings.route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF363636)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.contact),
                    contentDescription = "",
                    modifier = Modifier
                        .size(18.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Contact")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Settings.route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF363636)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.locationslash),
                    contentDescription = "",
                    modifier = Modifier
                        .size(18.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Privacy")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Overview.route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = accent_dark),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.closecircle_light),
                    contentDescription = "",
                    modifier = Modifier
                        .size(18.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Back")
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun privacy(navController: NavController) {
    Text(text = "HEJ")
}