package com.kloc.ktadmin.presentation.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kloc.ktadmin.components.TopBar1
import com.kloc.ktadmin.presentation.profile.ProfileViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdminDashboardScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToResultScreen: () -> Unit,
    control: NavHostController
) {
    val isInsert = remember { mutableStateOf(false) }
    Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold( topBar = {
                    TopBar1(
                        title ="Test Series",
                        signOut = {
                            viewModel.signOut()
                        }

                    )
                },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            isInsert.value = true
                        })
                        {
                            Icon(Icons.Default.Add , contentDescription ="")
                        }
                    },
                    bottomBar = {
                        BottomBar(navResultScreen = navigateToResultScreen)
                    }
               , content = { TestSeriesScreen(isInsert,control) } )
//                {
//                    TestSeriesScreen(isInsert,control)
//                }
            }
}
@Composable
fun BottomBar(navResultScreen: () -> Unit) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomNavigationItem(
            selected = false,
            onClick = {
                // Navigate to the Home screen

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
                navResultScreen()
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



