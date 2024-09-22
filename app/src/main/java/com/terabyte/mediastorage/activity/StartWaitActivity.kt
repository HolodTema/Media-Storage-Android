package com.terabyte.mediastorage.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp
import com.terabyte.mediastorage.retrofit.RetrofitManager
import com.terabyte.mediastorage.ui.theme.Orange
import com.terabyte.mediastorage.util.AccessTokenManager
import com.terabyte.mediastorage.util.UserManager

class StartWaitActivity : ComponentActivity() {
    private lateinit var stateDialog: MutableState<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            stateDialog = remember {
                mutableStateOf(false)
            }
            if(stateDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        checkIfTokenExpired(AccessTokenManager.getAccessToken())
                        stateDialog.value = false
                    },
                    title = {
                        Text("Something went wrong...")
                    },
                    text = {
                        Text("Check your Internet connection.")
                    },
                    confirmButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Orange
                            ),
                            onClick = {
                                checkIfTokenExpired(AccessTokenManager.getAccessToken())
                                stateDialog.value = false
                            }
                        ) {
                            Text("Ok", fontSize = 18.sp)
                        }
                    }
                )
            }
            AccessTokenManager.cacheIfTokenExists(applicationContext) {
                if(it) {
                    checkIfTokenExpired(AccessTokenManager.getAccessToken())
                }
                else {
                    startLoginActivity()
                }
            }
        }
    }

    private fun checkIfTokenExpired(accessToken: String) {
        RetrofitManager.getCurrentUser(
            applicationContext,
            accessToken,
            successListener = {
                UserManager.cache(it)
                startMainActivity()
            },
            unauthorizedListener = {
                AccessTokenManager.deleteAccessToken(applicationContext)
                startLoginActivity()
            },
            failureListener = {
                stateDialog.value = true
            }
        )
    }

    private fun startLoginActivity() {
        startActivity(
            Intent(applicationContext, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    private fun startMainActivity() {
        startActivity(
            Intent(applicationContext, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }
}

