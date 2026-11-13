package com.example.lab7.ui.screens.Locations.List

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lab7.core.navigation.bars.LabBottomBar
import com.example.lab7.core.ui.commonComponents.ErrorScreen
import com.example.lab7.core.ui.commonComponents.LoadingScreen
import com.example.lab7.ui.screens.Locations.Classes.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: LocationListViewModel = viewModel(),
    onLocationClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    BackHandler {
        (context as? Activity)?.finish()
    }

    val displayList = when (state.filter) {
        LocationFilter.ALL -> state.data
        LocationFilter.FAVORITES -> state.data.filter { it.isFavorite }
        LocationFilter.NON_FAVORITES -> state.data.filter { !it.isFavorite }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Locations",
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            LabBottomBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.filter == LocationFilter.ALL,
                    onClick = { viewModel.onEvent(LocationListEvent.SetFilter(LocationFilter.ALL)) },
                    label = { Text("Todas") }
                )
                FilterChip(
                    selected = state.filter == LocationFilter.FAVORITES,
                    onClick = { viewModel.onEvent(LocationListEvent.SetFilter(LocationFilter.FAVORITES)) },
                    label = { Text("Favoritas") }
                )
                FilterChip(
                    selected = state.filter == LocationFilter.NON_FAVORITES,
                    onClick = { viewModel.onEvent(LocationListEvent.SetFilter(LocationFilter.NON_FAVORITES)) },
                    label = { Text("No Favoritas") }
                )
            }

            when {
                state.isLoading -> {
                    LoadingScreen(
                        message = "Cargando",
                        onClick = { viewModel.setError() }
                    )
                }
                state.hasError -> {
                    ErrorScreen(
                        errorMessage = "Error al obtener listado de ubicaciones.",
                        onRetry = { viewModel.loadLocations() }
                    )
                }
                displayList.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay elementos a mostrar",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(displayList) { location ->
                            LocationItem(
                                location = location,
                                onClick = { onLocationClick(location.id) },
                                onToggleFavorite = {
                                    viewModel.onEvent(
                                        LocationListEvent.ToggleFavorite(
                                            location.id,
                                            location.isFavorite
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationItem(
    location: Location,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = location.type,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (location.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (location.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
