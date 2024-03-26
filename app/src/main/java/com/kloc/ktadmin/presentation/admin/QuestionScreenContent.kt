package com.kloc.ktadmin.presentation.admin

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kloc.ktadmin.common.CommonDialog
import com.kloc.ktadmin.firestoredb.module.QuestionModel
import com.kloc.ktadmin.firestoredb.viewmodel.FireStoreQuesViewModel
import com.kloc.ktadmin.util.ResultState
import com.kloc.ktadmin.util.extentionsFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun QuestionScreenContent(
   isInsert: MutableState<Boolean>,
   seriesname: String,
   viewModel: FireStoreQuesViewModel = hiltViewModel())
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
   val selectedAnswers = remember { mutableStateOf(mutableMapOf<String, String>()) }
   val context = LocalContext.current
   Box(
      modifier = Modifier
         .fillMaxSize()
         .padding(20.dp)
         .padding(bottom = 48.dp),
      contentAlignment = Alignment.TopCenter
   ) {
      if (isDialog)
         CommonDialog()

      if (isInsert.value) {

         AlertDialog(onDismissRequest = { isInsert.value = false },
            title = {
               Text(
                  text = "Add Question",
                  modifier = Modifier
                     .padding(vertical = 10.dp)
                     .fillMaxWidth(),
                  textAlign = TextAlign.Center,
                  fontWeight = FontWeight.Bold,
                  fontSize =20.sp,
               )
            },
            text = {
               Column(
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(horizontal = 20.dp, vertical = 20.dp)
                     .verticalScroll(
                        rememberScrollState()
                     ),
                  horizontalAlignment = Alignment.CenterHorizontally,

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
                    if(ques.trim().isNotEmpty()
                       &&option1.trim().isNotEmpty()
                       &&option2.trim().isNotEmpty()
                       &&option3.trim().isNotEmpty()
                       &&option4.trim().isNotEmpty()
                       &&answer.trim().isNotEmpty())
                    {
                       scope.launch(Dispatchers.IO) {
                          viewModel.insert(
                             QuestionModel.Question(
                                ques.trim(),
                                option1.trim(),
                                option2.trim(),
                                option3.trim(),
                                option4.trim(),
                                answer.trim(),
                                seriesname.trim()

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
                                   extentionsFunctions.showMessage(context, "question added in series")

                                }

                                is ResultState.Failure -> {
                                   isDialog = false
                                   extentionsFunctions.showMessage(context, "failed")
                                }

                                is ResultState.Loading -> {
                                   isDialog = true
                                }


                             }
                          }
                       }
                    }
                     else
                     {
                        extentionsFunctions.showMessage(context, "Please fill all the fields")
                     }
                  },
                     colors=ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4F84EE),
                        contentColor = Color.White
                     )
                     ) {
                     Text(text = "Add",color=Color.White,modifier=Modifier.padding(5.dp), fontSize = 16.sp)
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
      if (isUpdate.value)
         Update(
            viewModel.updateData.value,
            isUpdate,
            viewModel,
            isRefresh
         )
      if(res.data.isEmpty() && count==0)
      {
         viewModel.getAllQues(seriesname)
         count=count + 1
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
               EachRow1(ques = ques, onUpdate = {
                  isUpdate.value = true
                  viewModel.setData(
                     QuestionModel(
                        key = ques.key,
                        question = QuestionModel.Question(
                           ques.question?.ques,
                           ques.question?.option1,
                           ques.question?.option2,
                           ques.question?.option3,
                           ques.question?.option4,
                           ques.question?.answer,
                           ques.question?.qtype
                        )
                     )
                  )
               },
                  onDelete = {
                     scope.launch {
                        viewModel.delete(ques.key!!).collect {
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
                  ){ selectedOption ->
                  selectedAnswers.value[ques.key!!] = selectedOption
               }
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
fun EachRow1(
    ques: QuestionModel,
    onUpdate: () -> Unit = {},
    onDelete: () -> Unit = {},
    onOptionSelected: (String) -> Unit,
)
{
   var selectedOption by remember { mutableStateOf("") }
   val gradientBackground = Brush.verticalGradient(
      colors = listOf(Color(0xFFACCEEB), Color(0xFF69AEF1)),
      startY = 0f,
      endY = 200f
   )
   val gradient= Brush.linearGradient(
      0.0f to Color(0xFFB9CBF1),
      500.0f to Color.Transparent,
      start = Offset.Zero,
      end = Offset.Infinite
   )

   Card(
      modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 8.dp),
      shape = RoundedCornerShape(8.dp),
      elevation = CardDefaults.cardElevation(
         defaultElevation = 1.dp
      ),
      onClick = {
         onUpdate()
      }

   ) {
      Box(
         modifier = Modifier
            .background(brush = gradient) // Apply gradient background

      ) {
         Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
         ) {
            IconButton(onClick = {
               onDelete()
            }) {
               Icon(Icons.Default.Delete, contentDescription = "", tint = Color.Black)
            }
         }
         Spacer(modifier = Modifier.padding(8.dp))
         Column(
            modifier = Modifier.padding(32.dp)
         ) {
            Text(
               text = ques.question?.ques ?: "",
               style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal), // Bolden the question text
               textAlign = TextAlign.Start,
               fontSize = 18.sp,
               modifier = Modifier.padding(bottom = 16.dp) // Increase bottom padding for separation
            )

            Spacer(modifier = Modifier.height(8.dp)) // Add vertical space between question and options

            OptionButton(
               text = ques.question?.option1 ?: "",
               isSelected = selectedOption == ques.question?.option1,
               onClick = {
                  selectedOption = ques.question?.option1 ?: ""
                  onOptionSelected(selectedOption)
               }
            )

            Spacer(modifier = Modifier.height(8.dp)) // Add vertical space between options

            OptionButton(
               text = ques.question?.option2 ?: "",
               isSelected = selectedOption == ques.question?.option2,
               onClick = {
                  selectedOption = ques.question?.option2 ?: ""
                  onOptionSelected(selectedOption)
               }
            )

            Spacer(modifier = Modifier.height(8.dp)) // Add vertical space between options

            OptionButton(
               text = ques.question?.option3 ?: "",
               isSelected = selectedOption == ques.question?.option3,
               onClick = {
                  selectedOption = ques.question?.option3 ?: ""
                  onOptionSelected(selectedOption)
               }
            )

            Spacer(modifier = Modifier.height(8.dp)) // Add vertical space between options

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
         textAlign = TextAlign.Start
      )
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Update(
   ques: QuestionModel,
   isDialog: MutableState<Boolean>,
   viewModel: FireStoreQuesViewModel,
   isRefresh: MutableState<Boolean>
) {

   var question by remember { mutableStateOf(ques.question?.ques) }
   var option1 by remember { mutableStateOf(ques.question?.option1) }
   var option2 by remember { mutableStateOf(ques.question?.option2) }
   var option3 by remember { mutableStateOf(ques.question?.option3) }
   var option4 by remember { mutableStateOf(ques.question?.option4) }
   var answer by remember { mutableStateOf(ques.question?.answer) }
   var qtype by remember{mutableStateOf(ques.question?.qtype)}
   val scope = rememberCoroutineScope()
   val context = LocalContext.current
   var progress by remember { mutableStateOf(false) }

   AlertDialog(onDismissRequest = { isDialog.value = false },
      title = {
         Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            androidx.compose.material3.Text(
               text = "Update Question",
               modifier = Modifier.padding(vertical = 10.dp)
            )
         }
      },
      text = {
         Column(
            modifier = Modifier
               .fillMaxWidth()
               .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
         ) {
            androidx.compose.material3.TextField(value = question!!, onValueChange = {
               question = it
            },
               placeholder = { androidx.compose.material3.Text(text = "Question") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.TextField(value = option1!!, onValueChange = {
               option1 = it
            },
               placeholder = { androidx.compose.material3.Text(text = "option1") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.TextField(value = option2!!, onValueChange = {
               option2 = it
            },
               placeholder = { androidx.compose.material3.Text(text = "option2") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.TextField(value = option3!!, onValueChange = {
               option3 = it
            },
               placeholder = { androidx.compose.material3.Text(text = "option3") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.TextField(value = option4!!, onValueChange = {
               option4 = it
            },
               placeholder = { androidx.compose.material3.Text(text = "option4") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.TextField(value = answer!!, onValueChange = {
               answer = it
            },
               placeholder = { androidx.compose.material3.Text(text = "answer") }
            )
            Spacer(modifier = Modifier.height(8.dp))
         }
      },
      confirmButton = {
         Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(onClick = {
               scope.launch {
                  viewModel.update(
                     QuestionModel(
                        key = ques.key!!,
                        question= QuestionModel.Question(
                          ques = question,
                          option1 = option1,
                          option2 = option2,
                          option3 = option3,
                          option4 = option4,
                          answer = answer,
                          qtype = qtype
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
               androidx.compose.material3.Text(text = "Update")
            }
         }
      }
   )

   if (progress)
      CommonDialog()
}
