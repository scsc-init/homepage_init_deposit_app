package dev.scsc.init.depositapp.notification

import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.scsc.init.depositapp.R
import dev.scsc.init.depositapp.api.ApiService
import dev.scsc.init.depositapp.db.NotificationContract
import dev.scsc.init.depositapp.db.NotificationReaderDbHelper
import dev.scsc.init.depositapp.model.LoginRequest
import dev.scsc.init.depositapp.model.SendDepositRequest
import dev.scsc.init.depositapp.util.Util.Companion.convertCurrencyStringToLong
import dev.scsc.init.depositapp.util.Util.Companion.convertTimestampToISOString
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UploadNotificationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        // Retrieve data passed from the service
        val packageName: String? = inputData.getString(KEY_PACKAGE_NAME)
        val title: String? = inputData.getString(KEY_TITLE)
        val text: String? = inputData.getString(KEY_TEXT)
        val postTime: Long = inputData.getLong(KEY_POST_TIME, 0)

        val resultReqDepositBody = parseNotification(packageName, title, text, postTime)
        var amount: Long? = null
        var depositName: String? = null
        var resultCode: Int? = null
        if (resultReqDepositBody.isSuccess) {
            val reqDepositBody = resultReqDepositBody.getOrThrow()
            resultCode = sendNotificationToServer(applicationContext, reqDepositBody)
            amount = reqDepositBody.amount
            depositName = reqDepositBody.deposit_name
        } else {
            resultCode = resultReqDepositBody.exceptionOrNull()?.message?.toIntOrNull()
        }


        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(NotificationContract.NotificationEntry.COLUMN_NAME_PACKAGE_NAME, packageName)
            put(NotificationContract.NotificationEntry.COLUMN_NAME_TITLE, title)
            put(NotificationContract.NotificationEntry.COLUMN_NAME_TEXT, text)
            put(NotificationContract.NotificationEntry.COLUMN_NAME_POST_TIME, postTime)
            put(NotificationContract.NotificationEntry.COLUMN_NAME_AMOUNT, amount)
            put(NotificationContract.NotificationEntry.COLUMN_NAME_DEPOSIT_NAME, depositName)
            put(NotificationContract.NotificationEntry.COLUMN_NAME_RESULT_CODE, resultCode)
        }

        return try {
            val dbHelper = NotificationReaderDbHelper(applicationContext)
            try {
                dbHelper.writableDatabase.use { db ->
                    // Insert the new row, returning the primary key value of the new row
                    val newRowId =
                        db.insert(NotificationContract.NotificationEntry.TABLE_NAME, null, values)
                    if (newRowId == -1L) {
                        Result.failure()
                    } else {
                        Result.success()
                    }
                }
            } finally {
                dbHelper.close()
            }
        } catch (_: android.database.sqlite.SQLiteDatabaseLockedException) {
            Result.failure()
        } catch (_: android.database.sqlite.SQLiteException) {
            Result.failure()
        }
    }

    private fun parseNotification(
        packageName: String?,
        title: String?,
        text: String?,
        postTime: Long
    ): kotlin.Result<SendDepositRequest> {
        return runCatching {
            if (packageName.isNullOrBlank() || title.isNullOrBlank() || text.isNullOrBlank() || postTime == 0L) {
                throw IllegalStateException("901")
            }

            val titleSplit = title.split(' ')
            val isDeposit = titleSplit[0] == "입금"
            val amount = convertCurrencyStringToLong(titleSplit[1])

            if (!isDeposit) {
                throw IllegalStateException("902")
            }

            val textSplit = text.split(' ')
            val depositName = textSplit[4]
            val amount2 = convertCurrencyStringToLong(textSplit[6])

            if (amount == null || amount2 == null || amount2 != amount) {
                throw IllegalStateException("903")
            }

            SendDepositRequest(
                amount,
                convertTimestampToISOString(postTime),
                depositName
            )
        }
    }

    private suspend fun sendNotificationToServer(
        context: Context,
        reqDepositBody: SendDepositRequest
    ): Int {
        if (!isNetworkAvailable(context)) return 951

        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.server_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        try {
            val email = context.getString(R.string.server_login_email)
            val apiSecret = context.getString(R.string.server_api_key)
            val reqLoginBody = LoginRequest(email)
            val resLogin = apiService.loginUser(apiSecret, reqLoginBody)

            if (!resLogin.isSuccessful) return 952
            val loginResponse = resLogin.body()
            val jwt = loginResponse?.jwt
            if (jwt == null) return 953

            val resDeposit = apiService.sendDeposit(apiSecret, jwt, reqDepositBody)

            if (!resDeposit.isSuccessful) return resDeposit.code()
            val resultCode = resDeposit.body()?.result?.result_code
            if (resultCode == null) return 954
            return resultCode
        } catch (_: Exception) {
            return 955
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false

        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    companion object {
        const val KEY_PACKAGE_NAME = "package_name"
        const val KEY_TITLE = "title"
        const val KEY_TEXT = "text"
        const val KEY_POST_TIME = "post_time"
    }
}
