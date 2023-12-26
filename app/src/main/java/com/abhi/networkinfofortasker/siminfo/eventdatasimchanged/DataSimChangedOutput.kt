package com.abhi.networkinfofortasker.siminfo.eventdatasimchanged

import com.abhi.networkinfofortasker.R
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable

@TaskerInputRoot
@TaskerOutputObject()
class DataSimChangedOutput @JvmOverloads constructor(
    @field:TaskerInputField("data_sim_new") @get:TaskerOutputVariable(
        "data_sim_new",
        R.string.sc_label_new_sim,
        R.string.sc_html_label_new_sim
    ) val dataSim: String
)