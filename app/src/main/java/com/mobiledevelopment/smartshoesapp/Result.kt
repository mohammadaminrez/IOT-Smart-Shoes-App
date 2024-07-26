package com.mobiledevelopment.smartshoesapp

data class Result(
    val url: String,
    val id: String,
    val label: String,
    val name: String,
    val description: String,
    val device: Device,
    val tags: List<String>,
    val properties: Map<String, String>,
    val type: String,
    val unit: String?,
    val syntheticExpression: String,
    val createdAt: String,
    val lastValue: LastValue,
    val lastActivity: Long,
    val valuesUrl: String
)