package com.kLoc.ktquizz1.firestoredb.repository

import com.kLoc.ktquizz1.firestoredb.module.FireStoreModelSeries
import com.kLoc.ktquizz1.firestoredb.module.QuestionModel
import com.kLoc.ktquizz1.util.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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