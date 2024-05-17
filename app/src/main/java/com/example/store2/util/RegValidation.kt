package com.example.store2.util

sealed class RegValidation {
    data object Success : RegValidation()
    data class Failed(val message: String): RegValidation()
}

data class RegFieldState(
    val email: RegValidation,
    val password: RegValidation
)