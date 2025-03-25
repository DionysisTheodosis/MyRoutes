package com.dionysis.routes.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.dionysis.routes.domain.models.Position
import com.dionysis.routes.presentation.actions.RouteActions
import com.dionysis.routes.presentation.viewmodels.RoutesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    appTitle: String = "My Routes",
    viewModel: RoutesViewModel = hiltViewModel(),
    onNavigateToMap: (String, List<Position>) -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.initLoadRoutes()
    }

    val routeState by viewModel.routeState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    NewRouteDialog(
        onRouteNameChange = { viewModel.onRouteNameChange(it) },
        onUrlChange = { viewModel.onUrlChange(it) },
        onSave = {
            viewModel.onAction(
                RouteActions.SaveRoute(
                    routeName = routeState.routeName,
                    url = routeState.url
                )
            )
        },
        routeNameError = routeState.routeNameError,
        urlError = routeState.urlError,
        routeNameValue = routeState.routeName,
        urlValue = routeState.url,
        isVisible = routeState.isDialogVisible,
        onDismiss = {
            viewModel.onDialogClose()
        },
        modifier = modifier
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = appTitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onDialogOpen() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Route"
                )
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = modifier.padding(padding)
            ) {

                items(routeState.routes) { route ->
                    if(routeState.isLoading){
                        IndeterminateCircularIndicator(modifier)
                    }
                    MyRoutes(
                        modifier,
                        onNavigateToMap,
                        route
                    )
                }
            }
            routeState.errorMessage?.let { message ->
                LaunchedEffect(message) {
                    snackbarHostState.showSnackbar(message)
                    viewModel.clearErrorMessage()
                }
            }

            routeState.successMessage?.let {message ->
                LaunchedEffect(message) {
                    snackbarHostState.showSnackbar(message)
                    viewModel.clearSuccessMessage()
                    if(message == "Route saved successfully"){
                        onNavigateToMap(routeState.routeName, routeState.routes.last().positions)
                    }
                }
            }

        }
    )
}

