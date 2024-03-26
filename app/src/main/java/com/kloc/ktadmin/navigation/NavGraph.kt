package com.kloc.ktadmin.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kloc.ktadmin.presentation.admin.ResultScreen
import com.kloc.ktadmin.presentation.admin.QuestionScreen
import com.kloc.ktadmin.navigation.Screen.ForgotPasswordScreen
import com.kloc.ktadmin.navigation.Screen.ProfileScreen
import com.kloc.ktadmin.navigation.Screen.AdminDashboardScreen
import com.kloc.ktadmin.navigation.Screen.ResultScreen
import com.kloc.ktadmin.navigation.Screen.SignInScreen
import com.kloc.ktadmin.navigation.Screen.SignUpScreen
import com.kloc.ktadmin.navigation.Screen.VerifyEmailScreen
import com.kloc.ktadmin.presentation.forgot_password.ForgotPasswordScreen
import com.kloc.ktadmin.presentation.profile.ProfileScreen
import com.kloc.ktadmin.presentation.admin.AdminDashboardScreen
import com.kloc.ktadmin.presentation.sign_in.SignInScreen
import com.kloc.ktadmin.presentation.sign_up.SignUpScreen
import com.kloc.ktadmin.presentation.users.QuestionSeriesScreen
import com.kloc.ktadmin.presentation.users.ResultCard
import com.kloc.ktadmin.presentation.verify_email.VerifyEmailScreen

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
                control = navController
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
        composable("resultant/{score}/{total}")
        {
            var score=it.arguments?.getString("score")
            var total=it.arguments?.getString("total")
            ResultCard(score=score!!,control=navController,total=total!!)
        }
    }
}


