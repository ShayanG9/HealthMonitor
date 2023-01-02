
package nu.veberod.healthmonitor.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.background


import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.wear.compose.material.*
import ca.hss.heatmaplib.HeatMap
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import nu.veberod.healthmonitor.R
import nu.veberod.healthmonitor.presentation.theme.HealthMonitorTheme
import java.util.*




class MainActivity : ComponentActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val intent = Intent(this, MyService::class.java)
        startService(intent)
        
        setContent {
            WearApp()
        }
    }

}





@OptIn(ExperimentalWearMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun WearApp() {

    HealthMonitorTheme {


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
                0-> Navigation()

                1-> null //TODO: Add Graph Page

                2->HeatMapTab()
            }

        }
        HorizontalPageIndicator( modifier = Modifier
            .rotate(-90.0f)
            .padding(4.dp),
            pageIndicatorState = pageIndicatorState
        )


    }


}


@Composable
fun HeatMapTab(){
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
        HeatMap(modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp))




    }


}

@Composable

fun HeatMap(modifier: Modifier){
    AndroidView(factory = {context -> LayoutInflater.from(context).inflate(R.layout.heatmap, null).apply{
        setHeatMap(view = this)
    } }, modifier =  modifier)
}

fun setHeatMap(view: View){
    view.findViewById<HeatMap>(R.id.heatmap).setMinimum(0.0)
    view.findViewById<HeatMap>(R.id.heatmap).setMaximum(100.0)
    val rand = Random()
    for (i in 0..19) {
        val point =
            HeatMap.DataPoint(rand.nextFloat(), rand.nextFloat(), rand.nextDouble() * 100.0)
        view.findViewById<HeatMap>(R.id.heatmap).addData(point)

    }

    view.setOnClickListener {
        //TODO: FullScreen the HeatMap
    }

}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}

