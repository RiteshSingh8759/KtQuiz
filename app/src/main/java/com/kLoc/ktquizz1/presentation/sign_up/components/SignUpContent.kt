package com.kLoc.ktquizz1.presentation.sign_up.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kLoc.ktquizz1.R
import com.kLoc.ktquizz1.components.ConfirmPasswordField
import com.kLoc.ktquizz1.components.EmailField
import com.kLoc.ktquizz1.components.NameField
import com.kLoc.ktquizz1.components.PasswordField
import com.kLoc.ktquizz1.components.SmallSpacer
import com.kLoc.ktquizz1.firestoredb.module.FirestoreModel
import com.kLoc.ktquizz1.firestoredb.viewmodel.FirestoreViewModel
import com.kLoc.ktquizz1.presentation.sign_in.components.SignInContent
import com.kLoc.ktquizz1.util.Constants.ALREADY_USER
import com.kLoc.ktquizz1.util.Constants.EMPTY_STRING
import com.kLoc.ktquizz1.util.Constants.SIGN_UP_BUTTON
import com.kLoc.ktquizz1.util.ResultState
import com.kLoc.ktquizz1.util.extentionsFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
@ExperimentalComposeUiApi
fun SignUpContent(
    padding: PaddingValues,
    signUp: (email: String, password: String) -> Unit,
    navigateBack: () -> Unit,
    firestoreViewModel: FirestoreViewModel= hiltViewModel()
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
    var confirmPassword by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = EMPTY_STRING
                )
            )
        }
    )
    var name by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = EMPTY_STRING
                )
            )
        }
    )
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDialog by remember { mutableStateOf(false) }
    val isUpdate = remember { mutableStateOf(false) }
    val res = firestoreViewModel.res.value
    val isRefresh = remember { mutableStateOf(false) }
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
                text = "JOIN US",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(40.dp))
                NameField(name = name,
                    onNameValueChange = { newValue ->
                        name = newValue
                    }
                )
                SmallSpacer()
                EmailField(
                    email = email,
                    onEmailValueChange = { newValue ->
                        email = newValue
                    }
                )
                SmallSpacer()
                PasswordField(
                    password = password,
                    onPasswordValueChange = { newValue ->
                        password = newValue
                    }
                )
                SmallSpacer()
                ConfirmPasswordField( confirmpassword = confirmPassword,
                    onconfirmpasswordValueChange= { newValue ->
                        confirmPassword = newValue
                    } )
                SmallSpacer()
                Button(
                    onClick = {
                        keyboard?.hide()
                        signUp(email.text, password.text)
                        scope.launch(Dispatchers.IO) {
                            firestoreViewModel.insert(
                                FirestoreModel.FirestoreUser(
                                    name.text,
                                    email.text,
                                    password.text,
                                    coins = 0
                                )
                            ).collect {
                                when (it) {
                                    is ResultState.Success<*> -> {
                                        isRefresh.value = true
                                        extentionsFunctions.showMessage(context,"${it.data}")

                                    }
                                    is ResultState.Failure -> {
                                        isDialog = false
                                        extentionsFunctions.showMessage(context,"${it.e}")
                                    }
                                    ResultState.Loading -> {
                                        isDialog = true
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(
                        text = SIGN_UP_BUTTON,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.clickable {
                        navigateBack()
                    },
                    text = ALREADY_USER,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun SignUpContentPreview() {
    SignUpContent(
        padding = PaddingValues(25.dp),
        signUp = { email, password -> /* Implement signUp functionality */ },
        navigateBack = { /* Implement navigateBack functionality */ }
    )
}