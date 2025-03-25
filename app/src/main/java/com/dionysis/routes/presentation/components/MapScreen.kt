package com.dionysis.routes.presentation.components

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dionysis.routes.presentation.viewmodels.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel()
) {
    val permissions = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                android.Manifest.permission.POST_NOTIFICATIONS,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            listOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    )


    LaunchedEffect(Unit) {
        permissions.launchMultiplePermissionRequest()
    }

    when {
        permissions.allPermissionsGranted -> {

            val mapState by viewModel.mapState.collectAsState()
            viewModel.setLocationPermision(true)


            LaunchedEffect(Unit) {
                viewModel.startLocationTracking()
            }


            Box(modifier = modifier.fillMaxSize()) {
                MapWithPolyline(
                    mapState = mapState,
                    createLatLngBounds = { points -> viewModel.createLatLngBounds(points) },
                    calculateAverageLatLng = { positions -> viewModel.calculateAverageLatLng(positions) }
                )
            }
        }

        permissions.shouldShowRationale -> {
            viewModel.setLocationPermision(false)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = { permissions.launchMultiplePermissionRequest() }) {
                    Text(text = "Give Permissions")
                }
            }
        }
    }
}