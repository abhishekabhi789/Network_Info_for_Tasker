package com.abhi.networkinfofortasker.call.eventcalllogentry

import android.content.Context
import com.joaomgcd.taskerpluginlibrary.condition.TaskerPluginRunnerConditionEvent
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultCondition
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultConditionSatisfied
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultConditionUnsatisfied

class CallLogEntryEventRunner :
    TaskerPluginRunnerConditionEvent<Unit, CallLogEntryEventOutput, CallLogEntryEventOutput>() {
    override fun getSatisfiedCondition(
        context: Context,
        input: TaskerInput<Unit>,
        update: CallLogEntryEventOutput?
    ): TaskerPluginResultCondition<CallLogEntryEventOutput> {
        return if (update != null) TaskerPluginResultConditionSatisfied(
            context,
            CallLogEntryEventOutput(
                update.phoneNumber,
                update.callerName,
                update.callType,
                update.callTime,
                update.simIndex
            )
        ) else TaskerPluginResultConditionUnsatisfied()
    }
}