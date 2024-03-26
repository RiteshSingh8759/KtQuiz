package com.kLoc.ktadmin.presentation.forgot_password.components


import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.kloc.ktadmin.components.BackIcon
import com.kloc.ktadmin.util.Constants.FORGOT_PASSWORD_SCREEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar (
        title = {
            Text(
                text = FORGOT_PASSWORD_SCREEN,
                fontSize = 20.sp,
                color = Color.White
            )
        },
        navigationIcon = {
            BackIcon(
                navigateBack = navigateBack
            )
        },
        colors =  TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor =Color.Transparent,
        )

    )
}
