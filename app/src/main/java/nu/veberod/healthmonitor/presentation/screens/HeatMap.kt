package nu.veberod.healthmonitor.presentation.screens




import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import nu.veberod.healthmonitor.presentation.HeatMapTile


@Composable
fun HeatMap(navCon: NavController){
    Box{
        val veberod = LatLng(55.634944, 13.500889)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(veberod, 11.8f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
            properties = MapProperties(mapType = MapType.HYBRID),
            cameraPositionState = cameraPositionState,
        ){
            HeatMapTile()
        }

        Button(modifier = Modifier
            .align(Alignment.TopStart)
            .padding(22.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            onClick = { navCon.popBackStack()}) {
            Image(
                painterResource(id = nu.veberod.healthmonitor.R.drawable.closecircle_heatmap),
                contentDescription = "",
                modifier = Modifier.size(30.dp) )
        }

    }

}