package dev.scsc.init.depositapp.model

data class LoginRequest(
    val email: String,
    val hashToken: String
)

data class LoginResponse(
    val jwt: String
)
