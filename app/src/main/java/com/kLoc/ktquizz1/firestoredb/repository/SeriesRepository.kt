package com.kLoc.ktquizz1.firestoredb.repository

import com.kLoc.ktquizz1.firestoredb.module.FireStoreModelSeries
import com.kLoc.ktquizz1.firestoredb.module.FirestoreModel
import com.kLoc.ktquizz1.util.ResultState
import kotlinx.coroutines.flow.Flow

interface SeriesRepository
{
    fun insertSeries(
        series:FireStoreModelSeries.FirestoreSeries
    ) : Flow<ResultState<String>>

    fun getAllSeries() : Flow<ResultState<List<FireStoreModelSeries>>>

    fun deleteSeries(key:String) : Flow<ResultState<String>>

    fun updateSeries(
        series: FireStoreModelSeries
    ) : Flow<ResultState<String>>
}