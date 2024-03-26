package com.kLoc.ktquizz1.presentation.users

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ResultCard(score: String, control: NavHostController)
{
    Text(text = score, fontSize = 100.sp)
}