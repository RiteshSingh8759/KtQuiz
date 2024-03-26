package com.kloc.ktadmin.firestoredb.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kloc.ktadmin.firestoredb.module.QuestionModel
import com.kloc.ktadmin.firestoredb.repository.QuestionRepository
import com.kloc.ktadmin.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FireStoreQuesViewModel @Inject constructor(
    private val quesRepo: QuestionRepository
) : ViewModel()
{
    private val _res: MutableState<FirestoreQuesState> = mutableStateOf(FirestoreQuesState())
    val res: State<FirestoreQuesState> = _res

    fun insert(question: QuestionModel.Question) = quesRepo.insertQues(question)

    private val _updateData: MutableState<QuestionModel> = mutableStateOf(
        QuestionModel(
            question  = QuestionModel.Question()
        )
    )
    val updateData: State<QuestionModel> = _updateData
    fun setData(data: QuestionModel){
        _updateData.value = data
    }

//    init {
//
//        getAllQues(seriesname)
//    }

    fun getAllQues(qtype:String) = viewModelScope.launch {
        quesRepo.getAllQues(qtype).collect {
            when (it) {
                is ResultState.Success -> {
                    _res.value = FirestoreQuesState(
                        data = it.data
                    )
                }
                is ResultState.Failure -> {
                    _res.value = FirestoreQuesState(
                        error = it.toString()
                    )
                }
                ResultState.Loading -> {
                    _res.value = FirestoreQuesState(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun delete(key:String) = quesRepo.deleteQues(key)
    fun update(question: QuestionModel) = quesRepo.updateQues(question)

}

data class FirestoreQuesState(
    val data:List<QuestionModel> = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false
)