package com.example.pushnotification

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences( private val context: Context?) {

    private val sharedPreferences: SharedPreferences? = context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun loadData(key: String): String? {
        return sharedPreferences?.getString(key, null)
    }

    fun removeData(key: String) {
        val editor = sharedPreferences?.edit()
        editor?.remove(key)
        editor?.apply()
    }

    fun clearData() {
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
    }

}