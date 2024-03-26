package com.kLoc.ktquizz1.presentation.users

import android.annotation.SuppressLint
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import com.kLoc.ktquizz1.common.CommonDialog
import com.kLoc.ktquizz1.firestoredb.module.FirestoreModel
import com.kLoc.ktquizz1.firestoredb.module.QuestionModel
import com.kLoc.ktquizz1.firestoredb.viewmodel.FireStoreQuesViewModel
import com.kLoc.ktquizz1.firestoredb.viewmodel.FirestoreViewModel
import com.kLoc.ktquizz1.presentation.profile.ProfileViewModel
import com.kLoc.ktquizz1.util.ResultState
import com.kLoc.ktquizz1.util.extentionsFunctions
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun QuestionSeriesScreen(
    seriesname: String,
    control: NavHostController,
    profileviewmodel: ProfileViewModel = hiltViewModel(),
    viewModel:FireStoreQuesViewModel= hiltViewModel(),
    userModel:FirestoreViewModel= hiltViewModel()
) {
    var score by remember { mutableStateOf(0) }
    val selectedAnswers = remember { mutableStateOf(mutableMapOf<String, String>()) }
    val scope = rememberCoroutineScope()
    var isDialog by remember { mutableStateOf(false) }
    val isRefresh = remember { mutableStateOf(false) }
    val isUpdate = remember { mutableStateOf(false) }
    var count by remember { mutableStateOf(0) }
    var progress by remember { mutableStateOf(false) }

    val res = viewModel.res.value
    val context = LocalContext.current

        if (isDialog){
            CommonDialog()
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

   if(res.data.isNotEmpty())
   {
       LazyColumn(
           modifier = Modifier.fillMaxSize()
       ) {
           items(res.data, key = { it.key!! }) { ques ->
               EachRow1(ques = ques) { selectedOption ->
                   selectedAnswers.value[ques.key!!] = selectedOption
               }
               Spacer(modifier = Modifier.padding(10.dp))
           }
       }
   }

    Button(
        onClick = {
            // Calculate score
            score = 0
            for ((key, value) in selectedAnswers.value) {
                if (value == res.data.find { it.key == key }?.question?.answer) {
                    score++
                }
            }
           scope.launch {
               val cuser: FirestoreModel? =userModel.getUserByEmail(profileviewmodel.currentUser.toString())
               if (cuser != null) {
                   Log.d("cuser",cuser.user?.email.toString())
                   userModel.update(
                       FirestoreModel(
                           key = cuser.key,
                           user = FirestoreModel.FirestoreUser(
                               cuser.user?.name,
                               cuser.user?.email,
                               cuser.user?.password,
                               score
                           )
                       )
                   ).collect {
                       when (it) {
                           is ResultState.Success<*> -> {
                               extentionsFunctions.showMessage(context,"${it.data}")
                               isDialog = false
                               progress = false
                               isRefresh.value = true
                           }

                           is ResultState.Failure -> {
                               extentionsFunctions.showMessage(context,"${it.e}")
                               isDialog = false
                               progress = false
                           }

                           is ResultState.Loading -> {
                               progress = true
                           }


                       }
                   }
               }

           }


            // Navigate to result screen or perform other actions based on the score
            control.navigate("resultant/$score")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
//        enabled = selectedAnswers.value.size == res.data.size-1 // Enable button when all questions are answered
    ) {
        Text(text = "Submit Quiz")
    }
}

@Composable
fun EachRow1(ques: QuestionModel, onOptionSelected: (String) -> Unit) {
    var selectedOption by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = ques.question?.ques ?: "",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OptionButton(
                text = ques.question?.option1 ?: "",
                isSelected = selectedOption == ques.question?.option1,
                onClick = {
                    selectedOption = ques.question?.option1 ?: ""
                    onOptionSelected(selectedOption)
                }
            )

            OptionButton(
                text = ques.question?.option2 ?: "",
                isSelected = selectedOption == ques.question?.option2,
                onClick = {
                    selectedOption = ques.question?.option2 ?: ""
                    onOptionSelected(selectedOption)
                }
            )

            OptionButton(
                text = ques.question?.option3 ?: "",
                isSelected = selectedOption == ques.question?.option3,
                onClick = {
                    selectedOption = ques.question?.option3 ?: ""
                    onOptionSelected(selectedOption)
                }
            )

            OptionButton(
                text = ques.question?.option4 ?: "",
                isSelected = selectedOption == ques.question?.option4,
                onClick = {
                    selectedOption = ques.question?.option4 ?: ""
                    onOptionSelected(selectedOption)
                }
            )
        }
    }
}

@Composable
fun OptionButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        enabled = !isSelected,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
            contentColor = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
}
//@Composable
//fun QuestionSeriesScreen(
//    seriesname: String,
//    control: NavHostController,
//    profileviewmodel: ProfileViewModel = hiltViewModel(),
//    viewModel:FireStoreQuesViewModel= hiltViewModel()
//    )
//{
//    val scope = rememberCoroutineScope()
//    var isDialog by remember { mutableStateOf(false) }
//    val isRefresh = remember { mutableStateOf(false) }
//    val isUpdate = remember { mutableStateOf(false) }
//    var count by remember { mutableStateOf(0) }
//    val res = viewModel.res.value
//    val context = LocalContext.current
//    Box( modifier = Modifier
//        .fillMaxSize()
//        .padding(20.dp)
//        .padding(top = 48.dp),
//        contentAlignment = Alignment.TopCenter)
//    {
//        if (isDialog)
//            CommonDialog()
//        if (isRefresh.value) {
//            isRefresh.value = false
//            viewModel.getAllQues(seriesname)
//        }
//        if(res.data.isEmpty() && count==0)
//        {
//            viewModel.getAllQues(seriesname)
//            count=count + 1
//            Log.d("empty","------------")
//        }
//        if (res.data.isNotEmpty()) {
//            LazyColumn(
//                modifier = Modifier.fillMaxSize()
//            ) {
//                //this is the code to shuffle the list
////            Collections.shuffle(res.data)
//                items(res.data, key = {
//
//                    it.key!!
//                }) { ques ->
//                    EachRow1(ques = ques)
//                    Spacer(modifier = Modifier.padding(10.dp))
//                }
//
//            }
//
//        }
//        if (res.error.isNotEmpty())
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 10.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(text = res.error)
//            }
//
//        if (res.isLoading)
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator()
//            }
//    }
//}
//
//@Composable
//fun EachRow1(ques: QuestionModel)
//{
//    Card(
//        modifier = Modifier
//            .fillMaxWidth(),
//        shape = RoundedCornerShape(8.dp),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 1.dp
//        )
//    )
//    {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp)
//        )
//        {
//            Text(
//                text = ques.question?.ques!!,
//                modifier = Modifier.padding(top = 10.dp),
//                textAlign = TextAlign.Center,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//            Text(
//                text = ques.question?.option1!!,
//                modifier = Modifier.padding(top = 10.dp),
//                textAlign = TextAlign.Center
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//            Text(
//                text = ques.question?.option2!!,
//                modifier = Modifier.padding(top = 10.dp),
//                textAlign = TextAlign.Center
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//            Text(
//                text = ques.question?.option3!!,
//                modifier = Modifier.padding(top = 10.dp),
//                textAlign = TextAlign.Center
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//            Text(
//                text = ques.question?.option4!!,
//                modifier = Modifier.padding(top = 10.dp),
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}