package com.dionysis.routes.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dionysis.routes.domain.models.Position
import com.dionysis.routes.domain.models.Route

@Composable
fun MyRoutes(
    modifier: Modifier = Modifier,
    onNavigateToMap: (String, List<Position>) -> Unit,
    route: Route
) {
    RouteCard(modifier, route,onNavigateToMap)

}

@Composable
fun RouteCard(
    modifier: Modifier,
    route: Route,
    onNavigateToMap: (String, List<Position>) -> Unit
) {
    Card(
        modifier = Modifier.padding(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 24.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Route Name:",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = route.name,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onNavigateToMap(route.name, route.positions)
                    }
                ) {
                    Text(text = "Open In Map")
                }
            }
        }
    }
}


