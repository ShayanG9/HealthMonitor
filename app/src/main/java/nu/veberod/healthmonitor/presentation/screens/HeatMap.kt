package nu.veberod.healthmonitor.presentation.screens




import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.maps.android.heatmaps.Gradient
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.heatmaps.WeightedLatLng
import nu.veberod.healthmonitor.R
import nu.veberod.healthmonitor.presentation.Database


@Composable
fun HeatMap(navCon: NavController){
    Box{
        val veberod = LatLng(55.634944, 13.500889)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(veberod, 12f)
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

@Composable
fun HeatMapTab(setVisibility: (Boolean) -> Unit){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "preview"){

        composable("preview"){
            setVisibility(true)
            HeatMapPreview(navController)
        }
        composable("fullscreen"){
            setVisibility(false)
            HeatMap(navController)
        }
    }



}

@Composable
fun HeatMapPreview(navCon : NavController){
    val titlePadding = 30.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Top


    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = titlePadding, bottom = 0.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.heatmap_title)

        )

        val veberod = LatLng(55.634944, 13.500889)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(veberod, 12f)
        }



        GoogleMap(
            modifier = Modifier
                .width(167.dp)
                .height(122.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp),
            uiSettings = MapUiSettings(zoomControlsEnabled = false,scrollGesturesEnabled = false, zoomGesturesEnabled = false),
            properties = MapProperties(mapType = MapType.HYBRID),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                navCon.navigate("fullscreen")
            }
        ){
            HeatMapTile()
        }


    }

}


@Composable
fun HeatMapTile(){
    val colors = intArrayOf(
        android.graphics.Color.argb(100, 100, 100, 255),// red
        android.graphics.Color.rgb(0, 200, 0)  // green

    )
    val startPoints = floatArrayOf(0.2f, 1f)



   val prov = HeatmapTileProvider.Builder().gradient(
        Gradient(colors, startPoints)
    ).weightedData(getMapTileData()).maxIntensity(1.00).build()
    TileOverlay(tileProvider = prov)
}


fun getMapTileData( ): MutableList<WeightedLatLng> {
    val data: List<Pair<LatLng, Int>>  = Database.readHeatMapData()

    val intensity = arrayListOf<Double>()

    for(i in data){
        intensity.add(i.second.toDouble())
    }

    val max = intensity.minOrNull()
    val min = intensity.maxOrNull()

    val finalList = mutableListOf<WeightedLatLng>()
    for(i in 0 until intensity.size){
        intensity[i] = (intensity[i] - min!!)/(max!! - min!!)

        finalList.add(WeightedLatLng(data[i].first, intensity[i]))
    }


    return finalList
}