package com.terabyte.mediastorage.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.terabyte.mediastorage.INTENT_DELETED_ITEM_BYTES_SIZE
import com.terabyte.mediastorage.INTENT_DELETED_ITEM_ID
import com.terabyte.mediastorage.R
import com.terabyte.mediastorage.activity.room.UploadingHistoryItem
import com.terabyte.mediastorage.activity.ui.theme.MediaStorageTheme
import com.terabyte.mediastorage.contract.PickImageActivityResultContract
import com.terabyte.mediastorage.model.ItemModel
import com.terabyte.mediastorage.ui.theme.Orange
import com.terabyte.mediastorage.util.ActivityCommunicationManager
import com.terabyte.mediastorage.util.UserManager
import com.terabyte.mediastorage.viewmodel.MainViewModel


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    private val pickImageLauncher =
        registerForActivityResult(PickImageActivityResultContract()) { uri ->
            if (uri != null) {
                viewModel.uploadImage(
                    uri,
                    successListener = {
                        // TODO: update itemmodel list and memory usage and items amount
                    },
                    noBitmapListener = {
                        toastNoBitmap()
                    }
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            viewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )[MainViewModel::class]

            checkIntentExtras()
            val navController = rememberNavController()

            MediaStorageTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (!viewModel.stateFailureRequest.value) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    MainContent(navController, innerPadding)
                }
            }
        }
    }

    private fun checkIntentExtras() {
        val hasIntent = intent != null && intent.extras != null
        if (hasIntent) {
            val hasExtraId = intent.extras!!.containsKey(INTENT_DELETED_ITEM_ID)
            val hasExtraBytesSize = intent.extras!!.containsKey(INTENT_DELETED_ITEM_BYTES_SIZE)
            if (hasExtraId && hasExtraBytesSize) {
                val deletedItemId = intent.extras!!.getString(INTENT_DELETED_ITEM_ID)!!
                val deletedItemBytesSize = intent.extras!!.getInt(INTENT_DELETED_ITEM_BYTES_SIZE)
                viewModel.deleteItemFromMutableStates(deletedItemId, deletedItemBytesSize)
            }

        }

    }


    @Composable
    fun MainContent(navController: NavHostController, paddingValues: PaddingValues) {
        if (viewModel.stateFailureRequest.value) {
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
        } else {
            Column(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = paddingValues.calculateBottomPadding()
                    )
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
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val buttonLogout = createRef()
            val textUsername = createRef()
            val textEmail = createRef()

            Text(
                text = UserManager.user!!.name,
                fontSize = 30.sp,
                modifier = Modifier
                    .constrainAs(textUsername) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 16.dp, top = 16.dp)
            )
            Text(
                text = "Email: ${UserManager.user!!.email}",
                modifier = Modifier
                    .constrainAs(textEmail) {
                        top.linkTo(textUsername.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(top = 16.dp, start = 16.dp),
                fontSize = 18.sp
            )

            Button(
                onClick = {
                    viewModel.logout()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                ),
                modifier = Modifier
                    .constrainAs(buttonLogout) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(text = "Sign out")
            }
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
                    .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(text = "Amount photos: ${viewModel.stateAmountItems.value}")
                Text(text = "Memory usage: ${viewModel.stateMemoryUsage.value}")
            }
            if (viewModel.stateAmountItems.value != 0) {
                LazyVerticalGrid(

                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    items(viewModel.stateAmountItems.value) { n ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (viewModel.stateItems.value != null && viewModel.stateItems.value!!.size > n) {
                                val itemModel = viewModel.stateItems.value!![n]
                                Image(bitmap = itemModel.image,
                                    contentDescription = "photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .height(150.dp)
                                        .width(150.dp)
                                        .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            startPhotoInfoActivity(itemModel)
                                        }
                                )
                                Text(
                                    text = if (itemModel.filename.length > 10) {
                                        itemModel.filename.substring(0, 7) + "..."
                                    } else {
                                        itemModel.filename
                                    },
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .height(164.dp)
                                        .width(150.dp)
                                        .clip(RoundedCornerShape(80.dp))
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
            } else {
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
        val lottieCompositionUploading =
            rememberLottieComposition(spec = LottieCompositionSpec.Asset("ic_lottie_upload.json"))

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val buttonUpload = createRef()
            val lazyColumnUploadHistory = createRef()
            val textNothingUploaded = createRef()

            if (viewModel.stateUploadingHistory.value.isEmpty()) {
                Text(
                    text = "Upload your first file!",
                    modifier = Modifier
                        .constrainAs(textNothingUploaded) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        }
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(viewModel.stateUploadingHistory.value) { item ->
                        UploadingHistoryItem(item)
                    }
                }
            }
            FloatingActionButton(
                containerColor = Orange,
                onClick = {
                    pickImage()
                },
                modifier = Modifier
                    .constrainAs(buttonUpload) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp)
                    .size(60.dp)
            ) {
                LottieAnimation(
                    composition = lottieCompositionUploading.value,
                    isPlaying = true,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        Box(

        ) {

        }
    }


    @Composable
    fun UploadingHistoryItem(item: UploadingHistoryItem) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            if (item.image == null) {
                Image(
                    painter = painterResource(id = R.drawable.template_no_image),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                )
            } else {
                Image(
                    bitmap = item.image,
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = if(item.filename.length>30) {
                        item.filename.substring(0, 27) + "..."
                    }
                    else {
                        item.filename
                    },
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(end = 16.dp)
                )
                Text("Uploaded ${item.date}")
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
                    drawableId = R.drawable.ic_face,
                    route = Routes.Account.route
                ),
                BarItem(
                    title = "Photos",
                    drawableId = R.drawable.ic_photo,
                    route = Routes.Photos.route
                ),
                BarItem(
                    title = "Uploading",
                    drawableId = R.drawable.ic_history,
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
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = navItem.drawableId),
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


    private fun pickImage() {
        pickImageLauncher.launch(Unit)
    }

    private fun startPhotoInfoActivity(itemModel: ItemModel) {
        val intent = Intent(this, PhotoInfoActivity::class.java)
        ActivityCommunicationManager.putItemModel(itemModel)
        startActivity(intent)
    }

    private fun toastNoBitmap() {
        Toast.makeText(this, "Unable to open image.", Toast.LENGTH_LONG).show()
    }


    data class BarItem(
        val title: String,
        @DrawableRes val drawableId: Int,
        val route: String
    )

    sealed class Routes(val route: String) {
        object Account : Routes("account")
        object Photos : Routes("photos")
        object Uploading : Routes("uploading")
    }

}













