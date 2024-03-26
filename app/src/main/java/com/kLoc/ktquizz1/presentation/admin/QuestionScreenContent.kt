package com.kLoc.ktquizz1.presentation.admin

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kLoc.ktquizz1.common.CommonDialog
import com.kLoc.ktquizz1.firestoredb.module.QuestionModel
import com.kLoc.ktquizz1.firestoredb.viewmodel.FireStoreQuesViewModel
import com.kLoc.ktquizz1.util.ResultState
import com.kLoc.ktquizz1.util.extentionsFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun QuestionScreenContent(
   isInsert: MutableState<Boolean>,
   seriesname: String,
   viewModel:FireStoreQuesViewModel= hiltViewModel())
{
   var ques by remember { mutableStateOf("") }
   var option1 by remember { mutableStateOf("") }
   var option2 by remember { mutableStateOf("") }
   var option3 by remember { mutableStateOf("") }
   var option4 by remember { mutableStateOf("") }
   var answer by remember { mutableStateOf("") }
   val scope = rememberCoroutineScope()
   var isDialog by remember { mutableStateOf(false) }
   val isRefresh = remember { mutableStateOf(false) }
   val isUpdate = remember { mutableStateOf(false) }
    var count by remember { mutableStateOf(0)}
   val res = viewModel.res.value
   val context = LocalContext.current
   Box(
      modifier = Modifier
         .fillMaxSize()
         .padding(20.dp)
         .padding(top = 48.dp),
      contentAlignment = Alignment.TopCenter
   ) {
      if (isDialog)
         CommonDialog()

      if (isInsert.value) {

         AlertDialog(onDismissRequest = { isInsert.value = false },
            title = {
               Text(
                  text = "Add Question",
                  modifier = Modifier.padding(vertical = 10.dp),
                  textAlign = TextAlign.Center
               )
            },
            text = {
               Column(
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(horizontal = 20.dp, vertical = 20.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
               ) {
                  TextField(value = ques, onValueChange = {
                     ques = it
                  },
                     placeholder = { Text(text = "Enter Question") }
                  )
                  Spacer(modifier = Modifier.height(10.dp))
                  TextField(value = option1, onValueChange = {
                     option1 = it
                  },
                     placeholder = { Text(text = "option1") }
                  )
                  Spacer(modifier = Modifier.height(10.dp))
                  TextField(value = option2, onValueChange = {
                     option2 = it
                  },
                     placeholder = { Text(text = "option2") }
                  )
                  Spacer(modifier = Modifier.height(10.dp))
                  TextField(value = option3, onValueChange = {
                     option3 = it
                  },
                     placeholder = { Text(text = "option3") }
                  )
                  Spacer(modifier = Modifier.height(10.dp))
                  TextField(value = option4, onValueChange = {
                     option4 = it
                  },
                     placeholder = { Text(text = "option4") }
                  )
                  Spacer(modifier = Modifier.height(10.dp))
                  TextField(value = answer, onValueChange = {
                     answer = it
                  },
                     placeholder = { Text(text = "answer") }
                  )
                  Spacer(modifier = Modifier.height(20.dp))
                  Button(onClick = {
                     scope.launch(Dispatchers.IO) {
                        viewModel.insert(
                           QuestionModel.Question(
                              ques,
                              option1,
                              option2,
                              option3,
                              option4,
                              answer,
                              seriesname

                           )
                        ).collect {
                           when (it) {
                              is ResultState.Success<*> -> {
                                 ques = ""
                                 option1 = ""
                                 option2 = ""
                                 option3 = ""
                                 option4 = ""
                                 answer = ""
                                 isDialog = false
                                 isInsert.value = false
                                 isRefresh.value = true
                                 extentionsFunctions.showMessage(context, "${it.data}")

                              }

                              is ResultState.Failure -> {
                                 isDialog = false
                                 extentionsFunctions.showMessage(context, "${it.e}")
                              }

                              is ResultState.Loading -> {
                                 isDialog = true
                              }


                           }
                        }
                     }
                  }) {
                     Text(text = "Add")
                  }
               }
            },
            confirmButton = {

            }
         )
      }
      if (isRefresh.value) {
         isRefresh.value = false
         viewModel.getAllQues(seriesname)
      }

      if(res.data.isEmpty() && count==0)
      {
         viewModel.getAllQues(seriesname)
         count=count + 1
         Log.d("empty","------------")
      }
      if (res.data.isNotEmpty()) {
         LazyColumn(
            modifier = Modifier.fillMaxSize()
         ) {
            //this is the code to shuffle the list
//            Collections.shuffle(res.data)
            items(res.data, key = {

               it.key!!
            }) { ques ->
               EachRow1(ques = ques)
               Spacer(modifier = Modifier.padding(10.dp))
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
}

@Composable
fun EachRow1(ques: QuestionModel)
{
   Card(
      modifier = Modifier
         .fillMaxWidth(),
      shape = RoundedCornerShape(8.dp),
      elevation = CardDefaults.cardElevation(
         defaultElevation = 1.dp
      )
   )
   {
      Column(
         modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
      )
      {
         Text(
            text = ques.question?.ques!!,
            modifier = Modifier.padding(top = 10.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
         )
         Spacer(modifier = Modifier.height(10.dp))
         Text(
            text = ques.question?.option1!!,
            modifier = Modifier.padding(top = 10.dp),
            textAlign = TextAlign.Center
         )
         Spacer(modifier = Modifier.height(10.dp))
         Text(
            text = ques.question?.option2!!,
            modifier = Modifier.padding(top = 10.dp),
            textAlign = TextAlign.Center
         )
         Spacer(modifier = Modifier.height(10.dp))
         Text(
            text = ques.question?.option3!!,
            modifier = Modifier.padding(top = 10.dp),
            textAlign = TextAlign.Center
         )
         Spacer(modifier = Modifier.height(10.dp))
         Text(
            text = ques.question?.option4!!,
            modifier = Modifier.padding(top = 10.dp),
            textAlign = TextAlign.Center
         )
      }
   }
}
