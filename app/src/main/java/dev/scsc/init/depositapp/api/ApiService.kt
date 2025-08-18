package dev.scsc.init.depositapp.api

import dev.scsc.init.depositapp.model.LoginRequest
import dev.scsc.init.depositapp.model.LoginResponse
import dev.scsc.init.depositapp.model.SendDepositRequest
import dev.scsc.init.depositapp.model.SendDepositResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/api/user/login")
    suspend fun loginUser(
        @Header("x-api-secret") apiSecret: String,
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("/api/executive/user/standby/process/deposit")
    suspend fun sendDeposit(
        @Header("x-api-secret") apiSecret: String,
        @Header("x-jwt") jwt: String,
        @Body request: SendDepositRequest
    ): Response<SendDepositResponse>
}