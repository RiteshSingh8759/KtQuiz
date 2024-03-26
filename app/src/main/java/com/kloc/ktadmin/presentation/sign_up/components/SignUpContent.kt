package com.kloc.ktadmin.presentation.sign_up.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kloc.ktadmin.components.ConfirmPasswordField
import com.kloc.ktadmin.components.EmailField
import com.kloc.ktadmin.components.NameField
import com.kloc.ktadmin.components.PasswordField
import com.kloc.ktadmin.firestoredb.viewmodel.FirestoreViewModel
import com.kloc.ktadmin.util.ResultState
import com.kloc.ktadmin.firestoredb.module.FirestoreModel
import com.kloc.ktadmin.util.Constants.ALREADY_USER
import com.kloc.ktadmin.util.Constants.EMPTY_STRING
import com.kloc.ktadmin.util.Constants.SIGN_UP_BUTTON
import com.kloc.ktadmin.util.extentionsFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.kloc.ktadmin.R
@Composable
@ExperimentalComposeUiApi
fun SignUpContent(
    padding: PaddingValues,
    signUp: (email: String, password: String) -> Unit,
    navigateBack: () -> Unit,
    firestoreViewModel: FirestoreViewModel = hiltViewModel()
) {
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDialog by remember { mutableStateOf(false) }
    val isUpdate = remember { mutableStateOf(false) }
    val res = firestoreViewModel.res.value
    val isRefresh = remember { mutableStateOf(false) }
    val keyboard = LocalSoftwareKeyboardController.current
    var exist by remember {
        mutableStateOf(false)
    }

    val gradient= Brush.linearGradient(
        0.0f to Color(0xFF153A34),
        500.0f to Color.Transparent,
        start = Offset.Zero,
        end = Offset.Infinite
    )
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
                .padding(top = 150.dp, bottom = 130.dp, start = 25.dp, end = 25.dp)
                .background(gradient), // Add padding here
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp), // Add padding here
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "JOIN US",
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(15.dp))
                NameField(name = name,
                    onNameValueChange = { newValue ->
                        name = newValue
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
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
                Text(text = "minimum 8 characters,at least one letter,one number and one special character",
                    fontSize = 12.sp, color = Color.White,modifier = Modifier.padding(start = 8.dp,end = 8.dp))
                Spacer(modifier = Modifier.height(5.dp))
                ConfirmPasswordField(confirmpassword = confirmPassword,
                    onconfirmpasswordValueChange = { newValue ->
                        confirmPassword = newValue
                    })
                Spacer(modifier = Modifier.height(5.dp))
                Button(
                    onClick = {
                        if (password == confirmPassword
                            && email.text.isNotEmpty()
                            && password.text.isNotEmpty()
                            &&name.text.trim().isNotEmpty()
                            &&confirmPassword.text.isNotEmpty())
                        {
                            if( !email.text.contains(" ")&& !password.text.contains(" ")&& !confirmPassword.text.contains(" "))
                            {
                                val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
                                val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$".toRegex()
                               if(email.text.matches(emailRegex))
                               {
                                   if(password.text.matches(passwordRegex))
                                   {
                                       val isEmailPresent = res.data.any { firestoreModel ->
                                           firestoreModel.user?.email == email.text
                                       }
                                       if (!isEmailPresent) {
                                           keyboard?.hide()
                                           signUp(email.text, password.text)
                                           scope.launch(Dispatchers.IO) {
                                               firestoreViewModel.insert(
                                                   FirestoreModel.FirestoreUser(
                                                       name.text.trim(),
                                                       email.text,
                                                       password.text,
                                                       coins = 0
                                                   )
                                               ).collect {
                                                   when (it) {
                                                       is ResultState.Success<*> -> {
                                                           isRefresh.value = true
                                                           extentionsFunctions.showMessage(
                                                               context,
                                                               "account created successfully"
                                                           )

                                                       }

                                                       is ResultState.Failure -> {
                                                           isDialog = false
                                                           extentionsFunctions.showMessage(
                                                               context,
                                                               "${it.e}"
                                                           )
                                                       }

                                                       ResultState.Loading -> {
                                                           isDialog = true
                                                       }
                                                   }
                                               }
                                           }

                                       } else {
                                           extentionsFunctions.showMessage(context, ALREADY_USER)
                                       }
                                   }
                                   else{
                                        extentionsFunctions.showMessage(context, "Enter Valid Password")
                                   }
                               }
                                else
                                {
                                    extentionsFunctions.showMessage(context, "enter valid email")
                                }
                            }
                            else{
                                extentionsFunctions.showMessage(context, "Fields cannot contain spaces.")
                            }
                        }
                        else if(password!=confirmPassword)
                        {
                            extentionsFunctions.showMessage(context, "PASSWORD_IS_NOT_MATCHED")
                        }
                        else if(name.text.trim().isEmpty())
                        {
                            extentionsFunctions.showMessage(context, "NAME_IS_EMPTY")
                        }
                        else if(email.text.isEmpty())
                        {
                            extentionsFunctions.showMessage(context, "EMAIL_IS_EMPTY")
                        }
                        else if(password.text.isEmpty())
                        {
                            extentionsFunctions.showMessage(context, "PASSWORD_IS_EMPTY")
                        }
                        else if(confirmPassword.text.isEmpty())
                        {
                            extentionsFunctions.showMessage(context, "CONFIRM_PASSWORD_IS_EMPTY")
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
                Spacer(modifier = Modifier.height(5.dp))
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
}

private fun TextFieldValue.isNullOrEmpty(): Boolean {
    return text.isNullOrEmpty()
}
