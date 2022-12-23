package com.walterlauk.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtils {
    companion object {
        fun isInternetAvailable(ctx: Context?): Boolean {
            if (ctx == null) {
                return true
            }
            val mConMgr =
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return (mConMgr.activeNetworkInfo != null && mConMgr.activeNetworkInfo!!.isAvailable
                    && mConMgr.activeNetworkInfo!!.isConnected)
        }
    }
}