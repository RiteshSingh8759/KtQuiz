package com.kloc.ktadmin.firestoredb.repository

import com.kloc.ktadmin.util.ResultState
import com.kloc.ktadmin.firestoredb.module.FirestoreModel
import kotlinx.coroutines.flow.Flow


interface FirestoreRepository {

    fun insert(
        user: FirestoreModel.FirestoreUser
    ) : Flow<ResultState<String>>

    fun getUsers() : Flow<ResultState<List<FirestoreModel>>>

    fun delete(key:String) : Flow<ResultState<String>>

    fun updateUser(
        user:FirestoreModel
    ) : Flow<ResultState<String>>
    fun getUserByEmail(email:String) : Flow<ResultState<FirestoreModel>>

}