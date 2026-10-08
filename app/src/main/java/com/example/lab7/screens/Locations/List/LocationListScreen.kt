package com.example.lab7.screens.Locations.List

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lab7.navigation.bars.LabBottomBar
import com.example.lab7.screens.ErrorScreen
import com.example.lab7.screens.LoadingScreen
import com.example.lab7.screens.Locations.Classes.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: LocationListViewModel = viewModel(),
    onLocationClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

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
        when {
            state.isLoading -> {
                LoadingScreen(
                    modifier = Modifier.padding(innerPadding),
                    message = "Cargando",
                    onClick = { viewModel.setError() }
                )
            }
            state.hasError -> {
                ErrorScreen(
                    modifier = Modifier.padding(innerPadding),
                    errorMessage = "Error al obtener listado de ubicaciones.",
                    onRetry = { viewModel.loadLocations() }
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(state.data) { location ->
                        LocationItem(
                            location = location,
                            onClick = { onLocationClick(location.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationItem(
    location: Location,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
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
}
