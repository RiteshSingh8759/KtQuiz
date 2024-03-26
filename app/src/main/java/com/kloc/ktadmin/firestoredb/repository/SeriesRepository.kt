package com.kloc.ktadmin.firestoredb.repository

import com.kloc.ktadmin.firestoredb.module.FireStoreModelSeries
import com.kloc.ktadmin.util.ResultState
import kotlinx.coroutines.flow.Flow

interface SeriesRepository
{
    fun insertSeries(
        series: FireStoreModelSeries.FirestoreSeries
    ) : Flow<ResultState<String>>

    fun getAllSeries() : Flow<ResultState<List<FireStoreModelSeries>>>

    fun deleteSeries(key:String) : Flow<ResultState<String>>

    fun updateSeries(
        series: FireStoreModelSeries
    ) : Flow<ResultState<String>>
}