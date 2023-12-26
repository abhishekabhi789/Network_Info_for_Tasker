package com.abhi.networkinfofortasker.call.eventcalllogentry

import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelperNoInput
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigNoInput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput

class CallLogEntryEventHelper(config: TaskerPluginConfigNoInput) :
    TaskerPluginConfigHelperNoInput<CallLogEntryEventOutput, CallLogEntryEventRunner>(
        config
    ) {
    override val outputClass: Class<CallLogEntryEventOutput>
        get() = CallLogEntryEventOutput::class.java
    override val runnerClass: Class<CallLogEntryEventRunner>
        get() = CallLogEntryEventRunner::class.java

    override fun addToStringBlurb(input: TaskerInput<Unit>, blurbBuilder: StringBuilder) {
        super.addToStringBlurb(input, blurbBuilder)
        blurbBuilder.append("Will trigger whenever an entry is added to call log")
    }
}