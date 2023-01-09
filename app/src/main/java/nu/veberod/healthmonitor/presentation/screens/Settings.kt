package nu.veberod.healthmonitor.presentation.screens

import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings.*
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.wear.compose.material.*
import nu.veberod.healthmonitor.R
import nu.veberod.healthmonitor.presentation.Screen
import nu.veberod.healthmonitor.presentation.data.SettingsData
import nu.veberod.healthmonitor.presentation.theme.*
import java.util.*

@Composable
fun Settings(navController: NavController) {
    val mContext = LocalContext.current
    val packageName = mContext.packageName
    val CONTACT_PERMISSION_CODE = 0

    val shareLocation = remember { mutableStateOf(SettingsData.shareHeatmapLocation) }

    val contactsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickContact()) {
        SettingsData.emergencyNumber = it
    }

    val keyboardLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Retrieve the location data from the Intent object
            val result = result.data?.getStringExtra("result_text") ?: ""
            val geocoder = Geocoder(mContext, Locale.getDefault())
            var addressList = geocoder.getFromLocationName(result, 1)

            if (addressList?.size!! > 0) {

                val location = addressList[0]
                SettingsData.chosenLocation = Uri.parse("geo:${location?.latitude},${location?.longitude}")
                Toast.makeText(mContext, "Vald plats: " + location?.locality + ", " + location?.countryCode, Toast.LENGTH_SHORT).show()
                println(SettingsData.chosenLocation.toString())

            } else {
                Toast.makeText(mContext, "Plats kunde inte hittas.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    Column (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {

        Text(text = "Inställningar", fontWeight = FontWeight.Bold)

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
                val perm = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                if (perm) {
                    contactsLauncher.launch()
                }
                else {
                    val permission = arrayOf(android.Manifest.permission.READ_CONTACTS)
                    ActivityCompat.requestPermissions(mContext as Activity, permission, CONTACT_PERMISSION_CODE)
                }
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
                Text(text = "Nödkontakt")
            }
        }

// REDUNDANT SINCE WE HAVE "INFORMATION" WHERE ALL PERMISSIONS ARE LOCATED
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Button(
//            onClick = {
//                val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                ActivityCompat.requestPermissions(mContext as Activity, permission, LOCATION_PERMISSION_CODE)
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF363636)),
//            shape = RoundedCornerShape(8.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .align(Alignment.CenterStart)) {
//                Spacer(modifier = Modifier.width(16.dp))
//                Icon(
//                    painter = painterResource(id = R.drawable.locationslash),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .size(18.dp)
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Text(text = "Sekretess")
//            }
//        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

                val intent = Intent("com.google.android.wearable.action.LAUNCH_KEYBOARD")
                keyboardLauncher.launch(intent)

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
                    painter = painterResource(id = R.drawable.locationadd),
                    contentDescription = "",
                    modifier = Modifier
                        .size(18.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Mitt Hem")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

                shareLocation.value = !shareLocation.value
                SettingsData.shareHeatmapLocation = shareLocation.value

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
                Switch(shareLocation)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Dela Plats")
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
                    tint = Color.White,
                    contentDescription = "",
                    modifier = Modifier
                        .size(18.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Tillbaka", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun Switch(checked: MutableState<Boolean>) {
    Switch(
        checked = checked.value,
        onCheckedChange = { checked.value = it }
    )
}