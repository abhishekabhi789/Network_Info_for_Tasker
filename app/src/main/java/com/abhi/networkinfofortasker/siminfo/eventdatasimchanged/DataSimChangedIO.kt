package com.abhi.networkinfofortasker.siminfo.eventdatasimchanged

import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable

@TaskerInputRoot
@TaskerOutputObject()
class DataSimChangedOutput @JvmOverloads constructor(
    @field:TaskerInputField("data_sim_new") @get:TaskerOutputVariable("data_sim_new") val dataSim: String
)