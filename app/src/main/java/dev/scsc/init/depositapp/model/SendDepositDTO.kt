package dev.scsc.init.depositapp.model

data class SendDepositRequest(
    val amount: Long,
    val deposit_time: String,
    val deposit_name: String
)

data class SendDepositResponse(
    val result: ResultData
)


data class ResultData(
    val result_code: Int,
    val result_msg: String,
    val record: DepositRecord,
    val users: List<User>
)

data class DepositRecord(
    val amount: Long,
    val deposit_time: String, // ISO 8601 string, consider converting to Instant/LocalDateTime if needed
    val deposit_name: String
)

data class User(
    val id: String,
    val email: String,
    val name: String,
    val major_id: Int
)
