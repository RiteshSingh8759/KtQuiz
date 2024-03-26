package com.kloc.ktadmin.repository

import com.google.firebase.auth.FirebaseUser
import com.kloc.ktadmin.util.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

typealias SignUpResponse = ResultState<Boolean>
typealias SendEmailVerificationResponse = ResultState<Boolean>
typealias SignInResponse = ResultState<Boolean>
typealias ReloadUserResponse = ResultState<Boolean>
typealias SendPasswordResetEmailResponse = ResultState<Boolean>
typealias RevokeAccessResponse = ResultState<Boolean>
typealias AuthStateResponse = StateFlow<Boolean>
interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun firebaseSignUpWithEmailAndPassword(email: String, password: String): SignUpResponse

    suspend fun sendEmailVerification(): SendEmailVerificationResponse

    suspend fun firebaseSignInWithEmailAndPassword(email: String, password: String): SignInResponse

    suspend fun reloadFirebaseUser(): ReloadUserResponse

    suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse

    fun signOut()

    suspend fun revokeAccess(): RevokeAccessResponse

    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse

//    suspend fun isEmailAlreadyVerified(email: String): Boolean

}