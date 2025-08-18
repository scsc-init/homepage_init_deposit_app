package dev.scsc.init.depositapp.model

import com.google.gson.annotations.SerializedName

data class SendDepositRequest(
    val amount: Long,
    @SerializedName("deposit_time") val depositTime: String,
    @SerializedName("deposit_name") val depositName: String
)

data class SendDepositResponse(
    val result: ResultData
)


data class ResultData(
    @SerializedName("result_code") val resultCode: Int,
    @SerializedName("result_msg") val resultMsg: String,
    val record: DepositRecord,
    val users: List<User>
)

data class DepositRecord(
    val amount: Long,
    @SerializedName("deposit_time") val depositTime: String, // ISO 8601 string, consider converting to Instant/LocalDateTime if needed
    @SerializedName("deposit_name") val depositName: String
)

data class User(
    val id: String,
    val email: String,
    val name: String,
    @SerializedName("major_id") val majorId: Int
)
