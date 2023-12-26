package com.abhi.networkinfofortasker.siminfo

enum class SimSlots(val id: String, val index: Int?, val label: String?) {
    SIM1("sim_slot_1", 0, "SIM 1"),
    SIM2("sim_slot_2", 1, "SIM 2"),
    DEFAULT_DATA("sim_slot_default_data", null, null)
}