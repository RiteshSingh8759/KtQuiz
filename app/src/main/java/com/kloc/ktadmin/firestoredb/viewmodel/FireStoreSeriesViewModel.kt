package com.kloc.ktadmin.firestoredb.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kloc.ktadmin.firestoredb.module.FireStoreModelSeries
import com.kloc.ktadmin.firestoredb.repository.SeriesRepository
import com.kloc.ktadmin.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FireStoreSeriesViewModel @Inject constructor(
    private val seriesRepo: SeriesRepository
):ViewModel()
{
    private val _res: MutableState<FirestoreSeriesState> = mutableStateOf(FirestoreSeriesState())
    val res: State<FirestoreSeriesState> = _res

    fun insert(series: FireStoreModelSeries.FirestoreSeries) = seriesRepo.insertSeries(series)

    private val _updateData: MutableState<FireStoreModelSeries> = mutableStateOf(
        FireStoreModelSeries(
            series = FireStoreModelSeries.FirestoreSeries()
        )
    )
    val updateData: State<FireStoreModelSeries> = _updateData

    fun setData(data: FireStoreModelSeries){
        _updateData.value = data
    }

    init {
        getAllSeries()
    }

    fun getAllSeries() = viewModelScope.launch {
        seriesRepo.getAllSeries().collect{
            when(it){
                is ResultState.Success->{
                    _res.value = FirestoreSeriesState(
                        data = it.data
                    )
                }
                is ResultState.Failure->{
                    _res.value = FirestoreSeriesState(
                        error = it.toString()
                    )
                }
                ResultState.Loading->{
                    _res.value = FirestoreSeriesState(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun delete(key:String) = seriesRepo.deleteSeries(key)
    fun update(series: FireStoreModelSeries) = seriesRepo.updateSeries(series)

}

data class FirestoreSeriesState(
    val data:List<FireStoreModelSeries> = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false
)