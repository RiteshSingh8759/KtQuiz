package com.kloc.ktadmin.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.kloc.ktadmin.util.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override val currentUser get() = auth.currentUser
    override suspend fun firebaseSignUpWithEmailAndPassword(
        email: String, password: String
    ) = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        ResultState.Success(true)
    } catch (e: Exception) {
        ResultState.Failure(e)
    }

    override suspend fun sendEmailVerification() = try {
        auth.currentUser?.sendEmailVerification()?.await()
        ResultState.Success(true)
    } catch (e: Exception) {
        ResultState.Failure(e)
    }

    override suspend fun firebaseSignInWithEmailAndPassword(
        email: String, password: String
    ) = try {
        auth.signInWithEmailAndPassword(email, password).await()
        ResultState.Success(true)
    } catch (e: Exception) {
        ResultState.Failure(e)
    }

    override suspend fun reloadFirebaseUser() = try {
        auth.currentUser?.reload()?.await()
        ResultState.Success(true)
    } catch (e: Exception) {
        ResultState.Failure(e)
    }

    override suspend fun sendPasswordResetEmail(email: String) = try {
        auth.sendPasswordResetEmail(email).await()
        ResultState.Success(true)
    } catch (e: Exception) {
        ResultState.Failure(e)
    }

    override fun signOut() = auth.signOut()

    override suspend fun revokeAccess() = try {
        auth.currentUser?.delete()?.await()

        ResultState.Success(true)
    } catch (e: Exception) {
        ResultState.Failure(e)
    }

    override fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser == null)

//    override suspend fun isEmailAlreadyVerified(email: String): Boolean {
//        var status=false
//        repo.getUserByEmail(email).collect { result ->
//            when (result) {
//                is ResultState.Success -> {
//                    status = true
//                }
//
//                is ResultState.Failure -> {
//                    status = false
//                }
//
//                ResultState.Loading -> {
//                    Log.d("debug", "Loading user data...")
//                }
//            }
//        }
//        return status
//
//    }


}