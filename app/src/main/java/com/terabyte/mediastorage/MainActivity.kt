package com.terabyte.mediastorage

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.terabyte.mediastorage.ui.theme.MediaStorageTheme
import com.terabyte.mediastorage.ui.theme.Orange

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediaStorageTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainContent() {
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
                }
        )
        RowPassword(
            modifier = Modifier
                .constrainAs(linearPassword) {
                    top.linkTo(linearLogin.bottom, margin = 16.dp)
                    centerHorizontallyTo(parent)
                }
        )

        Button(
            onClick = {

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

@Composable
fun RowLogin(modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth(0.8f)
    ) {
        Text(
            text = "Login:",
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
            value = "",
            onValueChange = {

            },
            modifier = Modifier
                .padding(start = 16.dp)
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
            value = "",
            onValueChange = {

            },
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        )
        IconButton(
            onClick = {

            },
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_show_password),
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}