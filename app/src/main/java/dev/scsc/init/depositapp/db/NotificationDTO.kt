package dev.scsc.init.depositapp.db

data class NotificationDTO(
    val packageName: String,
    val title: String,
    val text: String,
    val postTime: Long
) {
}