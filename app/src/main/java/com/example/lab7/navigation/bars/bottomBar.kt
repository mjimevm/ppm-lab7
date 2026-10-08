package com.example.lab7.navigation.bars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lab7.navigation.CharacterGraph
import com.example.lab7.navigation.LocationGraph
import com.example.lab7.navigation.ProfileDestination

@Composable
fun LabBottomBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Group, contentDescription = "Characters") },
            label = { Text("Characters") },
            selected = currentRoute?.contains("CharacterGraph") == true || currentRoute?.contains("CharacterListDestination") == true || currentRoute?.contains("CharacterDetailDestination") == true,
            onClick = {
                navController.navigate(CharacterGraph) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Public, contentDescription = "Locations") },
            label = { Text("Locations") },
            selected = currentRoute?.contains("LocationGraph") == true || currentRoute?.contains("LocationListDestination") == true || currentRoute?.contains("LocationDetailDestination") == true,
            onClick = {
                navController.navigate(LocationGraph) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute?.contains("ProfileDestination") == true,
            onClick = {
                navController.navigate(ProfileDestination) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}
