package com.kloc.ktadmin.presentation.forgot_password.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.kloc.ktadmin.components.ProgressBar
import com.kloc.ktadmin.presentation.forgot_password.ForgotPasswordViewModel
import com.kloc.ktadmin.util.ResultState


@Composable
fun ForgotPassword(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    showResetPasswordMessage: () -> Unit,
    showErrorMessage: (errorMessage: String?) -> Unit
) {
    when(val sendPasswordResetEmailResponse = viewModel.sendPasswordResetEmailResponse) {
        is ResultState.Loading -> ProgressBar()
        is ResultState.Success<*> -> {
            val isPasswordResetEmailSent = sendPasswordResetEmailResponse.data
            LaunchedEffect(isPasswordResetEmailSent) {
                if (isPasswordResetEmailSent as Boolean) {
                    navigateBack()
                    showResetPasswordMessage()
                }
            }
        }
        is ResultState.Failure -> sendPasswordResetEmailResponse.apply {
            LaunchedEffect(e) {
                print(e)
                showErrorMessage(e.message)
            }
        }
    }
}