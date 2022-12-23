package com.walterlauk.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object AppPref {
    private const val PREF_NAME = "user_data"
    private lateinit var sharedPreferences: SharedPreferences

    const val PREF_IS_LOGIN = "PREF_IS_LOGIN"
    const val TOKEN = "TOKEN"
    const val FULL_NAME = "FULL_NAME"
    const val USER_TYPE = "USER_TYPE"
    const val EMAIL = "EMAIL"
    const val PROFILE_IMAGE = "PROFILE_IMAGE"
    const val IS_PAID = "IS_PAID"
    const val LOGIN_ID = "LOGIN_ID"
    const val USER_NAME = "USER_NAME"
    const val SWIPE_STATUS = "SWIPE_STATUS"
    const val FAVOURITE_ID_DEFAULT = "FAVOURITE_ID_DEFAULT"
    const val DRIVER_TYPE = "DRIVER_TYPE"
    const val DRIVER_ID = "DRIVER_ID"
    const val MESSAGE_NODE ="MESSAGE NODE"
    const val IMAGE_URL ="IMAGE_URL"


    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)
    }

    fun getValue(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    fun getStringSet(key: String, stringSet: Set<String>): Set<String>? {
        return sharedPreferences.getStringSet(key, stringSet)
    }

    fun setValue(key: String, defaultValue: String?) {
        sharedPreferences.edit().putString(key, defaultValue).apply()
    }

    fun setValue(key: String, defaultValue: Int) {
        sharedPreferences.edit().putInt(key, defaultValue).apply()
    }

    fun setValue(key: String, defaultValue: Long) {
        sharedPreferences.edit().putLong(key, defaultValue).apply()

    }

    fun setValue(key: String, defaultValue: Boolean) {
        sharedPreferences.edit().putBoolean(key, defaultValue).apply()
    }

    fun setValue(key: String, defaultValue: Float) {
        sharedPreferences.edit().putFloat(key, defaultValue).apply()
    }

    fun setStringSet(key: String, stringSet: Set<String>) {
        sharedPreferences.edit().putStringSet(key, stringSet).apply()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}