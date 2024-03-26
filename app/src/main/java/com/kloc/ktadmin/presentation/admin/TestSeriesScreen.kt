package com.kloc.ktadmin.presentation.admin

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.kloc.ktadmin.common.CommonDialog
import com.kloc.ktadmin.firestoredb.module.FireStoreModelSeries
import com.kloc.ktadmin.firestoredb.viewmodel.FireStoreSeriesViewModel
import com.kloc.ktadmin.util.ResultState
import com.kloc.ktadmin.R
import com.kloc.ktadmin.util.extentionsFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TestSeriesScreen(
    isInsert: MutableState<Boolean>,
    control: NavHostController,
    viewModel: FireStoreSeriesViewModel =  hiltViewModel()
    ) {
    var seriesname by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var isDialog by remember { mutableStateOf(false) }
    val isRefresh = remember { mutableStateOf(false) }
    val isUpdate = remember { mutableStateOf(false) }
    val res = viewModel.res.value
    val context = LocalContext.current
    val gradient= Brush.linearGradient(
        0.0f to Color(0xFFD9E2F3),
        500.0f to Color.Transparent,
        start = Offset.Zero,
        end = Offset.Infinite
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .padding(top = 48.dp, bottom = 48.dp),
        contentAlignment = Alignment.TopCenter
    ) {

        if (isDialog)
            CommonDialog()

        if (isInsert.value) {

            AlertDialog(onDismissRequest = { isInsert.value = false },
                title = {
                    Text(
                        text = "Add Series",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp).verticalScroll(
                                rememberScrollState()
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(value = seriesname, onValueChange = {
                            seriesname = it
                        },
                            label = {
                                Text(
                                    text = "Name",
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            },
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            textStyle = TextStyle(color = Color.Black),
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(value = image, onValueChange = {
                            image = it
                        },
                            label = {
                                Text(
                                    text = "ImageAddress",
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            },
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            textStyle = TextStyle(color = Color.Black),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = {
                            if(seriesname.trim().isNotEmpty()&& image.trim().isNotEmpty())
                            {
                                scope.launch(Dispatchers.IO) {
                                    viewModel.insert(
                                        FireStoreModelSeries.FirestoreSeries(
                                            seriesname.trim(),
                                            image.trim()

                                        )
                                    ).collect {
                                        when (it) {
                                            is ResultState.Success<*> -> {
                                                seriesname = ""
                                                image = ""
                                                isDialog = false
                                                isInsert.value = false
                                                isRefresh.value = true
                                                extentionsFunctions.showMessage(context, "series added successfully")

                                            }

                                            is ResultState.Failure -> {
                                                isDialog = false
                                                extentionsFunctions.showMessage(context, "failed to add series")
                                            }

                                            is ResultState.Loading -> {
                                                isDialog = true
                                            }


                                        }
                                    }
                                }
                            }
                            else{
                                extentionsFunctions.showMessage(context, "Please fill all the fields")
                            }
                        },
                            colors=ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4F84EE),
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "save",color=Color.White,modifier=Modifier.padding(5.dp), fontSize = 16.sp)
                        }
                    }
                },
                confirmButton = {

                },
                containerColor =Color(0xFFDCE3F1),
                icon = {
                    Image(painterResource(id = R.drawable.hill1), contentDescription ="" )
                }
            )
        }
        if (isRefresh.value) {
            isRefresh.value = false
            viewModel.getAllSeries()
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
//            Collections.shuffle(res.data)
                items(res.data, key = {

                    it.key!!
                }) { series ->
                    EachRow1(series = series,control, onUpdate = {
                        isUpdate.value = true
                        viewModel.setData(
                            FireStoreModelSeries(
                                key = series.key,
                                series = FireStoreModelSeries.FirestoreSeries(
                                    series.series?.seriesname,
                                    series.series?.imageVector
                                )
                            )
                        )
                    })
                    {
                        scope.launch {
                            viewModel.delete(series.key!!).collect {
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
                    Spacer(modifier = Modifier.padding(10.dp))
                }

            }

        }
        if (res.error.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(text = res.error)
            }
        }
        if (res.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun EachRow1(
    series: FireStoreModelSeries,
    control: NavHostController,
    onUpdate: () -> Unit = {},
    onDelete: () -> Unit = {}
)
{
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            onClick = {
                onUpdate()
            }
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
                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.SpaceBetween
                   ) {
                       Text(
                           text = series.series?.seriesname.toString(),
                           modifier = Modifier.padding(10.dp),
                           fontSize = 30.sp, color = Color.White
                       )
                       IconButton(onClick = {
                           onDelete()
                       }) {
                           Icon(Icons.Default.Delete, contentDescription = "", tint = Color.White)
                       }
                   }
                   Spacer(modifier = Modifier.height(100.dp))
                   Button(onClick = {
                     control.navigate("Question/${series.series?.seriesname.toString()}")
                   }) {
                       Text(text = "Add Quiz",
                           fontSize = 20.sp,color=Color.White
                       )
                   }

               }
            }
        }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Update(
    series: FireStoreModelSeries,
    isDialog: MutableState<Boolean>,
    viewModel: FireStoreSeriesViewModel,
    isRefresh: MutableState<Boolean>
) {

    var seriesName by remember { mutableStateOf(series.series?.seriesname) }
    var imageAddress by remember { mutableStateOf(series.series?.imageVector) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var progress by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = { isDialog.value = false },
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                androidx.compose.material3.Text(
                    text = "Update Series",
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
              OutlinedTextField(value = seriesName!!, onValueChange = {
                    seriesName = it
                },
                    placeholder = { androidx.compose.material3.Text(text = "series_name") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(value = imageAddress!!, onValueChange = {
                    imageAddress = it
                },
                    placeholder = { androidx.compose.material3.Text(text = "image_address") }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        },
        confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    scope.launch {
                        viewModel.update(
                            FireStoreModelSeries(
                                key = series.key,
                                series = FireStoreModelSeries.FirestoreSeries(
                                    seriesName,
                                    imageAddress
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
