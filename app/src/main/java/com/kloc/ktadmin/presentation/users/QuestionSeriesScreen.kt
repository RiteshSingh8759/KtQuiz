package com.kloc.ktadmin.presentation.users

import android.os.CountDownTimer
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController
import com.kloc.ktadmin.common.CommonDialog
import com.kloc.ktadmin.firestoredb.module.QuestionModel
import com.kloc.ktadmin.firestoredb.viewmodel.FireStoreQuesViewModel
import com.kloc.ktadmin.firestoredb.viewmodel.FirestoreViewModel
import com.kloc.ktadmin.presentation.profile.ProfileViewModel
import com.kloc.ktadmin.util.extentionsFunctions
import kotlinx.coroutines.launch

@Composable
fun QuestionSeriesScreen(
    seriesname: String,
    control: NavHostController,
    profileviewmodel: ProfileViewModel = hiltViewModel(),
    viewModel: FireStoreQuesViewModel = hiltViewModel(),
    userModel: FirestoreViewModel = hiltViewModel()
) {
    // Fetch questions for the given series name
    LaunchedEffect(seriesname) {
        viewModel.getAllQues(seriesname)
    }

    // Get the questions from the view model
    val res = viewModel.res.value

    // Shuffle the questions when they are loaded
    val shuffledQuestions = remember(res.data) {
        res.data.shuffled()
    }

    // State variables
    val selectedAnswers = remember { mutableStateOf(mutableMapOf<String, String>()) }
    val scope = rememberCoroutineScope()
    var isQuizCompleted by remember { mutableStateOf(false) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(20000L) } // 20 seconds timer for each question
    var isDialog by remember { mutableStateOf(false) }
    val isRefresh = remember { mutableStateOf(false) }
    val context = LocalContext.current
    DisposableEffect(currentQuestionIndex) {
        val timer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
            }

            override fun onFinish() {
                // If all questions are answered, set quiz as completed
                if (currentQuestionIndex == shuffledQuestions.size - 1) {
                    isQuizCompleted = true
                } else {
                    // Move to the next question when the timer finishes
                    currentQuestionIndex++
                    timeLeft = 20000L // Reset the timer for the next question
                }
            }
        }
        timer.start()
        onDispose {
            timer.cancel()
        }
    }


    // Use BackHandler to intercept back button presses
    BackHandler(enabled = true) {
        // Check if the user is attempting to navigate back without completing the quiz
        if (currentQuestionIndex < shuffledQuestions.size ) {
            extentionsFunctions.showMessage(context,"Please complete the quiz before navigating back.")
        } else {
            // Quiz is completed, navigate back normally
            control.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isDialog) {
            CommonDialog()
        }
        if (isRefresh.value) {
            isRefresh.value = false
            viewModel.getAllQues(seriesname)
        }
        if (shuffledQuestions.isNotEmpty()) {
            if (!isQuizCompleted) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    val question = shuffledQuestions[currentQuestionIndex]

                    Text(
                        text = "Time Remaining: ${timeLeft / 1000} seconds",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 16.dp))
                    EachRow1(
                        ques = question,
                        onOptionSelected = { questionKey, selectedOption ->
                            selectedAnswers.value[questionKey] = selectedOption // Update selected option in the map
                        },
                        selectedOption = selectedAnswers.value[question.key] ?: ""
                    )

                    Button(
                        onClick = {
                            // Move to the next question
                            if (currentQuestionIndex < shuffledQuestions.size - 1) {
                                currentQuestionIndex++
                                timeLeft = 20000L // Reset the timer for the next question
                            } else {
                                // If all questions are answered, set quiz as completed
                                isQuizCompleted = true
                            }
                        },
                        modifier = Modifier.padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Magenta,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50.dp),
                    ) {
                        Text(
                            text = "Next Question",
                            color = Color.White,
                            modifier = Modifier.padding(6.dp),
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                // Calculate total score
                var totalScore = 0
                for (question in shuffledQuestions) {
                    if (selectedAnswers.value[question.key] == question.question?.answer) {
                        totalScore++
                    }
                }

                // Display the review screen with the total score
                ReviewScreen(
                    res = res,
                    selectedAnswers = selectedAnswers.value,
                    onContinue = {
                        // Navigate to the result screen
                        scope.launch {
                            val email = profileviewmodel.currentUser.toString()
                            userModel.updateUserScore(email, totalScore)
                            control.navigate("resultant/$totalScore/${res.data.size}")
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun EachRow1(
    ques: QuestionModel,
    onOptionSelected: (String, String) -> Unit,
    selectedOption: String
) {
    val gradient = Brush.linearGradient(
        0.0f to Color(0xFFB9CBF1),
        500.0f to Color.Transparent,
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(.90f)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(
            modifier = Modifier
                .background(brush = gradient)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = ques.question?.ques ?: "",
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal),
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OptionButton(
                    text = ques.question?.option1 ?: "",
                    isSelected = selectedOption == ques.question?.option1,
                    onClick = {
                        onOptionSelected(ques.key ?: "", ques.question?.option1 ?: "")
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OptionButton(
                    text = ques.question?.option2 ?: "",
                    isSelected = selectedOption == ques.question?.option2,
                    onClick = {
                        onOptionSelected(ques.key ?: "", ques.question?.option2 ?: "")
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

              OptionButton(
                    text = ques.question?.option3 ?: "",
                    isSelected = selectedOption == ques.question?.option3,
                    onClick = {
                        onOptionSelected(ques.key ?: "", ques.question?.option3 ?: "")
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OptionButton(
                    text = ques.question?.option4 ?: "",
                    isSelected = selectedOption == ques.question?.option4,
                    onClick = {
                        onOptionSelected(ques.key ?: "", ques.question?.option4 ?: "")
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



//------------in this screen we are not showing the Questions with correct and wrong answers-------------------


//@SuppressLint("SuspiciousIndentation")
//@Composable
//fun QuestionSeriesScreen(
//    seriesname: String,
//    control: NavHostController,
//    profileviewmodel: ProfileViewModel = hiltViewModel(),
//    viewModel: FireStoreQuesViewModel = hiltViewModel(),
//    userModel: FirestoreViewModel = hiltViewModel()
//) {
//    var score by remember { mutableStateOf(0) }
//    var total by remember { mutableStateOf(0) }
//    val selectedAnswers = remember { mutableStateOf(mutableMapOf<String, String>()) }
//    val scope = rememberCoroutineScope()
//    var isDialog by remember { mutableStateOf(false) }
//    val isRefresh = remember { mutableStateOf(false) }
//    var count by remember { mutableStateOf(0) }
//    var progress by remember { mutableStateOf(false) }
//    val context= LocalContext.current
//    val res = viewModel.res.value
//    var currentQuestionIndex by remember { mutableStateOf(0) }
//    var timeLeft by remember { mutableStateOf(20000L) } // 20 seconds timer for each question
//
//    // Variable to track if the quiz is completed
//    var isQuizCompleted by remember { mutableStateOf(false) }
//
//    // Variable to track if the timer is running
//    DisposableEffect(currentQuestionIndex) {
//        val timer = object : CountDownTimer(timeLeft, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                timeLeft = millisUntilFinished
//            }
//
//            override fun onFinish() {
//                // Move to the next question when the timer finishes
//                if (currentQuestionIndex < res.data.size - 1) {
//                    currentQuestionIndex++
//                    timeLeft = 20000L // Reset the timer for the next question
//                } else {
//                    // If all questions are answered, navigate to the result screen
//                    scope.launch {
//                        val email = profileviewmodel.currentUser.toString()
//                        userModel.updateUserScore(email, score)
//                        control.navigate("resultant/$score/$total")
//                    }
//                }
//            }
//        }
//        timer.start()
//
//        onDispose {
//            timer.cancel()
//        }
//    }
//
//    // Use BackHandler to intercept back button presses
//    BackHandler(enabled = true) {
//        // Check if the user is attempting to navigate back without completing the quiz
//        if (currentQuestionIndex < res.data.size ) {
//            extentionsFunctions.showMessage(context,"Please complete the quiz before navigating back.")
//        } else {
//            // Quiz is completed, navigate back normally
//            control.popBackStack()
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        if (isDialog) {
//            CommonDialog()
//        }
//        if (isRefresh.value) {
//            isRefresh.value = false
//            viewModel.getAllQues(seriesname)
//        }
//        if (res.data.isEmpty() && count == 0) {
//            viewModel.getAllQues(seriesname)
//            count += 1
//        }
//
//        if (res.data.isNotEmpty()) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .verticalScroll(rememberScrollState())
//            ) {
//                val question = res.data[currentQuestionIndex]
//
//                Text(
//                    text = "Time Remaining: ${timeLeft / 1000} seconds",
//                    fontSize = 16.sp,
//                    color = Color.Black,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//                Spacer(modifier = Modifier.padding(vertical = 16.dp))
//                EachRow1(
//                    ques = question,
//                    onOptionSelected = { questionKey, selectedOption ->
//                        selectedAnswers.value[questionKey] = selectedOption // Update selected option in the map
//                    },
//                    selectedOption = selectedAnswers.value[question.key] ?: ""
//                )
//
//
//                Button(
//                    onClick = {
//                        // Calculate score
//                        val currentQuestion = res.data[currentQuestionIndex]
//                        if (selectedAnswers.value[currentQuestion.key] == currentQuestion.question?.answer) {
//                            score++
//                        }
//                        total++
//
//                        // Move to the next question
//                        if (currentQuestionIndex < res.data.size - 1) {
//                            currentQuestionIndex++
//                            timeLeft = 20000L // Reset the timer for the next question
//                        } else {
//                            // If all questions are answered, navigate to the result screen
//                            scope.launch {
//                                val email = profileviewmodel.currentUser.toString()
//                                userModel.updateUserScore(email, score)
//                                control.navigate("resultant/$score/$total")
//                            }
//                        }
//                        // Check if the quiz is completed
//                        isQuizCompleted = currentQuestionIndex >= res.data.size - 1
//                    },
//                    modifier = Modifier.padding(16.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.Magenta,
//                        contentColor = Color.White
//                    ),
//                    shape = RoundedCornerShape(50.dp),
//                ) {
//                    Text(
//                        text = "Next Question",
//                        color = Color.White,
//                        modifier = Modifier.padding(6.dp),
//                        fontSize = 16.sp
//                    )
//                }
//            }
//        }
//    }
//}






//------------------in one screen it will show all the Question--------------------
//@SuppressLint("SuspiciousIndentation")
//@Composable
//fun QuestionSeriesScreen(
//    seriesname: String,
//    control: NavHostController,
//    profileviewmodel: ProfileViewModel = hiltViewModel(),
//    viewModel:FireStoreQuesViewModel= hiltViewModel(),
//    userModel:FirestoreViewModel= hiltViewModel()
//) {
//    var score by remember { mutableStateOf(0) }
//    var total by remember { mutableStateOf(0)}
//    val selectedAnswers = remember { mutableStateOf(mutableMapOf<String, String>()) }
//    val scope = rememberCoroutineScope()
//    var isDialog by remember { mutableStateOf(false) }
//    val isRefresh = remember { mutableStateOf(false) }
//    var count by remember { mutableStateOf(0) }
//    var progress by remember { mutableStateOf(false) }
//
//    val res = viewModel.res.value
//    val context = LocalContext.current
//    var time by remember { mutableStateOf(300000L) }
//    DisposableEffect(Unit) {
//        val timer = object : CountDownTimer(time, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                time = millisUntilFinished
//            }
//
//            override fun onFinish() {
//                // Navigate to the Result Screen when the timer finishes
//                scope.launch {
////                    score = 0
////                    for ((key, value) in selectedAnswers.value) {
////                        if (value == res.data.shuffled().find { it.key == key }?.question?.answer) {
////                            score++
////                        }
////                    }
//                    val email = profileviewmodel.currentUser.toString()
//                    userModel.updateUserScore(email, score)
//                    control.navigate("resultant/$score/$total")
//                }
//            }
//        }
//        timer.start()
//
//        onDispose {
//            timer.cancel()
//        }
//    }
//    Box(Modifier.fillMaxSize()){
//        Image(
//            painterResource(id = R.drawable.back4),
//            contentDescription = null,
//            contentScale = ContentScale.FillBounds,
//            modifier = Modifier.fillMaxSize()
//        )
//        if (isDialog){
//            CommonDialog()
//        }
//        if (isRefresh.value) {
//            isRefresh.value = false
//            viewModel.getAllQues(seriesname)
//        }
//        if(res.data.isEmpty() && count==0)
//        {
//            viewModel.getAllQues(seriesname)
//            count += 1
//
//        }
//
//        if(res.data.isNotEmpty()) {
//            val shuffledData = remember { res.data.shuffled() }
//            Column(Modifier.padding(8.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Top)
//            {
//                Text(
//                    text = "Time Remaining: ${time / 60000}:${(time / 1000) % 60}",
//                    fontSize = 20.sp,
//                    color = Color.White
//                )
//                LazyColumn(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    total = res.data.size
////                Collections.shuffle(res.data)
//                    itemsIndexed(shuffledData) { index, ques ->
//                        EachRow1(
//                            ques = ques,
//                            onOptionSelected = { questionKey, selectedOption ->
//                                selectedAnswers.value[questionKey] = selectedOption // Update selected option in the map
//                            },
//                            selectedOption = selectedAnswers.value[ques.key] ?: ""
//                        )
//                        Spacer(modifier = Modifier.padding(10.dp))
//                    }
//                    item {
//                        Button(
//                            onClick = {
//                                // Calculate score
//                                score = 0
//                                for ((key, value) in selectedAnswers.value) {
//                                    if (value == shuffledData.find { it.key == key }?.question?.answer) {
//                                        score++
//                                    }
//                                }
//
//                                scope.launch {
//                                    val email = profileviewmodel.currentUser.toString()
//                                    userModel.updateUserScore(email, score)
//                                    // Navigate to result screen or perform other actions based on the score
//                                    control.navigate("resultant/$score/$total")
//                                }
//                            },
//                            modifier = Modifier
//                                .padding(16.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color.Magenta,
//                                contentColor = Color.White
//                            ),
//                            shape = RoundedCornerShape(50.dp),
////        enabled = selectedAnswers.value.size == res.data.size-1 // Enable button when all questions are answered
//                        ) {
//                            Text(
//                                text = "Submit Quiz",
//                                color = Color.White,
//                                modifier = Modifier.padding(10.dp),
//                                fontSize = 20.sp
//                            )
//                        }
//                    }
//                }
//            }
//
//        }
//    }
//}
