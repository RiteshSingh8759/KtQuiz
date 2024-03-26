package com.kLoc.ktquizz1.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kLoc.ktquizz1.components.TopBar
import com.kLoc.ktquizz1.presentation.profile.components.RevokeAccess
import com.kLoc.ktquizz1.presentation.users.UserContentScreen
import com.kLoc.ktquizz1.util.Constants.PROFILE_SCREEN

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    control: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBar(
                title = PROFILE_SCREEN,
                signOut = {
                    viewModel.signOut()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }
            )
        }
    )
    {
        UserContentScreen(control,viewModel)
    }

    RevokeAccess(
        scaffoldState = scaffoldState,
        coroutineScope = coroutineScope,
        signOut = {
            viewModel.signOut()
        }
    )
}