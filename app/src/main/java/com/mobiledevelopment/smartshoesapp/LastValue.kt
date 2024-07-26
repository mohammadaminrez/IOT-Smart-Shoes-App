package com.mobiledevelopment.smartshoesapp

data class LastValue(
    val value: Double,
    val timestamp: Long,
    val context: Map<String, Any>,
    val created_at: Long
)