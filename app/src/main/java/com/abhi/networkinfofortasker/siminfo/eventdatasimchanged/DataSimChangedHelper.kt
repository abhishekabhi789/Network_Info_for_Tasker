package com.abhi.networkinfofortasker.siminfo.eventdatasimchanged

import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelperNoInput
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigNoInput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput

class DataSimChangedHelper(config: TaskerPluginConfigNoInput) :
    TaskerPluginConfigHelperNoInput<DataSimChangedOutput, DataSimChangedRunner>(
        config
    ) {
    override val outputClass: Class<DataSimChangedOutput>
        get() = DataSimChangedOutput::class.java
    override val runnerClass: Class<DataSimChangedRunner>
        get() = DataSimChangedRunner::class.java

    override fun addToStringBlurb(input: TaskerInput<Unit>, blurbBuilder: StringBuilder) {
        super.addToStringBlurb(input, blurbBuilder)
        blurbBuilder.append("This event will be available only on Android O (API 26) and above devices")
    }
}