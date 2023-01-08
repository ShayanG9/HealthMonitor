package nu.veberod.healthmonitor.presentation.graphs

import android.app.Activity

import android.graphics.Insets.add
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface


import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.Insets.add
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import nu.veberod.healthmonitor.presentation.screens.HeatMapPreview
import java.nio.file.Files.size

import kotlin.math.roundToInt
import kotlin.random.Random





data class Point(val x: Float, val y: Float)

// List of points and min/max point values
var valuesG = mutableListOf<Point>()
var minXValue: Float = 0.0F
var maxXValue: Float = 0.0F
// find max and min value of Y, we will need that later
var minYValue: Float = 0.0F
var maxYValue: Float = 0.0F
var sensor = "Puls"
var unitX = "s"





// random get numbers dummy test
fun getNum(){

    /*
     //For dummy values ditch this when collecting real data values
    for (i in 1..100){
        values.add(Point(i-1f, Random.nextFloat()*100))
    }
    Random.nextFloat()*100
    */

    // use this instead
    /* Hi I tried to populate the values<point> list from the sensor
    reading event (MyService.kt) and it worked on the simulator, updates values in graph


    something like this in the "heart rate" reading*/
    valuesG.add(Point(valuesG.size.toFloat(), 0.toFloat()))



    //find max and min value of X, we will need that later
    minXValue = valuesG.minOf { it.x }
    maxXValue = valuesG.maxOf { it.x }
    // find max and min value of Y, we will need that later
    minYValue = valuesG.minOf { it.y }
    maxYValue = valuesG.maxOf { it.y }

}



//@Preview("graphs")
@Composable
fun ChartWithLabels() {
    getNum()
    Column(
        Modifier
            .background(Color.Black)
            .padding(25.dp)
            .border(width = 1.dp, color = Color.Black)
            .padding(5.dp)
            .width(IntrinsicSize.Min)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = maxYValue.roundToInt().toString(),color = Color.White)
                Text(text = minYValue.roundToInt().toString(),color = Color.White)
            }
            LineChart()
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(minXValue.roundToInt().toString(),color = Color.White)
            Text(sensor,color = Color.Gray)
            Text(maxXValue.roundToInt().toString()+unitX,color = Color.White)

        }
    }


}

}





@Composable
fun LineChart(modifier: Modifier = Modifier
    .size(110.dp, 110.dp)
    .fillMaxWidth()) {

        Box(modifier = modifier
        .drawBehind { // we use drawBehind() method to create canvas

            // map data points to pixel values, in canvas we think in pixels
            val pixelPoints = valuesG.map {
                // we use extension function to convert and scale initial values to pixels
                val x = it.x.mapValueToDifferentRange(
                    inMin = minXValue,
                    inMax = maxXValue,
                    outMin = 0f,
                    outMax = size.width
                )

                // same with y axis
                val y = it.y.mapValueToDifferentRange(
                    inMin = minYValue,
                    inMax = maxYValue,
                    outMin = size.height,
                    outMax = 0f
                )

                Point(x, y)
            }

            val path = Path() // prepare path to draw

            // in the loop below we fill our path
            pixelPoints.forEachIndexed { index, point ->
                if (index == 0) { // for the first point we just move drawing cursor to the position
                    path.moveTo(point.x, point.y)
                } else {
                    path.lineTo(point.x, point.y) // for rest of points we draw the line
                }
            }

            // and finally we draw the path
            drawPath(
                path,
                color = Color.Red,
                style = Stroke(width = 3f))

        })

}






// simple extension function that allows conversion between ranges
fun Float.mapValueToDifferentRange(
    inMin: Float,
    inMax: Float,
    outMin: Float,
    outMax: Float
) = (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin





