package com.kLoc.ktquizz1.presentation.admin

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kLoc.ktquizz1.R
import com.kLoc.ktquizz1.firestoredb.module.FirestoreModel
import com.kLoc.ktquizz1.firestoredb.viewmodel.FirestoreViewModel
import java.util.Collections

@Composable
fun ResultScreenContent(
    viewmodel: FirestoreViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDialog by remember { mutableStateOf(false) }
    val isUpdate = remember { mutableStateOf(false) }
    val res = viewmodel.res.value
    val isRefresh = remember { mutableStateOf(false) }
    if (res.data.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            //this is the code to shuffle the list
            Collections.shuffle(res.data)
            items(res.data, key = {
                it.key!!
            }) { user ->
                EachRow1(user = user)
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


@Composable
fun EachRow1(
    user: FirestoreModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(20.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(30.dp),
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
                Spacer(modifier = Modifier.padding(60.dp))
                Image(painterResource(id =R.drawable.coins) ,
                    contentDescription = "",
                    )
                Text(
                    text = (user.user?.coins.toString()), style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = user.user?.email!!, style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = user.user?.password!!, style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            )

        }
    }
}
