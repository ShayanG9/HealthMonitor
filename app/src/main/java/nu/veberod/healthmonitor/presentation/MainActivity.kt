
package nu.veberod.healthmonitor.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.maps.android.compose.*
import nu.veberod.healthmonitor.presentation.screens.HeatMapTab
import nu.veberod.healthmonitor.presentation.graphs.ChartWithLabels
import nu.veberod.healthmonitor.presentation.theme.HealthMonitorTheme


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

            1-> ChartWithLabels()

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

