package dev.scsc.init.depositapp.model

data class LoginRequest(
    val email: String
)

data class LoginResponse(
    val jwt: String
)
