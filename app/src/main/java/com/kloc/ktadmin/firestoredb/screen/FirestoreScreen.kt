package com.kloc.ktadmin.firestoredb.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kloc.ktadmin.common.CommonDialog
import com.kloc.ktadmin.firestoredb.viewmodel.FirestoreViewModel
import com.kloc.ktadmin.util.ResultState
import com.kloc.ktadmin.util.extentionsFunctions
import com.kloc.ktadmin.firestoredb.module.FirestoreModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirestoreScreen(
    isInput: MutableState<Boolean>,
    viewModel: FirestoreViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDialog by remember { mutableStateOf(false) }
    val isUpdate = remember { mutableStateOf(false) }
    val res = viewModel.res.value
    val isRefresh = remember { mutableStateOf(false) }

    if (isDialog)
        CommonDialog()

    if (isInput.value) {

        AlertDialog(onDismissRequest = { isInput.value = false },
            title = { Text(text = "Add your List", modifier = Modifier.padding(vertical = 10.dp)) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(value = name, onValueChange = {
                        name = it
                    },
                        placeholder = { Text(text = "Enter name") }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(value = email, onValueChange = {
                        email = it
                    },
                        placeholder = { Text(text = "Enter email") }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(value = password, onValueChange = {
                        password = it
                    },
                        placeholder = { Text(text = "Enter password") }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(value = confirmPassword, onValueChange = {
                        confirmPassword = it
                    },
                        placeholder = { Text(text = "Enter confirmPassword") }
                    )
                    Button(onClick = {
                        scope.launch(Dispatchers.IO) {
                            viewModel.insert(
                                FirestoreModel.FirestoreUser(
                                    name,
                                    email,
                                    password,
                                    coins = 0
                                )
                            ).collect {
                                when (it) {
                                    is ResultState.Success<*> -> {
                                        email=""
                                        password=""
                                        name=""
                                        confirmPassword=""
                                        isDialog = false
                                        isInput.value = false
                                        isRefresh.value = true
                                        extentionsFunctions.showMessage(context,"${it.data}")

                                    }
                                    is ResultState.Failure -> {
                                        isDialog = false
                                        extentionsFunctions.showMessage(context,"${it.e}")
                                    }
                                    ResultState.Loading -> {
                                        isDialog = true
                                    }
                                }
                            }
                        }
                    }) {
                        Text(text = "Save")
                    }
                }
            },
           confirmButton = {

           }
        )
    }

    if(isRefresh.value) {
        isRefresh.value = false
        viewModel.getUsers()
    }

    if (isUpdate.value)
        Update(
            viewModel.updateData.value,
            isUpdate,
            viewModel,
            isRefresh
        )

    if (res.data.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            //this is the code to shuffle the list
            Collections.shuffle(res.data)
            items(res.data, key = {
                it.key!!
            }) { user ->
                EachRow1(user = user, onUpdate = {
                    isUpdate.value = true
                    viewModel.setData(
                        FirestoreModel(
                            key = user.key,
                            user = FirestoreModel.FirestoreUser(
                                user.user?.name,
                                user.user?.email,
                                user.user?.password,
                                user.user?.coins
                            )
                        )
                    )
                }) {
                    scope.launch {
                        viewModel.delete(user.key!!).collect {
                            when (it) {
                                is ResultState.Success<*> -> {
                                    isDialog = false
                                    isRefresh.value = true
                                    extentionsFunctions.showMessage(context,"${it.data}")
                                }
                                is ResultState.Failure -> {
                                    isDialog = false
                                    extentionsFunctions.showMessage(context,"${it.e}")
                                }
                                ResultState.Loading -> {
                                    isDialog = true
                                }


                            }
                        }
                    }
                }
            }

        }
    }


    if (res.error.isNotEmpty())
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = res.error)
        }

    if (res.isLoading)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EachRow1(
    user: FirestoreModel,
    onUpdate: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        onClick = {
            onUpdate()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = user.user?.name!!, style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                IconButton(onClick = {
                    onDelete()
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "", tint = Color.Red)
                }
            }
            Text(
                text = user.user?.email!!, style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            )
            Text(
                text = user.user?.password!!, style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            )
            Text(
                text = (user.user?.coins.toString()), style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Update(
    user: FirestoreModel,
    isDialog: MutableState<Boolean>,
    viewModel: FirestoreViewModel,
    isRefresh: MutableState<Boolean>
) {

    var name by remember { mutableStateOf(user.user?.name) }
    var email by remember { mutableStateOf(user.user?.email) }
    var password by remember { mutableStateOf(user.user?.password) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var progress by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = { isDialog.value = false },
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "Update List", modifier = Modifier.padding(vertical = 10.dp))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(value = name!!, onValueChange = {
                    name = it
                },
                    placeholder = { Text(text = "Enter name") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextField(value = email!!, onValueChange = {
                    email = it
                },
                    placeholder = { Text(text = "Enter email") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextField(value = password!!, onValueChange ={password=it}, placeholder = {
                    Text(text = "Enter password")
                } )
            }
        },
        confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    scope.launch {
                        viewModel.update(
                            FirestoreModel(
                                key = user.key,
                                user = FirestoreModel.FirestoreUser(
                                    name,
                                    email,
                                   password
                                )
                            )
                        ).collect {
                            when (it) {
                                is ResultState.Success<*> -> {
                                    extentionsFunctions.showMessage(context,"${it.data}")
                                    isDialog.value = false
                                    progress = false
                                    isRefresh.value = true
                                }
                                is ResultState.Failure -> {
                                    extentionsFunctions.showMessage(context,"${it.e}")
                                    isDialog.value = false
                                    progress = false
                                }
                                is ResultState.Loading -> {
                                    progress = true
                                }



                            }
                        }
                    }
                }) {
                    Text(text = "Update")
                }
            }
        }
    )

    if (progress)
        CommonDialog()
}