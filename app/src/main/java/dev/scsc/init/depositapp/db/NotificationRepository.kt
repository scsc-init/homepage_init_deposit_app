package dev.scsc.init.depositapp.db

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import dev.scsc.init.depositapp.R
import dev.scsc.init.depositapp.api.ApiService
import dev.scsc.init.depositapp.model.LoginRequest
import dev.scsc.init.depositapp.model.SendDepositRequest
import dev.scsc.init.depositapp.model.SendDepositResponse
import dev.scsc.init.depositapp.util.Util.Companion.convertCurrencyStringToLong
import dev.scsc.init.depositapp.util.Util.Companion.convertTimestampToISOString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotificationRepository(
    private val context: Context,
    private val rawDao: RawNotificationDao,
    private val processedDao: ParsedNotificationDao,
    private val sendDepositDao: SendDepositResultDao
) {
    private val mutex = Mutex()

    suspend fun insertRawNotification(notification: RawNotification) {
        mutex.withLock {
            rawDao.insert(notification)
        }
        withContext(Dispatchers.IO) {
            processAndStoreNotifications()
        }
    }

    suspend fun processAndStoreNotifications() {
        mutex.withLock {
            val rawNotifications = rawDao.getAll()

            // 데이터 정제 및 버퍼 적재 로직
            rawNotifications.forEach { notif ->
                if (notif.content != null && notif.content.contains("입금")) {
                    try {
                        val processedNotif = process(notif)
                        processedDao.insert(processedNotif)
                        rawDao.delete(notif)
                    } catch (e: Exception) {
                        Log.e("Repository", "Failed to process notification: ${notif.id}", e)
                    }
                } else {
                    rawDao.delete(notif)
                }
            }
        }
        withContext(Dispatchers.IO) {
            sendBufferToServer()
        }
    }

    suspend fun sendBufferToServer(): Boolean {
        mutex.withLock {
            val processedNotifications = processedDao.getAll()
            if (processedNotifications.isNotEmpty()) {
                var isSuccess = false // mark as true if any notif succeeded
                processedNotifications.forEach { notif ->
                    try {
                        withContext(Dispatchers.IO) {
                            val response = sendNotificationToServer(
                                context,
                                SendDepositRequest(
                                    notif.amount,
                                    notif.depositTime,
                                    notif.depositName
                                )
                            )
                            sendDepositDao.insert(
                                SendDepositResult(
                                    resultCode = response.result.resultCode,
                                    resultMsg = response.result.resultMsg,
                                    depositTime = response.result.record.depositTime,
                                    depositName = response.result.record.depositName,
                                    amount = response.result.record.amount
                                )
                            )
                        }
                        processedDao.delete(notif)
                        isSuccess = true
                    } catch (e: Exception) {
                        Log.e("Repository", "Failed to send notification: ${notif.id}", e)
                    }
                }
                return isSuccess
            }
        }
        return true
    }

    suspend fun getAllRawNotif(): List<RawNotification> {
        return mutex.withLock {
            rawDao.getAll()
        }
    }

    suspend fun getAllProcessedNotif(): List<ParsedNotification> {
        return mutex.withLock {
            processedDao.getAll()
        }
    }

    suspend fun getAllSendDepositResult(): List<SendDepositResult> {
        return mutex.withLock {
            sendDepositDao.getAll()
        }
    }

    suspend fun deleteSucceededResult() {
        mutex.withLock {
            sendDepositDao.deleteSucceeded()
        }
    }

    suspend fun deleteFailedResult() {
        mutex.withLock {
            sendDepositDao.deleteFailed()
        }
    }

    private fun process(notif: RawNotification): ParsedNotification {
        if (notif.title.isNullOrBlank() || notif.content.isNullOrBlank() || notif.timestamp == 0L) {
            throw IllegalStateException("901")
        }

        val titleTokens = notif.title.trim().split(Regex("\\s+"))
        if (titleTokens.isEmpty() || !titleTokens[0].startsWith("입금")) {
            throw IllegalStateException("902")
        }
        if (titleTokens.size < 2) {
            throw IllegalStateException("903")
        }
        val amount = convertCurrencyStringToLong(titleTokens[1])

        val textSplit = notif.content.split(' ')
        if (textSplit.size < 7) {
            throw IllegalStateException("903")
        }
        val depositName = textSplit[4]
        val amount2 = convertCurrencyStringToLong(textSplit[6])
        if (amount == null || amount2 == null || amount2 != amount) {
            throw IllegalStateException("903")
        }

        return ParsedNotification(
            depositTime = convertTimestampToISOString(notif.timestamp),
            depositName = depositName,
            amount = amount
        )
    }

    private suspend fun sendNotificationToServer(
        context: Context,
        reqDepositBody: SendDepositRequest
    ): SendDepositResponse {
        if (!isNetworkAvailable(context)) throw IllegalStateException("951")

        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.server_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        val email = context.getString(R.string.server_login_email)
        val apiSecret = context.getString(R.string.server_api_key)
        val reqLoginBody = LoginRequest(email)
        val resLogin = apiService.loginUser(apiSecret, reqLoginBody)

        if (!resLogin.isSuccessful) throw IllegalStateException("952")
        val loginResponse = resLogin.body()
        val jwt = loginResponse?.jwt
        if (jwt == null) throw IllegalStateException("953")

        val resDeposit = apiService.sendDeposit(apiSecret, jwt, reqDepositBody)
        val result = resDeposit.body()
        if (!resDeposit.isSuccessful || result == null) throw IllegalStateException("954")
        return result
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
}
