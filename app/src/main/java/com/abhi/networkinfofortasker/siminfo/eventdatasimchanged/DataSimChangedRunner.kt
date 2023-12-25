package com.abhi.networkinfofortasker.siminfo.eventdatasimchanged

import android.content.Context
import com.abhi.networkinfofortasker.siminfo.SimInfo
import com.abhi.networkinfofortasker.utils.Convert
import com.joaomgcd.taskerpluginlibrary.condition.TaskerPluginRunnerConditionEvent
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultCondition
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultConditionSatisfied
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultConditionUnsatisfied

class DataSimChangedRunner :
    TaskerPluginRunnerConditionEvent<Unit, DataSimChangedOutput, SimInfo>() {
    override fun getSatisfiedCondition(
        context: Context,
        input: TaskerInput<Unit>,
        update: SimInfo?
    ): TaskerPluginResultCondition<DataSimChangedOutput> {
        return if (update != null) {
            val output = DataSimChangedOutput(Convert.convertToJson(update))
            TaskerPluginResultConditionSatisfied(context, output)
        } else TaskerPluginResultConditionUnsatisfied()
    }
}