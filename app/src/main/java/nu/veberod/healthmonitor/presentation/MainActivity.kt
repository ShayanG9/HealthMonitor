
package nu.veberod.healthmonitor.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.maps.android.heatmaps.HeatmapTileProvider
import nu.veberod.healthmonitor.R
import nu.veberod.healthmonitor.presentation.screens.HeatMap
import nu.veberod.healthmonitor.presentation.theme.HealthMonitorTheme
import kotlin.collections.ArrayList


class MainActivity :  ComponentActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val intent = Intent(this, MyService::class.java)
        startService(intent)


        setContent {
            WearApp()
        }
    }


}






@Composable
fun WearApp() {

    HealthMonitorTheme {
        var isVisible by remember {mutableStateOf(true)}
        Pager(isVisible, setVisibility = {isVisible = it})

    }


}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Pager(isVisible: Boolean, setVisibility: (Boolean) -> Unit){

    val pagerState = rememberPagerState()

    val pageIndicatorState: PageIndicatorState = remember {

        object : PageIndicatorState {
            override val pageOffset: Float
                get() = pagerState.currentPageOffset *-1
            override val selectedPage: Int
                get() = pagerState.pageCount - pagerState.currentPage -1
            override val pageCount: Int
                get() = pagerState.pageCount
        }

    }

    VerticalPager(count = 3, state= pagerState, userScrollEnabled = isVisible) { page ->
        // Our page content


        when(page){
            0-> Navigation(setVisibility)

            1-> null //TODO: Add Graph Page

            2->{
                HeatMapTab(setVisibility)
            }
        }

    }
    if(isVisible) {
        HorizontalPageIndicator(
            modifier = Modifier
                .rotate(-90.0f)
                .padding(4.dp),
            pageIndicatorState = pageIndicatorState
        )
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
            color = MaterialTheme.colors.primary,
            text = stringResource(R.string.heatmap_title)

        )

        val veberod = LatLng(55.634944, 13.500889)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(veberod, 11.8f)
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

        /*HeatMap(modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp))*/




    }

}
@Composable
fun HeatMapTile(){

    val latLngs :MutableList<LatLng> = ArrayList()
    latLngs.add(LatLng(55.634944, 13.500889))
    val prov = HeatmapTileProvider.Builder().data(latLngs).build()
    TileOverlay(tileProvider = prov)
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}

