package com.kLoc.ktquizz1.firestoredb.repository

import com.kLoc.ktquizz1.firestoredb.module.FirestoreModel
import com.kLoc.ktquizz1.util.ResultState
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    fun insert(
        user: FirestoreModel.FirestoreUser
    ) : Flow<ResultState<String>>

    fun getUsers() : Flow<ResultState<List<FirestoreModel>>>

    fun delete(key:String) : Flow<ResultState<String>>

    fun update(
        user:FirestoreModel
    ) : Flow<ResultState<String>>
    fun getUserByEmail(email:String) : Flow<ResultState<FirestoreModel>>
}