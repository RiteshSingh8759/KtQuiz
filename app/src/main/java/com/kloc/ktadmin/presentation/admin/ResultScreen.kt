package com.kloc.ktadmin.presentation.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResultScreen(navigateToAdminScreen: () -> Unit)
{
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(

            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BottomNavigationItem(
                        selected = false,
                        onClick = {
                            // Navigate to the Home screen
                           navigateToAdminScreen()
                        },
                        icon = {
                            // Icon for Home screen
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = {
                            // Label for Home screen
                            Text(text = "Home")
                        }
                    )
                    BottomNavigationItem(
                        selected = false,
                        onClick = {
                            // Navigate to the Results screen
                        },
                        icon = {
                            // Icon for Results screen
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = "Results"
                            )
                        },
                        label = {
                            // Label for Results screen
                            Text(text = "Results")
                        }
                    )
                }
            }
        )
        {
            ResultScreenContent()
        }

    }
}
