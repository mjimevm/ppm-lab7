package com.example.lab7.screens.Locations.Detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab7.screens.Characters.Detail.DetailRow
import com.example.lab7.screens.ErrorScreen
import com.example.lab7.screens.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    viewModel: LocationDetailViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Location details",
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
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
                    errorMessage = "Error al obtener detalle de ubicación.",
                    onRetry = { viewModel.loadLocation() }
                )
            }
            else -> {
                val location = state.data
                if (location != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(top = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = location.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 24.dp, bottom = 32.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 48.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DetailRow(label = "ID:", value = location.id.toString())
                            DetailRow(label = "Tipo:", value = location.type)
                            DetailRow(label = "Dimensión:", value = location.dimension)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Location not found")
                    }
                }
            }
        }
    }
}
