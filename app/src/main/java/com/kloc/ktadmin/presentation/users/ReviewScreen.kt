package com.kloc.ktadmin.presentation.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kloc.ktadmin.firestoredb.viewmodel.FirestoreQuesState

@Composable
fun ReviewScreen(
    res: FirestoreQuesState,
    selectedAnswers: MutableMap<String, String>,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        for (question in res.data) {
            val userAnswer = selectedAnswers[question.key]
            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Question: ${question.question?.ques}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Correct Answer: ${question.question?.answer}",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Your Answer: ${userAnswer ?: "Skipped"}",
                        fontSize = 16.sp,
                        color = if (userAnswer == question.question?.answer) Color.Blue else Color.Red,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
        Button(
            onClick = onContinue,
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Magenta,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(50.dp),
        ) {
            Text(
                text = "Continue to Result",
                color = Color.White,
                modifier = Modifier.padding(6.dp),
                fontSize = 16.sp
            )
        }
    }
}
