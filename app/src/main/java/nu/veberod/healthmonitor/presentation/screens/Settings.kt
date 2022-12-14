package nu.veberod.healthmonitor.presentation.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import nu.veberod.healthmonitor.presentation.Screen
import nu.veberod.healthmonitor.presentation.theme.*

@Composable
fun Settings(navController: NavController) {
    Column (
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Settings", modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(24.dp))
        
        // SETTINGS

        Button(
            onClick = {
                navController.navigate(Screen.Settings.route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = grays),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Option 1")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Settings.route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = grays),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Option 2")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Settings.route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = grays),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Option 3")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Overview.route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = grays),
            modifier = Modifier
                .fillMaxWidth()
        ) {
//            Icon(
//                imageVector = Icons.Rounded.ArrowBack,
//                contentDescription = ""
//            )
//            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Back")
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}