package com.terabyte.mediastorage.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.terabyte.mediastorage.INTENT_ITEM_MODEL
import com.terabyte.mediastorage.R
import com.terabyte.mediastorage.activity.ui.theme.MediaStorageTheme
import com.terabyte.mediastorage.model.ItemModel
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
                    MainContent(navController, innerPadding)
                }
            }
        }
    }


    @Composable
    fun MainContent(navController: NavHostController, paddingValues: PaddingValues) {
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
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = paddingValues.calculateBottomPadding())
            ) {
                NavHost(navController, startDestination = "account") {
                    composable(Routes.Account.route) {
                        Account()
                    }
                    composable(Routes.Photos.route) {
                        Photos()
                    }
                    composable(Routes.Uploading.route) {
                        Uploading()
                    }
                }

            }
        }

    }


    @Composable
    fun Account() {

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
    fun Photos() {
        Column() {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            ) {
                Text(text = "Amount photos: ${viewModel.stateAmountItems.value}")
                Text(text = "Memory usage: ${viewModel.stateMemoryUsage.value}")
            }
            if(viewModel.stateAmountItems.value!=0) {
                LazyVerticalGrid(

                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                ) {
                    items(viewModel.stateAmountItems.value) { n ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if(viewModel.stateItems.value!=null && viewModel.stateItems.value!!.size>n) {
                                val itemModel = viewModel.stateItems.value!![n]
                                Image(bitmap = itemModel.image,
                                    contentDescription = "photo",
                                    modifier = Modifier
                                        .height(150.dp)
                                        .width(150.dp)
                                        .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                                        .clickable {
                                            startPhotoInfoActivity(itemModel)
                                        }
                                )
                                Text(
                                    text = itemModel.filename,
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                )
                            }
                            else {
                                Box(
                                    modifier = Modifier
                                        .height(150.dp)
                                        .width(150.dp)
                                        .padding(16.dp)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = Orange,
                                        modifier = Modifier
                                            .width(40.dp)
                                            .height(40.dp)
                                    )
                                }
                            }


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
    fun Uploading() {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val buttonUpload = createRef()
            val lazyColumnUploadHistory = createRef()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(20) {
                    UploadingHistoryItem()
                }
            }
            FloatingActionButton(
                containerColor = Orange,
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .constrainAs(buttonUpload) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    tint = Color.White,
                    painter = painterResource(id = R.drawable.ic_show_password),
                    contentDescription = "upload"
                )
            }
        }
        Box(

        ) {

        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun UploadingHistoryItem() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "filename.png",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(end = 16.dp))
                Text("Uploaded 01.01.2024 09:35")
            }
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
                    title = "Uploading",
                    image = Icons.Filled.Call,
                    route = Routes.Uploading.route
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


    private fun startPhotoInfoActivity(itemModel: ItemModel) {
        val intent = Intent(this, PhotoInfoActivity::class.java)
        intent.putExtra(INTENT_ITEM_MODEL, itemModel)
        startActivity(intent)
    }
    data class BarItem(
        val title: String,
        val image: ImageVector,
        val route: String
    )

    sealed class Routes(val route: String) {
        object Account: Routes("account")
        object Photos: Routes("photos")
        object Uploading: Routes("uploading")
    }

}













