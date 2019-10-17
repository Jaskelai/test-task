package com.github.jaskelai.testtask.domain

data class BleDevice (
    val mac: String,
    val rssi: Int,
    val major: Int? = null,
    val minor: Int? = null,
    val distance: Double? = null
)
