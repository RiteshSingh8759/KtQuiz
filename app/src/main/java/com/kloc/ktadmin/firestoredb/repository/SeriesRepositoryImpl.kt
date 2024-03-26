package com.kloc.ktadmin.firestoredb.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.kloc.ktadmin.firestoredb.module.FireStoreModelSeries
import com.kloc.ktadmin.util.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): SeriesRepository
{
    override fun insertSeries(series: FireStoreModelSeries.FirestoreSeries): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.collection("series")
            .add(series)
            .addOnSuccessListener {
                trySend(ResultState.Success("Data is inserted with ${it.id}"))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun getAllSeries(): Flow<ResultState<List<FireStoreModelSeries>>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("series")
            .get()
            .addOnSuccessListener {
                val series =  it.map { data->
                    FireStoreModelSeries(
                        series = FireStoreModelSeries.FirestoreSeries(
                            seriesname = data["seriesname"] as String?,
                            imageVector = data["imageVector"] as  String?
                        ),
                        key = data.id
                    )
                }
                trySend(ResultState.Success(series))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun deleteSeries(key: String): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.collection("series")
            .document(key)
            .delete()
            .addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(ResultState.Success("Deleted successfully.."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun updateSeries(series: FireStoreModelSeries): Flow<ResultState<String>>  = callbackFlow{
        trySend(ResultState.Loading)
        val map = HashMap<String,Any>()
        map["seriesname"] = series.series?.seriesname!!
        map["imageVector"] = series.series?.imageVector!!


        db.collection("series")
            .document(series.key!!)
            .update(map)
            .addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(ResultState.Success("Update successfully..."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

}