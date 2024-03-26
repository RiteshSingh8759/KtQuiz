package com.kLoc.ktquizz1.presentation.forgot_password.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kLoc.ktquizz1.R
import com.kLoc.ktquizz1.components.EmailField
import com.kLoc.ktquizz1.components.SmallSpacer
import com.kLoc.ktquizz1.ui.theme.KTQuizz1Theme
import com.kLoc.ktquizz1.util.Constants.EMPTY_STRING
import com.kLoc.ktquizz1.util.Constants.RESET_PASSWORD_BUTTON

@Composable
fun ForgotPasswordContent(
    padding: PaddingValues,
    sendPasswordResetEmail: (email: String) -> Unit,
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
            EmailField(
                email = email,
                onEmailValueChange = { newValue ->
                    email = newValue
                }
            )
            SmallSpacer()
            Button(
                onClick = {
                    sendPasswordResetEmail(email.text)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(100.dp)
            ) {
                Text(
                    text = RESET_PASSWORD_BUTTON,
                    fontSize = 15.sp
                )
            }
        }
    }
}
@Composable
@Preview
fun forgotPasswordPreview() {

        ForgotPasswordContent(
            padding = PaddingValues(0.dp),
            sendPasswordResetEmail = {}
        )

}