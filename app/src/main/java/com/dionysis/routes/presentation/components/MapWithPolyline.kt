package com.dionysis.routes.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dionysis.routes.presentation.states.MapState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapWithPolyline(
    mapState: MapState,
    createLatLngBounds: (List<LatLng>) -> LatLngBounds,
    calculateAverageLatLng: (List<LatLng>) -> LatLng
) {
    val averagePosition = calculateAverageLatLng(mapState.polylinePositions)
    var isMarkerClicked by rememberSaveable {mutableStateOf(true)  }

    val markerState = remember { MarkerState(position = averagePosition) }


    val cameraPositionState = rememberCameraPositionState {CameraPositionState}


    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = mapState.permissionState),
            uiSettings = MapUiSettings(compassEnabled = true, zoomControlsEnabled = true, myLocationButtonEnabled = true)
        ) {

            if (mapState.polylinePositions.size >= 2) {
                Polyline(
                    points = mapState.polylinePositions,
                    startCap = ButtCap(),
                    endCap = ButtCap(),
                    color = Color.Red,
                    width = 5f
                )
                Marker(state = markerState, title = mapState.routeName, icon = null, onClick = {
                    isMarkerClicked = true
                    it.showInfoWindow()
                    true
                }
                )
            }


            if (mapState.dynamicPositions.size >= 2) {
                Log.d("DynamicPolyline", "Drawing dynamic polyline with ${mapState.dynamicPositions.size} points")
                Polyline(
                    points = mapState.dynamicPositions,
                    color = Color.Blue,
                    startCap = ButtCap(),
                    endCap = ButtCap(),
                    width = 5f
                )
            }
            if (isMarkerClicked) {
                MapEffect(mapState.polylinePositions) {
                    if (mapState.polylinePositions.isNotEmpty()) {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngBounds(
                                createLatLngBounds(mapState.polylinePositions),
                                30
                            ),
                            durationMs = 2000
                        )
                        isMarkerClicked = false
                    }
                }
            }
        }

    }
}