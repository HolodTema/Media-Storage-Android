package com.terabyte.mediastorage.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.terabyte.mediastorage.R
import com.terabyte.mediastorage.activity.ui.theme.MediaStorageTheme
import com.terabyte.mediastorage.ui.theme.Orange
import com.terabyte.mediastorage.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            viewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )[MainViewModel::class]
            val navController = rememberNavController()

            MediaStorageTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if(!viewModel.stateFailureRequest.value) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    MainContent(viewModel, navController, innerPadding)
                }
            }
        }
    }
}

@Composable
fun MainContent(viewModel: MainViewModel, navController: NavHostController, paddingValues: PaddingValues) {
    if(viewModel.stateFailureRequest.value) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Something went wrong.\nCheck your Internet connection."
            )
            Button(
                modifier = Modifier
                    .padding(top = 8.dp),
                onClick = {
                    viewModel.getUserInfo()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                )
            ) {
                Text(
                    text = "Try again",
                )
            }
        }
    }
    else {
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            NavHost(navController, startDestination = "account") {
                composable(Routes.Account.route) {
                    Account(viewModel)
                }
                composable(Routes.Photos.route) {
                    Photos(viewModel)
                }
                composable(Routes.Videos.route) {
                    Videos()
                }
            }

        }
    }

}


@Composable
fun Videos() {
}

@Composable
fun Photos(viewModel: MainViewModel) {
    Column() {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
        ) {
            Text(text = "Amount photos: ${viewModel.stateItems.value?.size ?: 0}")
            Text(text = "Memory usage: ")
        }
        if(viewModel.stateItems.value!=null) {
            LazyVerticalGrid(

                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
            ) {
                items(viewModel.stateItems.value!!) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(painter = painterResource(
                            id = R.drawable.ic_launcher_background),
                            contentDescription = "photo",
                            modifier = Modifier
                                .height(150.dp)
                                .width(150.dp)
                                .padding(16.dp)
                        )
                        Text(
                            text = it.filename,
                            modifier = Modifier
                                .padding(top = 4.dp)
                        )
                    }
                }
            }
        }
        else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No photos."
                )
            }
        }
    }
}

@Composable
fun Account(viewModel: MainViewModel) {
    
    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = "User",
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
            Button(
                onClick = {
                    viewModel.logout()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                )
            ) {
                Text(text = "Sign out")
            }
        }
        Text(
            text = "Email: user@example.com",
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp),
            fontSize = 18.sp
        )
    }


}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = Orange
    ) {
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
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                    disabledIconColor = Color.White,
                    disabledTextColor = Color.White
                ),
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
