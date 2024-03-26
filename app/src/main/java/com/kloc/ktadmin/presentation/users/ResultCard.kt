package com.kloc.ktadmin.presentation.users

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kloc.ktadmin.R
import com.kloc.ktadmin.navigation.Screen

@Composable
fun ResultCard(score: String, control: NavHostController, total: String)
{
    var tryAgainClicked by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        // Handling back button press
        if (tryAgainClicked) {
            control.popBackStack(Screen.ProfileScreen.route, inclusive = false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.congrats))
        LottieAnimation(
            modifier = Modifier,
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        val composition1 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw._con))
        animateLottieCompositionAsState(composition = composition1)
        LottieAnimation(
            modifier = Modifier
                .height(150.dp),
            composition = composition1,
            iterations = LottieConstants.IterateForever

        )
        Text(text = "You Scored", fontSize = 25.sp, color = Color.White)
        Text(text = "${score} out of ${total}", color = Color.Yellow, fontSize = 25.sp)
        Text(text = "&",color=Color.White, fontSize = 25.sp)
        Text(text = "Coins Earned",color=Color.White, fontSize = 25.sp)
        Text(text = ((score.toInt())*2).toString(), color = Color.Yellow, fontSize = 25.sp)
        Button(
            onClick = {
                tryAgainClicked = true
                control.navigate(Screen. ProfileScreen.route)
            },
            modifier = Modifier.width(200.dp),
            elevation = ButtonDefaults.buttonElevation(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor =Color(0xFFB4A5E0),
                contentColor = Color.White
            )

        ) {
            Text(
                text = "Try Again",
                fontSize = 18.sp
            )
        }


    }

}
