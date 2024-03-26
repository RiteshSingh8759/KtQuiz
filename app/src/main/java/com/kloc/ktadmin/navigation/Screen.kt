package com.kloc.ktadmin.navigation

import com.kloc.ktadmin.util.Constants.ADMIN_SCREEN
import com.kloc.ktadmin.util.Constants.FORGOT_PASSWORD_SCREEN
import com.kloc.ktadmin.util.Constants.PROFILE_SCREEN
import com.kloc.ktadmin.util.Constants.QUESTION_SCREEN
import com.kloc.ktadmin.util.Constants.RESULT_SCREEN
import com.kloc.ktadmin.util.Constants.SIGN_IN_SCREEN
import com.kloc.ktadmin.util.Constants.SIGN_UP_SCREEN
import com.kloc.ktadmin.util.Constants.TEST_SCREEN
import com.kloc.ktadmin.util.Constants.VERIFY_EMAIL_SCREEN


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