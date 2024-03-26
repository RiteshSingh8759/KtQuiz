package com.kloc.ktadmin.firestoredb.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kloc.ktadmin.firestoredb.repository.FirestoreRepository
import com.kloc.ktadmin.util.ResultState
import com.kloc.ktadmin.firestoredb.module.FirestoreModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirestoreViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel(){

    private val _res: MutableState<FirestoreState> = mutableStateOf(FirestoreState())
    val res:State<FirestoreState> = _res
    private val _res1: MutableState<singleState> = mutableStateOf(singleState())
    val res1:State<singleState> = _res1
    fun insert(user: FirestoreModel.FirestoreUser) = repo.insert(user)


    private val _updateData: MutableState<FirestoreModel> = mutableStateOf(
        FirestoreModel(
            user = FirestoreModel.FirestoreUser()
        )
    )
    val updateData:State<FirestoreModel> = _updateData

    fun setData(data:FirestoreModel){
        _updateData.value = data
    }

    init {
        getUsers()
    }

    fun getUsers() = viewModelScope.launch {
        repo.getUsers().collect{
            when(it){
                is ResultState.Success->{
                    _res.value = FirestoreState(
                        data = it.data
                    )
                }
                is ResultState.Failure->{
                    _res.value = FirestoreState(
                        error = it.toString()
                    )
                }
                ResultState.Loading->{
                    _res.value = FirestoreState(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun delete(key:String) = repo.delete(key)
    fun update(user: FirestoreModel) = repo.updateUser(user)
    fun getUserByEmail(email:String)= viewModelScope.launch {
        repo.getUserByEmail(email).collect{
            when(it){
                is ResultState.Success->{
                    _res1.value = singleState(
                        data = it.data
                    )
                }
                is ResultState.Failure->{
                    _res1.value = singleState(
                        error = it.toString()
                    )
                }
                ResultState.Loading->{
                    _res1.value = singleState(
                        isLoading = true
                    )
                }
            }
        }
    }
    fun updateUserScore(email: String, score: Int) = viewModelScope.launch {
        repo.getUserByEmail(email).collect { result ->
            when (result) {
                is ResultState.Success -> {
                    val user = result.data
                    var prevCoin=user.user?.coins
                    val updatedUser = user.copy(
                        user = user.user?.copy(coins = (( score * 2)+ prevCoin!!))
                    )
                    repo.updateUser(updatedUser).collect { updateResult ->
                        // Handle the update result here
                        when (updateResult) {
                            is ResultState.Success -> {
                                Log.d("debug", "Update result: ${updateResult.data}")
                            }
                            is ResultState.Failure -> {
                                Log.e("debug", "Failed to update user: ${updateResult.e}")
                            }
                            ResultState.Loading -> {
                                Log.d("debug", "Updating user data...")
                            }
                        }
                    }
                }
                is ResultState.Failure -> {
                    Log.e("debug", "Failed to fetch user: ${result.e}")
                }
                ResultState.Loading -> {
                    Log.d("debug", "Loading user data...")
                }
            }
        }
    }

}

data class FirestoreState(
    val data:List<FirestoreModel> = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false
)
data class singleState(
    val data: FirestoreModel? = null,
    val error:String = "",
    val isLoading:Boolean = false
)
