package com.kloc.ktadmin.firestoredb.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.kloc.ktadmin.util.ResultState
import com.kloc.ktadmin.firestoredb.module.FirestoreModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreDbRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : FirestoreRepository {

    override fun insert(user: FirestoreModel.FirestoreUser): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.collection("user")
            .add(user)
            .addOnSuccessListener {
                trySend(ResultState.Success("Data is inserted with ${it.id}"))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun getUsers(): Flow<ResultState<List<FirestoreModel>>> =  callbackFlow{
        trySend(ResultState.Loading)
        db.collection("user")
            .get()
            .addOnSuccessListener {
                val users =  it.map { data->
                    FirestoreModel(
                        user = FirestoreModel.FirestoreUser(
                            name = data["name"] as String?,
                            email = data["email"] as String?,
                            password =data["password"] as String?,
                            coins = (data["coins"] as? Long)?.toInt()
                        ),
                        key = data.id
                    )
                }
                trySend(ResultState.Success(users))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.collection("user")
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

    override fun updateUser(user: FirestoreModel): Flow<ResultState<String>> = callbackFlow {
        try {

            val map = HashMap<String, Any>()
            map["name"] = user.user?.name!!
            map["email"] = user.user.email!!
            map["password"] = user.user.password!!
            map["coins"] = user.user.coins!!
            db.collection("user")
                .document(user.key!!)
                .update(map)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(ResultState.Success("Update successfully..."))
                    } else {
                        trySend(ResultState.Failure(task.exception ?: Exception("Unknown error")))
                    }
                    close()
                }
                .addOnFailureListener { e ->
                    trySend(ResultState.Failure(e))
                    close()
                }
        } catch (e: Exception) {
            trySend(ResultState.Failure(e))
            close()
        }

        awaitClose {}
    }

    override fun getUserByEmail(email: String): Flow<ResultState<FirestoreModel>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("user")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {
                val user =  it.map { data->
                    FirestoreModel(
                        user = FirestoreModel.FirestoreUser(
                            name = data["name"] as String?,
                            email = data["email"] as String?,
                            password =data["password"] as String?,
                            coins = (data["coins"] as? Long)?.toInt()
                        ),
                        key = data.id
                    )
                }
                trySend(ResultState.Success(user.first()))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }
}