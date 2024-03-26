package com.kloc.ktadmin.firestoredb.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.kloc.ktadmin.firestoredb.module.QuestionModel
import com.kloc.ktadmin.util.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : QuestionRepository
{
    override fun insertQues(question: QuestionModel.Question): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("questions")
            .add(question)
            .addOnSuccessListener {
                trySend(ResultState.Success("Data is inserted with ${it.id}"))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun getAllQues(qtype:String): Flow<ResultState<List<QuestionModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("questions")
           .whereEqualTo("qtype", qtype)
            .get()
            .addOnSuccessListener {
                val question =  it.map { data->
                    QuestionModel(
                        question = QuestionModel.Question(
                            ques = data["ques"] as String?,
                            option1  = data["option1"] as  String?,
                            option2  = data["option2"] as  String?,
                            option3  = data["option3"] as  String?,
                            option4  = data["option4"] as  String?,
                            answer  = data["answer"] as  String?,
                            qtype  = data["qtype"] as  String?,
                        ),
                        key = data.id
                    )
                }
                trySend(ResultState.Success(question))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun deleteQues(key: String): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.collection("questions")
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

    override fun updateQues(question: QuestionModel): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        val map = HashMap<String,Any>()
        map["ques"] = question.question?.ques!!
        map["option1"] = question.question?.option1!!
        map["option2"] = question.question?.option2!!
        map["option3"] = question.question?.option3!!
        map["option4"] = question.question?.option4!!
        map["answer"] = question.question?.answer!!
        map["qtype"] = question.question?.qtype!!
        db.collection("questions")
            .document(question.key!!)
            .update(map)
            .addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(ResultState.Success("Update successfully..."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }
}