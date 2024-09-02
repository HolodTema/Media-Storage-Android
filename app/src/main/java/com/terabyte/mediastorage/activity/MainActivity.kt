package com.terabyte.mediastorage.activity

import android.accounts.Account
import android.os.Bundle
import android.provider.Contacts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.terabyte.mediastorage.activity.ui.theme.MediaStorageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            MediaStorageTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(navController)
                    }
                ) { innerPadding ->
                    MainContent(navController)
                }
            }
        }
    }
}


@Composable
fun MainContent(navController: NavHostController) {
    Column() {
        NavHost(navController, startDestination = "account") {
            composable(Routes.Account.route) {
                Account()
            }
            composable(Routes.Photos.route) {
                Photos()
            }
            composable(Routes.Videos.route) {
                Videos()
            }
        }

    }

}

@Composable
fun Videos() {
}

@Composable
fun Photos() {
}

@Composable
fun Account() {
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        val navBarItems = listOf(
            BarItem(
                title = "Account",
                image = Icons.Filled.Face,
                route = Routes.Account.route
            ),
            BarItem(
                title = "Photos",
                image = Icons.Filled.Build,
                route = Routes.Photos.route
            ),
            BarItem(
                title = "Videos",
                image = Icons.Filled.Call,
                route = Routes.Videos.route
            )
        )

        navBarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {saveState = true}
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.image,
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}

data class BarItem(
    val title: String,
    val image: ImageVector,
    val route: String
)

sealed class Routes(val route: String) {
    object Account: Routes("account")
    object Photos: Routes("photos")
    object Videos: Routes("videos")
}
