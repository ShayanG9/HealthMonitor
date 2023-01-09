package nu.veberod.healthmonitor.presentation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.RemoteInput
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender
import com.google.android.gms.maps.model.LatLng
import nu.veberod.healthmonitor.R
import nu.veberod.healthmonitor.presentation.Screen
import nu.veberod.healthmonitor.presentation.data.SettingsData
import nu.veberod.healthmonitor.presentation.settings.SettingsSave
import nu.veberod.healthmonitor.presentation.theme.link
import java.util.*


@SuppressLint("Range")
@Composable
fun Settings(navController: NavController) {
    val mContext = LocalContext.current
    val packageName = mContext.packageName

    val CONTACT_PERMISSION_CODE = 0

    val shareLocation = remember { mutableStateOf(SettingsData.shareHeatmapLocation) }
    val currentLocationString = remember { mutableStateOf(SettingsData.chosenLocationString) }

    val contactsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickContact()) {
            SettingsData.emergencyNumber = it

            val c = mContext.contentResolver.query(it!!, null, null,null)
            if (c!!.moveToFirst()) {
                val id: String =
                    c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val hasPhone: String =
                    c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                if (hasPhone.equals("1", ignoreCase = true)) {
                    val phones = mContext.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null
                    )
                    phones!!.moveToFirst()
                    val contactNumber: String =
                        phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    SettingsSave.writePhoneNumber(mContext as Activity, contactNumber)

                }
            }
        }

    val keyboardLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                val results: Bundle = RemoteInput.getResultsFromIntent(data)
                val location: CharSequence? = results.getCharSequence("address")

                val geocoder = Geocoder(mContext, Locale.getDefault())
                var addressList = geocoder.getFromLocationName(location.toString(), 5)

                // Return if no addresses were found
                if (addressList.isNullOrEmpty() || location.isNullOrEmpty()) {
                    Toast.makeText(mContext, "Kunde inte hitta den platsen.", Toast.LENGTH_SHORT).show()
                    return@let
                }

                val availableAddresses = addressList.map { it.getAddressLine(0) }.filter { !it.isNullOrEmpty() }.toTypedArray()
                val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
                builder.setTitle("Välj din address")
                builder.setItems(availableAddresses, DialogInterface.OnClickListener { dialog, which ->
                    currentLocationString.value = availableAddresses[which]
                    SettingsData.chosenLocationString = availableAddresses[which]
                    SettingsData.chosenLocation = "geo:${addressList[which].latitude},${addressList[which].longitude}".toUri()
                    SettingsSave.writeLatLng(mContext as Activity, LatLng(addressList[which].latitude, addressList[which].longitude))
                    SettingsSave.writeLocationString(mContext , availableAddresses[which])
                    println(SettingsData.chosenLocation.toString())
                })
                builder.show()
            }
        }


    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 20.dp)
    ) {

        Text(text = "Inställningar", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // ----------------------------
        // OPEN APPLICATION INFORMATION
        // ----------------------------

        settingsRow(
            icon = R.drawable.infocircle,
            label = "Information"
        ) {
            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                it.data = Uri.parse("package:${mContext.packageName}")
            }

            startActivity(mContext, intent, null)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ----------------------------
        // CHOOSE EMERGENCY CONTACT
        // ----------------------------

        settingsRow(
            icon = R.drawable.contact,
            label = "Nödkontakt"
        ) {
            val perm = ContextCompat.checkSelfPermission(
                mContext,
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
            if (perm) {
                contactsLauncher.launch()
            } else {
                val permission = arrayOf(android.Manifest.permission.READ_CONTACTS)
                ActivityCompat.requestPermissions(
                    mContext as Activity,
                    permission,
                    CONTACT_PERMISSION_CODE
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ----------------------------
        // CHOOSE HOME LOCATION
        // ----------------------------

        settingsRow(
            icon = R.drawable.locationadd,
            label = "Address",
            value = if (currentLocationString.value != null) currentLocationString.value else SettingsSave.readLocationString(mContext as Activity)


        ) {
            val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent()
            val remoteInputs: List<RemoteInput> = listOf(
                RemoteInput.Builder("address")
                    .setLabel("Hur vill du ange din address?")
                    .wearableExtender {
                        setEmojisAllowed(false)
                        setInputActionType(EditorInfo.IME_ACTION_DONE)
                    }.build()
            )

            RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)

            keyboardLauncher.launch(intent)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ----------------------------
        // TOGGLE LOCATION SHARE
        // ----------------------------
        shareLocation.value = SettingsSave.readLocationShare(mContext as Activity)
        settingsRow(
            icon = if (shareLocation.value) R.drawable.location else R.drawable.locationslash,
            label = "Platsdelning",
            value = if (shareLocation.value) "Delar" else "Delar inte"
        ) {
            SettingsSave.writeLocationShare(mContext as Activity,!shareLocation.value )
            shareLocation.value =  !shareLocation.value
            SettingsData.shareHeatmapLocation = shareLocation.value

        }

        Spacer(modifier = Modifier.height(6.dp))

        // ----------------------------
        // GO BACK
        // ----------------------------

        settingsRow(
            icon = R.drawable.closecircle_light,
            label = "Tillbaka"
        ) {

            navController.navigate(Screen.Overview.route)

        }

        Spacer(modifier = Modifier.height(6.dp))

    }
}

@Composable

fun settingsRow(icon : Int, label : String, value : String? = null, onClick : () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(15.dp, 5.dp)
                .align(Alignment.CenterStart)
        ) {
            Icon(painter = painterResource(id = icon), contentDescription = "", Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = label)
                if (value != null) Text(text = value, color = link, fontWeight = FontWeight.Medium, overflow = TextOverflow.Ellipsis, maxLines = 1)
            }
        }
    }
}

private fun showSettings(mContext : Context) {
    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        it.data = Uri.parse("package:${mContext.packageName}")
    }

    startActivity(mContext, intent, null)

}