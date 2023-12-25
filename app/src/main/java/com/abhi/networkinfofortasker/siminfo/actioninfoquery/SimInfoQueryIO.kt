package com.abhi.networkinfofortasker.siminfo.actioninfoquery

import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable

@TaskerInputRoot
class SimInfoActionInput @JvmOverloads constructor(
    @field:TaskerInputField("choose_sim_one") val chooseSimOne: Boolean = false,
    @field:TaskerInputField("choose_sim_two") val chooseSimTwo: Boolean = false,
    @field:TaskerInputField("chose_default_data") val chooseDefaultData: Boolean = false
)

@TaskerInputRoot
@TaskerOutputObject()
class SimInfoActionOutput @JvmOverloads constructor(
    @field:TaskerInputField("sim_info") @get:TaskerOutputVariable("sim_info") val simInfo: String? = null
)