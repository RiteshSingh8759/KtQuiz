package com.kLoc.ktquizz1.presentation.verify_email.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kLoc.ktquizz1.R
import com.kLoc.ktquizz1.components.SmallSpacer
import com.kLoc.ktquizz1.util.Constants.ALREADY_VERIFIED
import com.kLoc.ktquizz1.util.Constants.SPAM_EMAIL

@Composable
fun VerifyEmailContent(
    padding: PaddingValues,
    reloadUser: () -> Unit
) {
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
            modifier = Modifier.fillMaxSize().padding(padding).padding(start = 32.dp, end = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.clickable {
                    reloadUser()
                },
                text = ALREADY_VERIFIED,
                color = Color.White,
                fontSize = 20.sp,
                textDecoration = TextDecoration.Underline
            )
            SmallSpacer()
            Text(
                text = SPAM_EMAIL,
                color = Color.White,
                fontSize = 15.sp
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun VerifyEmailContentPreview() {
    VerifyEmailContent(
        padding = PaddingValues(25.dp),
        reloadUser = { /* Implement reloadUser functionality */ }
    )
}