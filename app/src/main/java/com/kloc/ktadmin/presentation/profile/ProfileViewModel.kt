package com.kloc.ktadmin.presentation.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kloc.ktadmin.firestoredb.repository.FirestoreRepository
import com.kloc.ktadmin.repository.AuthRepository
import com.kloc.ktadmin.repository.ReloadUserResponse
import com.kloc.ktadmin.repository.RevokeAccessResponse
import com.kloc.ktadmin.util.ResultState
import com.kloc.ktadmin.util.ResultState.Loading
import com.kloc.ktadmin.util.ResultState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val firestore: FirestoreRepository
): ViewModel() {
    var revokeAccessResponse by mutableStateOf<RevokeAccessResponse>(Success(false))
        private set
    var reloadUserResponse by mutableStateOf<ReloadUserResponse>(Success(false))
        private set

    fun reloadUser() = viewModelScope.launch {
        reloadUserResponse = Loading
        reloadUserResponse = repo.reloadFirebaseUser()
    }

    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false
    val currentUser get() = repo.currentUser?.email
    fun signOut() = repo.signOut()

    fun revokeAccess() = viewModelScope.launch {
        revokeAccessResponse = Loading
        repo.currentUser?.let { currentUser ->
            // Revoke access in FirebaseAuth
            revokeAccessResponse = repo.revokeAccess()
            // Get user data from Firestore
            firestore.getUserByEmail(currentUser.email.toString()).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val userData = result.data
                        val userKey = userData.key
                        // Delete user data from Firestore
                        if (userKey != null) {
                            firestore.delete(userKey).collect { deletionResult ->
                                when (deletionResult) {
                                    is ResultState.Success -> {
                                        Log.i("debug", "User data deleted successfully")
                                        // Handle any further actions after deletion
                                    }

                                    is ResultState.Failure -> {
                                        Log.e("error", "Failed to delete user data: ${deletionResult.e}")
                                        // Handle failure case
                                    }

                                    ResultState.Loading -> {
                                        // Handle loading state if needed
                                    }
                                }
                            }
                        }
                    }
                    is ResultState.Failure -> {
                        Log.e("error", "Failed to retrieve user data: ${result.e}")
                        // Handle failure case
                    }
                    ResultState.Loading -> {
                        // Handle loading state if needed
                    }
                }
            }
        } ?: run {
            // Handle the case where currentUser is null
            Log.e("Error", "Current user is null")
        }
    }


}