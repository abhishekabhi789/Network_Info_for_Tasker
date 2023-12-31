package com.abhi.networkinfofortasker.siminfo.actioninfoquery

import android.content.Context
import com.abhi.networkinfofortasker.siminfo.SimInfo
import com.abhi.networkinfofortasker.siminfo.SimInfoQuery
import com.abhi.networkinfofortasker.siminfo.SimSlots
import com.abhi.networkinfofortasker.utils.Convert
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultErrorWithOutput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess

class SimInfoActionRunner :
    TaskerPluginRunnerAction<SimInfoActionInput, SimInfoActionOutput>() {
    override fun run(
        context: Context,
        input: TaskerInput<SimInfoActionInput>
    ): TaskerPluginResult<SimInfoActionOutput> {
        val missingPermission = SimInfoQuery.requiredPermissions
            .filterNot { it.hasPermission(context) }
            .map { it.errorMessage }

        if (missingPermission.isNotEmpty()) {
            return TaskerPluginResultErrorWithOutput(
                1,
                missingPermission.joinToString("|")
            )
        }
        val simInfoQuery = SimInfoQuery()
        val dataMap = mutableMapOf<String, SimInfo?>()
        if (input.regular.chooseSimOne) dataMap[SimSlots.SIM1.name] =
            simInfoQuery.getSimDetails(context, SimSlots.SIM1.index!!)
        if (input.regular.chooseSimTwo) dataMap[SimSlots.SIM2.name] =
            simInfoQuery.getSimDetails(context, SimSlots.SIM2.index!!)
        if (input.regular.chooseDefaultData) dataMap[SimSlots.DEFAULT_DATA.name] =
            simInfoQuery.getDataSimOperator(context)
        return TaskerPluginResultSucess(SimInfoActionOutput(Convert.convertToJson(dataMap)))
    }
}