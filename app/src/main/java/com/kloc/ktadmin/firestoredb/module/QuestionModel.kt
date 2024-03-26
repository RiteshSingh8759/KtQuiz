package com.kloc.ktadmin.firestoredb.module

data class QuestionModel (
    val question: Question?,
    val key:String? = ""
)
{
    data class Question(
        val ques:String?="",
        val option1:String?="",
        val option2:String?="",
        val option3:String?="",
        val option4:String?="",
        val answer:String?="",
        val qtype:String?=""
    )
}