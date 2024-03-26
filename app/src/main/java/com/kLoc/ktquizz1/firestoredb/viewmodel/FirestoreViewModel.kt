package com.kLoc.ktquizz1.firestoredb.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import com.kLoc.ktquizz1.firestoredb.module.FirestoreModel
import com.kLoc.ktquizz1.firestoredb.repository.FirestoreRepository
import com.kLoc.ktquizz1.util.ResultState
import kotlinx.coroutines.flow.first

@HiltViewModel
class FirestoreViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel(){

    private val _res: MutableState<FirestoreState> = mutableStateOf(FirestoreState())
    val res:State<FirestoreState> = _res

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
    fun update(user: FirestoreModel) = repo.update(user)
    suspend fun getUserByEmail(email:String): FirestoreModel? {
        val resultState = repo.getUserByEmail(email).first()
        return when (resultState) {
            is ResultState.Success -> resultState.data
            else -> null // Handle failure or loading states accordingly
        }
    }
}

data class FirestoreState(
    val data:List<FirestoreModel> = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false
)
