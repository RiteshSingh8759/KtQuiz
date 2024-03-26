package com.kloc.ktadmin.firestoredb.module

data class FirestoreModel(
    val user: FirestoreUser?,
    val key:String? = ""
 )
{
    data class FirestoreUser(
        val name:String?="",
        val email:String?="",
        val password:String?="",
        var coins:Int?=0
    )
}