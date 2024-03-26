package com.kloc.ktadmin.firestoredb.repository

import com.kloc.ktadmin.firestoredb.module.QuestionModel
import com.kloc.ktadmin.util.ResultState
import kotlinx.coroutines.flow.Flow

interface QuestionRepository
{
    fun insertQues(
        question: QuestionModel.Question
    ) : Flow<ResultState<String>>

    fun getAllQues(qtype:String) : Flow<ResultState<List<QuestionModel>>>

    fun deleteQues(key:String) : Flow<ResultState<String>>

    fun updateQues(
        question: QuestionModel
    ) : Flow<ResultState<String>>

}