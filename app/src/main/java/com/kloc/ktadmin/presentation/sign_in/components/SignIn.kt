package com.kloc.ktadmin.presentation.sign_in.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.kloc.ktadmin.components.ProgressBar
import com.kloc.ktadmin.presentation.sign_in.SignInViewModel
import com.kloc.ktadmin.util.ResultState.Failure
import com.kloc.ktadmin.util.ResultState.Loading
import com.kloc.ktadmin.util.ResultState.Success

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