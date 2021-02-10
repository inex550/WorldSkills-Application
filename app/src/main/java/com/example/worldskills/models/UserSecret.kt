package com.example.worldskills.models

import java.io.Serializable

data class UserSecret(
        val login: String,
        val token: String,
        val id: Int
): Serializable