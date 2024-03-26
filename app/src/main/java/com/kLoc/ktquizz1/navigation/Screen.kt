package com.kLoc.ktquizz1.navigation

import com.kLoc.ktquizz1.util.Constants.ADMIN_SCREEN
import com.kLoc.ktquizz1.util.Constants.FORGOT_PASSWORD_SCREEN
import com.kLoc.ktquizz1.util.Constants.PROFILE_SCREEN
import com.kLoc.ktquizz1.util.Constants.QUESTION_SCREEN
import com.kLoc.ktquizz1.util.Constants.RESULT_SCREEN
import com.kLoc.ktquizz1.util.Constants.SIGN_IN_SCREEN
import com.kLoc.ktquizz1.util.Constants.SIGN_UP_SCREEN
import com.kLoc.ktquizz1.util.Constants.TEST_SCREEN
import com.kLoc.ktquizz1.util.Constants.VERIFY_EMAIL_SCREEN

sealed class Screen(val route: String) {
    object SignInScreen: Screen(SIGN_IN_SCREEN)
    object ForgotPasswordScreen: Screen(FORGOT_PASSWORD_SCREEN)
    object SignUpScreen: Screen(SIGN_UP_SCREEN)
    object VerifyEmailScreen: Screen(VERIFY_EMAIL_SCREEN)
    object ProfileScreen: Screen(PROFILE_SCREEN)
    object AdminDashboardScreen: Screen(ADMIN_SCREEN)
    object ResultScreen: Screen(RESULT_SCREEN)
    object TestSeriesScreen: Screen(TEST_SCREEN)
    object QuestionScreen: Screen(QUESTION_SCREEN)
}