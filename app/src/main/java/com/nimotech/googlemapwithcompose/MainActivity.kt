package com.nimotech.googlemapwithcompose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.Polyline
import com.nimotech.googlemapwithcompose.googlemaps.rememberMapViewWithLifecycle
import com.nimotech.googlemapwithcompose.ui.theme.GoogleMapWithComposeTheme
import com.nimotech.googlemapwithcompose.ui.theme.greenColor
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoogleMapWithComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Google Map Demo", textAlign = TextAlign.Center,
                            modifier = Modifier.padding(innerPadding),
                            style = TextStyle(
                                fontSize = 20.sp, fontWeight = FontWeight.Bold,
                                color = greenColor
                            )
                        )
                        //googleMapScreen()

                        val context = LocalContext.current
                        mapUI(context)
                    }
                }
            }
        }
    }
}

@Composable
fun mapUI(context : Context) {
    val mapView = rememberMapViewWithLifecycle()
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        AndroidView({ mapView }) { mapView ->
            CoroutineScope(Dispatchers.Main).launch {
                mapView.getMapAsync { googleMap ->
                    googleMap.uiSettings.isZoomControlsEnabled = true
                    //---for adding marker----
                    getMapLocation(mapView,context)
                }
            }
        }
    }
}

fun getMapLocation(mapView: MapView, context: Context) {
    mapView.getMapAsync {
        val Delhi = LatLng(28.613939, 77.209023)
        val Gurgaon = LatLng(28.459497, 77.026638)
        val Chandigarh = LatLng(30.733315, 76.779419)

       val arrayList : ArrayList<LatLng?> = ArrayList()
        arrayList.add(Delhi)
        arrayList.add(Gurgaon)
        arrayList.add(Chandigarh)

        //--for adding multiple marker----
        for(i in arrayList.indices){
            it.addMarker(MarkerOptions().position(arrayList[i]!!).title("Marker"))
            it.animateCamera(CameraUpdateFactory.zoomTo(20.0f))
            it.moveCamera(CameraUpdateFactory.newLatLng(arrayList[i]!!))
        }
        //--for adding polyline----
        it.addPolyline(PolylineOptions().add(Delhi,Gurgaon,Chandigarh,Delhi)
            .width(5f).color(context.getColor(R.color.black))
            .geodesic(true)
        )
        it.moveCamera(CameraUpdateFactory.newLatLngZoom(Delhi,10f))
    }
}



