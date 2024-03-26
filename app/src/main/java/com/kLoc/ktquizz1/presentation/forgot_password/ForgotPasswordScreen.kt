package com.kLoc.ktquizz1.presentation.forgot_password
import androidx.compose.foundation.background
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kLoc.ktquizz1.presentation.forgot_password.components.ForgotPassword
import com.kLoc.ktquizz1.presentation.forgot_password.components.ForgotPasswordContent
import com.kLoc.ktquizz1.presentation.forgot_password.components.ForgotPasswordTopBar
import com.kLoc.ktquizz1.ui.theme.KTQuizz1Theme
import com.kLoc.ktquizz1.util.Constants.RESET_PASSWORD_MESSAGE
import com.kLoc.ktquizz1.util.extentionsFunctions.Companion.showMessage
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




