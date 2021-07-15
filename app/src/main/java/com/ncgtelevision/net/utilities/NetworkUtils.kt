package com.ncgtelevision.net.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network

class NetworkUtils(var context: Context) {
    // Network Check
    @SuppressLint("NewApi")
    fun registerNetworkCallback() {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    ConstantUtility.isNetworkConnected = true // Global Static Variable
                }

                override fun onLost(network: Network) {
                    ConstantUtility.isNetworkConnected = false // Global Static Variable
                }
            })
            ConstantUtility.isNetworkConnected = false
        } catch (e: Exception) {
            ConstantUtility.isNetworkConnected = false
        }
    }
}