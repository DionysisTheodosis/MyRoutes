package com.dionysis.routes.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun NewRouteDialog(
    onRouteNameChange: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    onSave: () -> Unit,
    routeNameError: String,
    urlError: String,
    routeNameValue: String,
    urlValue: String,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NewRouteTitle(modifier)
                    NewRouteName(onRouteNameChange, routeNameError, routeNameValue)
                    NewRouteUrl(onUrlChange, urlError, urlValue)
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            onSave()
                            onDismiss()
                        }) {
                            Text("Save")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onDismiss) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewRouteTitle(modifier: Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = "Add Route",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
fun NewRouteName(onValueChange: (String) -> Unit, errorMessage: String, routeNameValue: String) {
    Column {
        TextField(
            value = routeNameValue,
            onValueChange = onValueChange,
            label = { Text("Enter Route Name") },
            isError = errorMessage.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun NewRouteUrl(onValueChange: (String) -> Unit, errorMessage: String, urlValue: String) {
    Column {
        TextField(
            value = urlValue,
            onValueChange = onValueChange,
            label = { Text("Enter Route GPX URL") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Uri
            ),
            isError = errorMessage.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
