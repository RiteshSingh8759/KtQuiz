package com.kLoc.ktquizz1.presentation.users

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.kLoc.ktquizz1.common.CommonDialog
import com.kLoc.ktquizz1.firestoredb.module.FireStoreModelSeries
import com.kLoc.ktquizz1.firestoredb.viewmodel.FireStoreSeriesViewModel
import com.kLoc.ktquizz1.presentation.profile.ProfileViewModel

@Composable
fun UserContentScreen(
    control: NavHostController,
    profileviewmodel:ProfileViewModel= hiltViewModel(),
    viewModel: FireStoreSeriesViewModel =  hiltViewModel()

)
{
    var isDialog by remember { mutableStateOf(false) }
    val isRefresh = remember { mutableStateOf(false) }
    val res = viewModel.res.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .padding(top = 48.dp),
        contentAlignment = Alignment.TopCenter
    )
    {
        if (isDialog) {
            CommonDialog()
        }
        if (isRefresh.value) {
            isRefresh.value = false
            viewModel.getAllSeries()
        }
        if (res.data.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                //this is the code to shuffle the list
//            Collections.shuffle(res.data)
                items(res.data, key = {

                    it.key!!
                }) { series ->
                    EachRow1(series = series,control)
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
fun EachRow1(series: FireStoreModelSeries, control: NavHostController)
{
    var nav by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
    {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(series.series?.imageVector),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Column {
                Text(
                    text = series.series?.seriesname.toString(),
                    modifier = Modifier.padding(10.dp),
                    fontSize = 30.sp, color = Color.White
                )
                Spacer(modifier = Modifier.height(100.dp))
                Button(onClick = {
                    control.navigate("QuestionSeries/${series.series?.seriesname.toString()}")
                }) {
                    Text(text = "Start Quiz",
                        fontSize = 20.sp,color= Color.White
                    )
                }

            }
        }
    }


}
