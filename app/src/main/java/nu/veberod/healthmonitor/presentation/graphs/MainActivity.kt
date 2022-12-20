package nu.veberod.healthmonitor.presentation.graphs
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*


import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random



class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            getNum()
        ChartWithLabels()
        }

}}

data class Point(val x: Float, val y: Float)

// List of points and min/max point values
var values = mutableListOf<Point>()
var minXValue: Float = 0.0F
var maxXValue: Float = 0.0F
// find max and min value of Y, we will need that later
var minYValue: Float = 0.0F
var maxYValue: Float = 0.0F
var sensor = "Puls"
var unitX = "s"





// random get numbers dummy test
fun getNum(){


    for (i in 1..100){
        values.add(Point(i-1f, Random.nextFloat()*100))
    }

    // find max and min value of X, we will need that later
    minXValue = values.minOf { it.x }
    maxXValue = values.maxOf { it.x }
    // find max and min value of Y, we will need that later
    minYValue = values.minOf { it.y }
    maxYValue = values.maxOf { it.y }

}



@Preview("graphs")
@Composable
fun ChartWithLabels() {
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
                Text(text = maxYValue.roundToInt().toString(),color = Color.Green)
                //Text(text = minYValue.roundToInt().toString(),color = Color.Green)
            }
            LineChart()
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(minXValue.roundToInt().toString(),color = Color.Green)
            Text(sensor,color = Color.Gray)
            Text(maxXValue.roundToInt().toString()+unitX,color = Color.Green)

        }
    }
}





@Composable
fun LineChart(modifier: Modifier = Modifier.size(110.dp, 110.dp).fillMaxWidth()) {

    //fun LineChart(modifier: Modifier = Modifier.size(120.dp, 120.dp)) {



    Box(modifier = modifier
        .drawBehind { // we use drawBehind() method to create canvas


            // map data points to pixel values, in canvas we think in pixels
            val pixelPoints = values.map {
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
                color = Color.Blue,
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



