package com.kloc.ktadmin.presentation.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuestionScreen(seriesname: String, control: NavHostController)
{
    val isInsert = remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    )
    {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BottomNavigationItem(
                        selected = false,
                        onClick = {
                            // Navigate to the Home screen
                          control.popBackStack()
                        },
                        icon = {
                            // Icon for Home screen
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        },
                        label = {
                            // Label for Home screen
                            Text(text = "Back")
                        }
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    isInsert.value = true
                })
                {
                    Icon(Icons.Default.Add , contentDescription ="")
                }
            },

        )
        {
            QuestionScreenContent(isInsert,seriesname)
        }
    }
}



