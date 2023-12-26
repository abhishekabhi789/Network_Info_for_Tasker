package com.abhi.networkinfofortasker.call.eventcalllogentry

import android.annotation.SuppressLint
import com.abhi.networkinfofortasker.R
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable

@TaskerInputRoot
@TaskerOutputObject()
class CallLogEntryEventOutput @JvmOverloads constructor(
    @SuppressLint("NonConstantResourceId") @field:TaskerInputField("cle_number") @get:TaskerOutputVariable(
        "cle_number",
        R.string.cle_label_phone_number, R.string.cle_html_label_phone_number
    ) val phoneNumber: String = "",
    @field:TaskerInputField("cle_name") @get:TaskerOutputVariable(
        "cle_name",
        R.string.cle_label_name,
        R.string.cle_html_label_name
    ) val callerName: String? = "",
    @field:TaskerInputField("cle_type") @get:TaskerOutputVariable(
        "cle_type",
        R.string.cle_label_type,
        R.string.cle_html_label_type
    ) val callType: String = "",
    @field:TaskerInputField("cle_time") @get:TaskerOutputVariable(
        "cle_time",
        R.string.cle_label_time,
        R.string.cle_html_label_time
    ) val callTime: Long = 0L,
    @field:TaskerInputField("cle_sim_index") @get:TaskerOutputVariable(
        "cle_sim_index",
        R.string.cle_label_sim_index,
        R.string.cle_html_label_sim_index
    ) val simIndex: Int? = 0
)
