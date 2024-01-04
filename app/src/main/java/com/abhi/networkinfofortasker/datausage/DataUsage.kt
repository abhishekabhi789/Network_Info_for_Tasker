package com.abhi.networkinfofortasker.datausage

data class DataUsage(
    val up: String,
    val upFormatted: String,
    val down: String,
    val downFormatted: String,
    val total: String,
    val totalFormatted: String
)