
package nu.veberod.healthmonitor.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle

import android.os.Looper
import androidx.annotation.RequiresApi
import android.provider.Settings.Secure
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.FirebaseApp
import nu.veberod.healthmonitor.R
import nu.veberod.healthmonitor.presentation.Database.Companion.readHeatMapData
import nu.veberod.healthmonitor.presentation.data.Singleton
import nu.veberod.healthmonitor.presentation.screens.HeatMap
import nu.veberod.healthmonitor.presentation.screens.HeatMapTab
import nu.veberod.healthmonitor.presentation.graphs.ChartWithLabels
import nu.veberod.healthmonitor.presentation.screens.Fall
import nu.veberod.healthmonitor.presentation.theme.HealthMonitorTheme
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

var androidID: String? = null

class MainActivity :  ComponentActivity(){


    private val sdf = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Permission for the sensors.
        setPermission()
        init()

        setContent {
            WearApp()
        }
    }


    private fun init(){
        getAndroidID()

        val intent = Intent(this, MyService::class.java)
        startService(intent)
        FirebaseApp.initializeApp(this)

    }

    private fun getAndroidID(){
        androidID = Secure.getString(this.contentResolver, Secure.ANDROID_ID)
    }

    override fun onDestroy() {
        super.onDestroy()
        updateHeatMapData()
    }

    private fun setPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                1
            )
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BODY_SENSORS),
                1
            )

        }
    }

    private fun updateHeatMapData(){
        val data = Database.readHeatMapTimestamp()
        while(!data.isComplete);

        if(data.isSuccessful){

            var savedDate: Date = sdf.parse(data.result.value as String)
            val currentDate = Date()

            val elapsedTime = (currentDate.time - savedDate.time )/(1000*3600)
            Log.i("test", elapsedTime.toString())

            if( elapsedTime >  24){
                Database.sendHeatMap(sdf.format(currentDate), 100)
            }

        }else{
            Database.sendHeatMap(sdf.format(Date()), 100)

        }
    }

}






@Composable
fun WearApp() {

    HealthMonitorTheme {
        var isVisible by remember {mutableStateOf(true)}
        var isFall  =  Singleton.viewModel.sensorsState.value.fall
        if(!isFall){
        Pager(isVisible, setVisibility = {isVisible = it})
        }else{
            Fall()
        }

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

    VerticalPager(count = 3, state= pagerState) { page ->
        // Our page content


        when(page){
            0-> Navigation(setVisibility)

            1-> null//ChartWithLabels()

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




@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}

