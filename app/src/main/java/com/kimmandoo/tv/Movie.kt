package com.kimmandoo.tv

import java.io.Serializable

data class Movie(
    val id: Long,
    var title: String,
    var studio: String,
    val imageUri: String?,
    val description: String? = null,
    val cardImageUrl: String? = null,
    val videoUrl: String? = null,
) : Serializable
