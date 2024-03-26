package com.kloc.ktadmin.firestoredb.module

data class FireStoreModelSeries(
    val series: FirestoreSeries?,
    val key:String? = ""
)
{
    data class FirestoreSeries(
        val seriesname:String?="",
        val imageVector:String?=""
    )
}