package dev.scsc.init.depositapp.util

import java.time.Instant
import java.time.format.DateTimeFormatter

class Util {
    companion object {
        fun convertCurrencyStringToLong(currencyString: String): Long? {
            return currencyString.replace(",", "").replace("원", "").toLongOrNull()
        }

        fun convertTimestampToISOString(timestampMilli: Long): String {
            val instant = Instant.ofEpochMilli(timestampMilli)
            return DateTimeFormatter.ISO_INSTANT.format(instant)
        }
    }
}
