package com.abhi.networkinfofortasker.siminfo.actioninfoquery

import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper

class SimInfoActionHelper(config: TaskerPluginConfig<SimInfoActionInput>) :
    TaskerPluginConfigHelper<SimInfoActionInput, SimInfoActionOutput, SimInfoActionRunner>(
        config
    ) {
    override val inputClass: Class<SimInfoActionInput>
        get() = SimInfoActionInput::class.java
    override val outputClass: Class<SimInfoActionOutput>
        get() = SimInfoActionOutput::class.java
    override val runnerClass: Class<SimInfoActionRunner>
        get() = SimInfoActionRunner::class.java

}