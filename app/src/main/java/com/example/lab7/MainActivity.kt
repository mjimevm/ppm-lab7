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
import com.example.lab7.navigation.CharacterDetailDestination
import com.example.lab7.navigation.CharacterGraph
import com.example.lab7.navigation.CharacterListDestination
import com.example.lab7.navigation.LocationDetailDestination
import com.example.lab7.navigation.LocationGraph
import com.example.lab7.navigation.LocationListDestination
import com.example.lab7.navigation.LoginDestination
import com.example.lab7.navigation.ProfileDestination
import com.example.lab7.screens.Characters.Detail.CharacterDetailScreen
import com.example.lab7.screens.Characters.List.CharacterListScreen
import com.example.lab7.screens.Locations.List.LocationListScreen
import com.example.lab7.screens.Locations.Detail.LocationDetailScreen
import com.example.lab7.screens.Login.LoginScreen
import com.example.lab7.screens.Profile.ProfileScreen
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
                        LoginScreen(
                            onStartClick = {
                                navController.navigate(CharacterGraph) {
                                    popUpTo(LoginDestination) { inclusive = true }
                                }
                            }
                        )
                    }

                    navigation<CharacterGraph>(startDestination = CharacterListDestination) {
                        composable<CharacterListDestination> {
                            CharacterListScreen(
                                navController = navController,
                                onCharacterClick = { id ->
                                    navController.navigate(CharacterDetailDestination(characterId = id))
                                }
                            )
                        }
                        composable<CharacterDetailDestination> {
                            CharacterDetailScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }

                    navigation<LocationGraph>(startDestination = LocationListDestination) {
                        composable<LocationListDestination> {
                            LocationListScreen(
                                navController = navController,
                                onLocationClick = { id ->
                                    navController.navigate(LocationDetailDestination(locationId = id))
                                }
                            )
                        }
                        composable<LocationDetailDestination> {
                            LocationDetailScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }

                    composable<ProfileDestination> {
                        ProfileScreen(
                            navController = navController,
                            onLogoutClick = {
                                navController.navigate(LoginDestination) {
                                    popUpTo(0) {
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

@Preview(name = "Login Screen", showBackground = true, uiMode = 32)
@Composable
fun LoginPreview() {
    Lab7Theme {
        LoginScreen(onStartClick = {})
    }
}

@Preview(name = "Characters List", showBackground = true, uiMode = 32)
@Composable
fun ListPreview() {
    Lab7Theme {
        CharacterListScreen(onCharacterClick = {})
    }
}

@Preview(name = "Character Detail", showBackground = true, uiMode = 32)
@Composable
fun DetailPreview() {
    Lab7Theme {
        CharacterDetailScreen(onBackClick = {})
    }
}

@Preview(name = "Location Screen", showBackground = true, uiMode = 32)
@Composable
fun LocationScreenPreview() {
    Lab7Theme {
        LocationListScreen(onLocationClick = {})
    }
}

@Preview(name = "Location Detail", showBackground = true, uiMode = 32)
@Composable
fun LocationDetailScreenPreview() {
    Lab7Theme {
        LocationDetailScreen(onBackClick = {})
    }
}

@Preview(name = "Profile Screen", showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Lab7Theme {
        ProfileScreen()
    }
}
