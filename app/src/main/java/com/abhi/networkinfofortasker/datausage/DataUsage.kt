package com.abhi.networkinfofortasker.datausage

data class DataUsage(
    val up: Long,
    val upFormatted: String,
    val down: Long,
    val downFormatted: String,
    val total: Long,
    val totalFormatted: String
){
    data class AppUsage(
    val appUid:Int,
    val appPackageName: String,
    val dataUsage: DataUsage,
)}
