package com.example.lab7.ui.screens.Characters.List

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.lab7.core.navigation.bars.LabBottomBar
import com.example.lab7.ui.screens.Characters.Classes.Character
import com.example.lab7.core.ui.commonComponents.ErrorScreen
import com.example.lab7.core.ui.commonComponents.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: CharacterListViewModel = viewModel(),
    onCharacterClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var characterToEdit by remember { mutableStateOf<Character?>(null) }

    BackHandler {
        (context as? Activity)?.finish()
    }

    val displayList = when (state.filter) {
        CharacterFilter.ALL -> state.data
        CharacterFilter.FAVORITES -> state.data.filter { it.isFavorite }
        CharacterFilter.NON_FAVORITES -> state.data.filter { !it.isFavorite }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Characters",
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Character")
            }
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
                    selected = state.filter == CharacterFilter.ALL,
                    onClick = { viewModel.onEvent(CharacterListEvent.SetFilter(CharacterFilter.ALL)) },
                    label = { Text("Todas") }
                )
                FilterChip(
                    selected = state.filter == CharacterFilter.FAVORITES,
                    onClick = { viewModel.onEvent(CharacterListEvent.SetFilter(CharacterFilter.FAVORITES)) },
                    label = { Text("Favoritas") }
                )
                FilterChip(
                    selected = state.filter == CharacterFilter.NON_FAVORITES,
                    onClick = { viewModel.onEvent(CharacterListEvent.SetFilter(CharacterFilter.NON_FAVORITES)) },
                    label = { Text("No Favoritas") }
                )
            }

            when {
                state.isLoading -> {
                    LoadingScreen(
                        message = "Cargando"
                    )
                }
                state.hasError -> {
                    ErrorScreen(
                        errorMessage = "Error al obtener listado de personajes.",
                        onRetry = { viewModel.onEvent(CharacterListEvent.LoadCharacters) }
                    )
                }
                displayList.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay ningún personaje en la lista",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(displayList) { character ->
                            CharacterItem(
                                character = character,
                                onClick = { onCharacterClick(character.id) },
                                onToggleFavorite = {
                                    viewModel.onEvent(
                                        CharacterListEvent.ToggleFavorite(
                                            character.id,
                                            character.isFavorite
                                        )
                                    )
                                },
                                onEdit = { characterToEdit = character },
                                onDelete = {
                                    viewModel.onEvent(CharacterListEvent.DeleteCharacter(character.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        CharacterFormDialog(
            title = "Add Character",
            initialCharacter = null,
            onDismiss = { showAddDialog = false },
            onSave = { name, status, species, gender, image ->
                viewModel.onEvent(CharacterListEvent.AddCharacter(name, status, species, gender, image))
                showAddDialog = false
            }
        )
    }

    characterToEdit?.let { char ->
        CharacterFormDialog(
            title = "Edit Character",
            initialCharacter = char,
            onDismiss = { characterToEdit = null },
            onSave = { name, status, species, gender, image ->
                viewModel.onEvent(CharacterListEvent.UpdateCharacter(char.id, name, status, species, gender, image))
                characterToEdit = null
            }
        )
    }
}

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = character.image,
                    contentDescription = character.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "${character.species} - ${character.status}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (character.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (character.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun CharacterFormDialog(
    title: String,
    initialCharacter: Character?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialCharacter?.name ?: "") }
    var status by remember { mutableStateOf(initialCharacter?.status ?: "Alive") }
    var species by remember { mutableStateOf(initialCharacter?.species ?: "Human") }
    var gender by remember { mutableStateOf(initialCharacter?.gender ?: "Male") }
    var image by remember { mutableStateOf(initialCharacter?.image ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
                OutlinedTextField(value = status, onValueChange = { status = it }, label = { Text("Status") }, singleLine = true)
                OutlinedTextField(value = species, onValueChange = { species = it }, label = { Text("Species") }, singleLine = true)
                OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, singleLine = true)
                OutlinedTextField(value = image, onValueChange = { image = it }, label = { Text("Image URL") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(name, status, species, gender, image)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
