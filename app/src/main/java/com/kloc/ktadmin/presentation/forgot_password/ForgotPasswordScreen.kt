package com.kloc.ktadmin.presentation.forgot_password


import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.kLoc.ktadmin.presentation.forgot_password.components.ForgotPasswordTopBar
import com.kloc.ktadmin.presentation.forgot_password.components.ForgotPassword
import com.kloc.ktadmin.presentation.forgot_password.components.ForgotPasswordContent
import com.kloc.ktadmin.util.Constants.RESET_PASSWORD_MESSAGE
import com.kloc.ktadmin.util.extentionsFunctions.Companion.showMessage

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ForgotPasswordTopBar(
                navigateBack = navigateBack
            )
        },
        content = { padding ->
            ForgotPasswordContent(
                padding = padding,
                sendPasswordResetEmail = { email ->
                    viewModel.sendPasswordResetEmail(email)
                }
            )
        }
    )

    ForgotPassword(
        navigateBack = navigateBack,
        showResetPasswordMessage = {
            showMessage(context, RESET_PASSWORD_MESSAGE)
        },
        showErrorMessage = { errorMessage ->
            showMessage(context, errorMessage)
        }
    )
}




