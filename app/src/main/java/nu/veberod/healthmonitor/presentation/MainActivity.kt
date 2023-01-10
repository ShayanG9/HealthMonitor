
package nu.veberod.healthmonitor.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle

import android.provider.Settings.Secure
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.FirebaseApp
import nu.veberod.healthmonitor.presentation.data.SettingsData
import nu.veberod.healthmonitor.presentation.data.Singleton
import nu.veberod.healthmonitor.presentation.screens.HeatMapTab
import nu.veberod.healthmonitor.presentation.graphs.ChartWithLabels
import nu.veberod.healthmonitor.presentation.screens.Fall
import nu.veberod.healthmonitor.presentation.settings.SettingsSave
import nu.veberod.healthmonitor.presentation.theme.HealthMonitorTheme
import java.text.SimpleDateFormat
import java.util.*

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
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
        if(SettingsSave.readLocationShare(this)) {
            updateHeatMapData()
        }
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

        if(data.isSuccessful && data.result.value != null){

            var savedDate: Date = sdf.parse(data.result.value as String)
            val currentDate = Date()

            val elapsedTime = (currentDate.time - savedDate.time )/(1000*3600)
            Log.i("test", elapsedTime.toString())

            if( elapsedTime >  24){
                //Use below if emulating
                //Database.sendHeatMap(sdf.format(currentDate), 100)
                Database.sendHeatMap(sdf.format(Date()), Singleton.viewModel.sensorsState.value.steps, this)
            }

        }else{
            //Use below if emulating
            //Database.sendHeatMap(sdf.format(Date()), 100)

            Database.sendHeatMap(sdf.format(Date()), kotlin.random.Random.nextInt(0,100).toFloat(), this)



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

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
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
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        VerticalPager(count = 3, state= pagerState, userScrollEnabled = isVisible) { page ->
            // Our page content


            when(page){
                0-> Navigation(setVisibility)

                1-> ChartWithLabels()

                2->{
                    HeatMapTab(setVisibility)
                }
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

