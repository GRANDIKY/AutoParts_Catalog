package com.example.autoparts_catalog.models

import kotlinx.serialization.Serializable

@Serializable
data class Parts(
    val article: String = "",
    val carID: List<String> = emptyList(),
    val description: String = "",
    val image: String = "",
    val name: String = ""
)
