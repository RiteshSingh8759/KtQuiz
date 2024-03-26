package com.kloc.ktadmin.presentation.sign_in.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kloc.ktadmin.components.EmailField
import com.kloc.ktadmin.components.PasswordField
import com.kloc.ktadmin.R
import com.kloc.ktadmin.util.Constants.EMPTY_STRING
import com.kloc.ktadmin.util.Constants.FORGOT_PASSWORD
import com.kloc.ktadmin.util.Constants.NO_ACCOUNT
import com.kloc.ktadmin.util.Constants.SIGN_IN_BUTTON

@Composable
@ExperimentalComposeUiApi
fun SignInContent(
    padding: PaddingValues,
    signIn: (email: String, password: String) -> Unit,
    navigateToForgotPasswordScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit
) {
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = EMPTY_STRING
                )
            )
        }
    )
    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = EMPTY_STRING
                )
            )
        }
    )
    val gradient= Brush.linearGradient(
        0.0f to Color(0xFF153A34),
        500.0f to Color.Transparent,
        start = Offset.Zero,
        end = Offset.Infinite
    )

    val keyboard = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painterResource(id = R.drawable.hill1),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp, bottom = 180.dp, start = 25.dp, end = 25.dp)
                .background(gradient), // Add padding here
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize().padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hello Users !",
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Welcome to Quiz App",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(15.dp))
                EmailField(
                    email = email,
                    onEmailValueChange = { newValue ->
                        email = newValue
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                PasswordField(
                    password = password,
                    onPasswordValueChange = { newValue ->
                        password = newValue
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    modifier = Modifier
                        .clickable { navigateToForgotPasswordScreen() }
                        .fillMaxWidth().padding(end = 10.dp),
                    text = FORGOT_PASSWORD,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        keyboard?.hide()
                        signIn(email.text, password.text)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(
                        text = SIGN_IN_BUTTON,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    modifier = Modifier.clickable { navigateToSignUpScreen() },
                    text = NO_ACCOUNT,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}

