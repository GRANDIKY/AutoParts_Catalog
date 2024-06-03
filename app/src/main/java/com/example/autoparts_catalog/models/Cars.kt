package com.example.autoparts_catalog.models

import kotlinx.serialization.Serializable

@Serializable
data class Cars(
    val carID: String,
    val make: String,
    val name: String,
    val parts: List<String>,
    val year: Int
)
