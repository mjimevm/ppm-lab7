package com.example.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.lab7.core.navigation.AppGraph
import com.example.lab7.core.navigation.CharacterDetailDestination
import com.example.lab7.core.navigation.CharacterGraph
import com.example.lab7.core.navigation.CharacterListDestination
import com.example.lab7.core.navigation.LocationDetailDestination
import com.example.lab7.core.navigation.LocationGraph
import com.example.lab7.core.navigation.LocationListDestination
import com.example.lab7.core.navigation.LoginDestination
import com.example.lab7.core.navigation.ProfileDestination
import com.example.lab7.ui.screens.Characters.Detail.CharacterDetailScreen
import com.example.lab7.ui.screens.Characters.List.CharacterListScreen
import com.example.lab7.ui.screens.Locations.Detail.LocationDetailScreen
import com.example.lab7.ui.screens.Locations.List.LocationListScreen
import com.example.lab7.ui.screens.Login.LoginScreen
import com.example.lab7.ui.screens.Profile.ProfileScreen
import com.example.lab7.ui.theme.Lab7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab7Theme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = LoginDestination
                ) {
                    composable<LoginDestination> {
                        _root_ide_package_.com.example.lab7.ui.screens.Login.LoginScreen(
                            onStartClick = {
                                navController.navigate(AppGraph) {
                                    popUpTo(LoginDestination) { inclusive = true }
                                }
                            }
                        )
                    }

                    navigation<AppGraph>(startDestination = CharacterGraph) {
                        navigation<CharacterGraph>(startDestination = CharacterListDestination) {
                            composable<CharacterListDestination> {
                                _root_ide_package_.com.example.lab7.ui.screens.Characters.List.CharacterListScreen(
                                    navController = navController,
                                    onCharacterClick = { id ->
                                        navController.navigate(
                                            CharacterDetailDestination(
                                                characterId = id
                                            )
                                        )
                                    }
                                )
                            }
                            composable<CharacterDetailDestination> {
                                _root_ide_package_.com.example.lab7.ui.screens.Characters.Detail.CharacterDetailScreen(
                                    onBackClick = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }

                        navigation<LocationGraph>(startDestination = LocationListDestination) {
                            composable<LocationListDestination> {
                                _root_ide_package_.com.example.lab7.ui.screens.Locations.List.LocationListScreen(
                                    navController = navController,
                                    onLocationClick = { id ->
                                        navController.navigate(LocationDetailDestination(locationId = id))
                                    }
                                )
                            }
                            composable<LocationDetailDestination> {
                                _root_ide_package_.com.example.lab7.ui.screens.Locations.Detail.LocationDetailScreen(
                                    onBackClick = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }

                        composable<ProfileDestination> {
                            _root_ide_package_.com.example.lab7.ui.screens.Profile.ProfileScreen(
                                navController = navController,
                                onLogoutClick = {
                                    navController.navigate(LoginDestination) {
                                        popUpTo<AppGraph> {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Login Screen", showBackground = true, uiMode = 32)
@Composable
fun LoginPreview() {
    Lab7Theme {
        _root_ide_package_.com.example.lab7.ui.screens.Login.LoginScreen(onStartClick = {})
    }
}

@Preview(name = "Characters List", showBackground = true, uiMode = 32)
@Composable
fun ListPreview() {
    Lab7Theme {
        _root_ide_package_.com.example.lab7.ui.screens.Characters.List.CharacterListScreen(
            onCharacterClick = {})
    }
}

@Preview(name = "Character Detail", showBackground = true, uiMode = 32)
@Composable
fun DetailPreview() {
    Lab7Theme {
        _root_ide_package_.com.example.lab7.ui.screens.Characters.Detail.CharacterDetailScreen(
            onBackClick = {})
    }
}

@Preview(name = "Location Screen", showBackground = true, uiMode = 32)
@Composable
fun LocationScreenPreview() {
    Lab7Theme {
        _root_ide_package_.com.example.lab7.ui.screens.Locations.List.LocationListScreen(
            onLocationClick = {})
    }
}

@Preview(name = "Location Detail", showBackground = true, uiMode = 32)
@Composable
fun LocationDetailScreenPreview() {
    Lab7Theme {
        _root_ide_package_.com.example.lab7.ui.screens.Locations.Detail.LocationDetailScreen(
            onBackClick = {})
    }
}

@Preview(name = "Profile Screen", showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Lab7Theme {
        _root_ide_package_.com.example.lab7.ui.screens.Profile.ProfileScreen()
    }
}
