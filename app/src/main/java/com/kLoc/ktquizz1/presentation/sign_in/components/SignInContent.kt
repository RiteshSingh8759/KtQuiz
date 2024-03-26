package com.kLoc.ktquizz1.presentation.sign_in.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kLoc.ktquizz1.R
import com.kLoc.ktquizz1.components.EmailField
import com.kLoc.ktquizz1.components.PasswordField
import com.kLoc.ktquizz1.presentation.sign_in.SignInViewModel
import com.kLoc.ktquizz1.util.Constants.EMPTY_STRING
import com.kLoc.ktquizz1.util.Constants.FORGOT_PASSWORD
import com.kLoc.ktquizz1.util.Constants.NO_ACCOUNT
import com.kLoc.ktquizz1.util.Constants.SIGN_IN_BUTTON

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

    val keyboard = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painterResource(id = R.drawable.signin1),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp), // Add padding here
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hello Users !",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Welcome to Quizz App",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(40.dp))
            EmailField(
                email = email,
                onEmailValueChange = { newValue ->
                    email = newValue
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordField(
                password = password,
                onPasswordValueChange = { newValue ->
                    password = newValue
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .clickable { navigateToForgotPasswordScreen() }
                    .fillMaxWidth(),
                text = FORGOT_PASSWORD,
                fontSize = 16.sp,
                textAlign = TextAlign.End,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
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
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.clickable { navigateToSignUpScreen() },
                text = NO_ACCOUNT,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun preview() {
    val signIn: (email: String, password: String) -> Unit = { s: String, s1: String -> }
    val navigateToForgotPasswordScreen: () -> Unit = {}
    val navigateToSignUpScreen: () -> Unit = {}
    val padding = PaddingValues(16.dp)
    val navigateToAdminDashBoard:()-> Unit = {}
    SignInContent(
        padding = padding,
        signIn = signIn,
        navigateToForgotPasswordScreen = navigateToForgotPasswordScreen,
        navigateToSignUpScreen = navigateToSignUpScreen
    )
}