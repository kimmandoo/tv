package com.kimmandoo.tv

import java.io.Serializable

data class Movie(
    val id: Long,
    val title: String,
    val studio: String,
    val imageUri: String?,
    val description: String? = null,
    val cardImageUrl: String? = null,
//    val videoUrl: String? = null,
) : Serializable
