package com.terabyte.mediastorage.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.terabyte.mediastorage.R
import com.terabyte.mediastorage.ui.theme.MediaStorageTheme
import com.terabyte.mediastorage.ui.theme.Orange
import com.terabyte.mediastorage.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[LoginViewModel::class]

        enableEdgeToEdge()
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()
            MediaStorageTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(snackBarHostState)
                    }
                ) { innerPadding ->
                    LoginContent(snackBarHostState, coroutineScope)
                }
            }
        }
    }


    @Composable
    fun RowLogin(modifier: Modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth(0.8f)
        ) {
            Text(
                text = "Email:",
                color = Color.White,
                fontSize = 18.sp,
            )
            TextField(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    errorTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    disabledIndicatorColor = Color.White,
                    errorIndicatorColor = Color.White
                ),
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                value = viewModel.stateLogin.value,
                onValueChange = {
                    viewModel.stateLogin.value = it
                },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
        }
    }

    @Composable
    fun RowPassword(modifier: Modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth(0.8f)
        ) {
            Text(
                text = "Password:",
                color = Color.White,
                fontSize = 18.sp,
            )
            TextField(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    errorTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    disabledIndicatorColor = Color.White,
                    errorIndicatorColor = Color.White
                ),
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                singleLine = true,
                value = viewModel.statePassword.value,
                onValueChange = {
                    viewModel.statePassword.value = it
                },
                visualTransformation = if (viewModel.statePasswordVisibility.value) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
            IconButton(
                onClick = {
                    viewModel.statePasswordVisibility.value =
                        !viewModel.statePasswordVisibility.value
                },
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_show_password),
                    contentDescription = "",
                    tint = if (viewModel.statePasswordVisibility.value) {
                        Color.Blue
                    } else {
                        Color.White
                    }
                )
            }
        }
    }

    @Composable
    fun LoginContent(snackbarHostState: SnackbarHostState, coroutineScope: CoroutineScope) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Orange)
        ) {
            val (textHello, linearLogin, linearPassword, buttonShowPassword, buttonLogin) = createRefs()
            Text(
                text = "Hello!",
                fontSize = 34.sp,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(textHello) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom, margin = 250.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }

            )
            RowLogin(
                modifier = Modifier
                    .constrainAs(linearLogin) {
                        top.linkTo(textHello.top, margin = 100.dp)
                        centerHorizontallyTo(parent)
                    },

                )
            RowPassword(
                modifier = Modifier
                    .constrainAs(linearPassword) {
                        top.linkTo(linearLogin.bottom, margin = 16.dp)
                        centerHorizontallyTo(parent)
                    },

                )

            Button(
                onClick = {
                    viewModel.login(
                        successListener = {
                            startMainActivity()
                        },
                        incorrectLoginListener = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Incorrect email or password.")
                            }
                        },
                        failureListener = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Unable to sign in. Check your Internet connection")
                            }
                        }
                    )

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .constrainAs(buttonLogin) {
                        top.linkTo(linearPassword.bottom)
                        bottom.linkTo(parent.bottom)
                        centerHorizontallyTo(parent)
                    }
            ) {
                Text(text = "Login!", color = Orange)
            }

        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}



