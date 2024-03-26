package com.kLoc.ktquizz1.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kLoc.ktquizz1.presentation.admin.ResultScreen
import com.kLoc.ktquizz1.presentation.admin.QuestionScreen
import com.kLoc.ktquizz1.navigation.Screen.ForgotPasswordScreen
import com.kLoc.ktquizz1.navigation.Screen.ProfileScreen
import com.kLoc.ktquizz1.navigation.Screen.AdminDashboardScreen
import com.kLoc.ktquizz1.navigation.Screen.ResultScreen
import com.kLoc.ktquizz1.navigation.Screen.SignInScreen
import com.kLoc.ktquizz1.navigation.Screen.SignUpScreen
import com.kLoc.ktquizz1.navigation.Screen.VerifyEmailScreen
import com.kLoc.ktquizz1.presentation.forgot_password.ForgotPasswordScreen
import com.kLoc.ktquizz1.presentation.profile.ProfileScreen
import com.kLoc.ktquizz1.presentation.admin.AdminDashboardScreen
import com.kLoc.ktquizz1.presentation.sign_in.SignInScreen
import com.kLoc.ktquizz1.presentation.sign_up.SignUpScreen
import com.kLoc.ktquizz1.presentation.users.QuestionSeriesScreen
import com.kLoc.ktquizz1.presentation.users.ResultCard
import com.kLoc.ktquizz1.presentation.verify_email.VerifyEmailScreen

@Composable
@ExperimentalComposeUiApi
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = SignInScreen.route
    ) {
        composable(
            route = SignInScreen.route
        ) {
            SignInScreen(
                navigateToForgotPasswordScreen = {
                    navController.navigate(ForgotPasswordScreen.route)
                },
                navigateToSignUpScreen = {
                    navController.navigate(SignUpScreen.route)
                }
            )
        }
        composable(
            route = ForgotPasswordScreen.route
        ) {
            ForgotPasswordScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = SignUpScreen.route
        ) {
            SignUpScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = VerifyEmailScreen.route
        ) {
            VerifyEmailScreen(
                navigateToProfileScreen = {
                    navController.navigate(ProfileScreen.route) {
                        Log.e("check", "VerifyEmailScreen")
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = ProfileScreen.route
        ) {
            Log.e("check", "composable")
            ProfileScreen(
                control=navController
            )
        }


        composable(
            route =AdminDashboardScreen.route
        )
        {
            AdminDashboardScreen(
                navigateToResultScreen = {
                    navController.navigate(ResultScreen.route) {
                        Log.e("check", "AdminDashboardScreen")
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                control=navController
            )
        }
        composable(
            route = ResultScreen.route
        )
        {
            ResultScreen {
                navController.navigate(AdminDashboardScreen.route) {
                    Log.e("check", "AdminDashboardScreen")
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }

        }
        composable("Question/{seriesname}")
        {
            var seriesname=it.arguments?.getString("seriesname")
            QuestionScreen(seriesname=seriesname!!,control=navController)
        }
        composable("QuestionSeries/{seriesname}")
        {
            var seriesname=it.arguments?.getString("seriesname")
            QuestionSeriesScreen(seriesname=seriesname!!,control=navController)
        }
        composable("resultant/{score}")
        {
            var score=it.arguments?.getString("score")
            ResultCard(score=score!!,control=navController)
        }
    }
}


