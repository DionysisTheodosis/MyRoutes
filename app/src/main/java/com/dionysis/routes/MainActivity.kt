package com.dionysis.routes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dionysis.routes.presentation.components.MainScreen
import com.dionysis.routes.presentation.components.MapScreen
import com.dionysis.routes.presentation.viewmodels.MapViewModel
import com.dionysis.routes.presentation.viewmodels.RoutesViewModel
import com.dionysis.routes.routes.Routes
import com.dionysis.routes.ui.theme.RoutesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mapViewModel: MapViewModel by viewModels()
    private val routesViewModel: RoutesViewModel by viewModels()


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            RoutesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.MainScreen
                    ) {
                        composable<Routes.MainScreen> {
                            MainScreen(
                                modifier = Modifier,
                                viewModel = routesViewModel,
                                onNavigateToMap = { routeName, positions ->
                                    mapViewModel.setRouteData(routeName, positions)
                                    navController.navigate(Routes.MapScreen)
                                }
                            )
                        }
                        composable<Routes.MapScreen> {
                            MapScreen(
                                modifier = Modifier,
                                viewModel = mapViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

