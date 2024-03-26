package com.kloc.ktadmin.presentation.profile.components

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.kloc.ktadmin.components.ProgressBar
import com.kloc.ktadmin.presentation.profile.ProfileViewModel
import com.kloc.ktadmin.util.ResultState.Failure
import com.kloc.ktadmin.util.ResultState.Loading
import com.kloc.ktadmin.util.ResultState.Success
import com.kloc.ktadmin.util.Constants.ACCESS_REVOKED_MESSAGE
import com.kloc.ktadmin.util.Constants.REVOKE_ACCESS_MESSAGE
import com.kloc.ktadmin.util.Constants.SENSITIVE_OPERATION_MESSAGE
import com.kloc.ktadmin.util.Constants.SIGN_OUT_ITEM
import com.kloc.ktadmin.util.extentionsFunctions.Companion.showMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RevokeAccess(
    viewModel: ProfileViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    signOut: () -> Unit,
) {
    val context = LocalContext.current

    fun showRevokeAccessMessage() = coroutineScope.launch {
        val result = scaffoldState.snackbarHostState.showSnackbar(
            message = REVOKE_ACCESS_MESSAGE,
            actionLabel = SIGN_OUT_ITEM
        )
        if (result == SnackbarResult.ActionPerformed) {
            signOut()
        }
    }

    when(val revokeAccessResponse = viewModel.revokeAccessResponse) {
        is Loading -> ProgressBar()
        is Success -> {
            val isAccessRevoked = revokeAccessResponse.data
            LaunchedEffect(isAccessRevoked) {
                if (isAccessRevoked) {
                    showMessage(context, ACCESS_REVOKED_MESSAGE)
                }
            }
        }
        is Failure -> revokeAccessResponse.apply {
            LaunchedEffect(e) {
                print(e)
                if (e.message == SENSITIVE_OPERATION_MESSAGE) {
                    showRevokeAccessMessage()
                }
            }
        }
    }
}