package com.kLoc.ktquizz1.presentation.verify_email

import androidx.compose.foundation.background
import androidx.compose.material.Colors
import androidx.compose.material3.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kLoc.ktquizz1.components.TopBar
import com.kLoc.ktquizz1.presentation.profile.ProfileViewModel
import com.kLoc.ktquizz1.presentation.profile.components.RevokeAccess
import com.kLoc.ktquizz1.presentation.verify_email.components.ReloadUser
import com.kLoc.ktquizz1.presentation.verify_email.components.VerifyEmailContent
import com.kLoc.ktquizz1.util.Constants.EMAIL_NOT_VERIFIED_MESSAGE
import com.kLoc.ktquizz1.util.Constants.VERIFY_EMAIL_SCREEN
import com.kLoc.ktquizz1.util.extentionsFunctions.Companion.showMessage

@Composable
fun VerifyEmailScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToProfileScreen: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(
                title = VERIFY_EMAIL_SCREEN,
                signOut = {
                    viewModel.signOut()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }

            )
        },
        content = { padding ->
            VerifyEmailContent(
                padding = padding,
                reloadUser = {
                    viewModel.reloadUser()
                }
            )
        }
    )

    ReloadUser(
        navigateToProfileScreen = {
            if (viewModel.isEmailVerified) {
                navigateToProfileScreen()
            } else {
                showMessage(context, EMAIL_NOT_VERIFIED_MESSAGE)
            }
        }
    )

    RevokeAccess(
        scaffoldState = scaffoldState,
        coroutineScope = coroutineScope,
        signOut = {
            viewModel.signOut()
        }
    )
}

