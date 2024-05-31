package com.my.application.financify.models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.my.application.financify.AppContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ModelImp:Model {
    private var initCoroutine: Boolean = false

        override suspend fun startRequest(urlStringText: String): String? = suspendCoroutine { contAviaFreeFlying ->
            CoroutineScope(Dispatchers.IO).launch {
                initCoroutine=true

                val client = OkHttpClient()

                val url = URL(urlStringText)
                val request = Request.Builder()
                    .url(url)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        contAviaFreeFlying.resume(response.body?.string())
                    }
                })

            }
        }

    override fun checkInternet(): Boolean {
        val connectivityManager = AppContext.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNet = connectivityManager.activeNetwork
        if (activeNet != null) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNet)
            return networkCapabilities != null && networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        return false
    }
}