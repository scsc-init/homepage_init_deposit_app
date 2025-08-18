package dev.scsc.init.depositapp.model

/**
 * Notification snapshot persisted locally.
 * @param postTime Epoch milliseconds.
 */
data class NotificationDTO(
    val packageName: String,
    val title: String,
    val text: String,
    val postTime: Long,
    val amount: Long,
    val depositName: String,
    val resultCode: Int
)