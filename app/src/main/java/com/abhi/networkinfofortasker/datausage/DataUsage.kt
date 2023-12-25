package com.abhi.networkinfofortasker.datausage

data class DataUsage(
    val bytesUp: String,
    val bytesUpFormatted: String,
    val bytesDown: String,
    val bytesDownFormatted: String,
    val bytesTotal: String,
    val bytesTotalFormatted: String
)