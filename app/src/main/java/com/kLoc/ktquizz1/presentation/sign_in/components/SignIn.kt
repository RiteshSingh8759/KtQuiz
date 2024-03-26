package com.kLoc.ktquizz1.presentation.sign_in.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.kLoc.ktquizz1.components.ProgressBar
import com.kLoc.ktquizz1.presentation.sign_in.SignInViewModel
import com.kLoc.ktquizz1.util.ResultState.Loading
import com.kLoc.ktquizz1.util.ResultState.Success
import com.kLoc.ktquizz1.util.ResultState.Failure
@Composable
fun SignIn(
    viewModel: SignInViewModel = hiltViewModel(),
    showErrorMessage: (errorMessage: String?) -> Unit
) {
    when(val signInResponse = viewModel.signInResponse) {
        is Loading -> ProgressBar()
        is Success -> Unit
        is Failure -> signInResponse.apply {
            LaunchedEffect(e) {
                print(e)
                showErrorMessage(e.message)
            }
        }
    }
}