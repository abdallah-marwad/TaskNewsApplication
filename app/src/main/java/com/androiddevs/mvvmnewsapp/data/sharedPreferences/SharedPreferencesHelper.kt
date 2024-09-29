package com.androiddevs.mvvmnewsapp.data.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.androiddevs.mvvmnewsapp.application.MyApplication
import com.androiddevs.mvvmnewsapp.data.models.users.User
import com.androiddevs.mvvmnewsapp.utils.Constants
import com.google.gson.Gson


object SharedPreferencesHelper {
    private val preferences: SharedPreferences =
        MyApplication.myAppContext.getSharedPreferences("newsApp", Context.MODE_PRIVATE)

    fun addBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun addString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun getString(key: String): String {
        return preferences.getString(key, "") ?: ""
    }

    fun contain(key: String): Boolean {
        return preferences.contains(key)
    }
    fun saveUserData(user: User){
        val toJson = Gson().toJson(user)
        addString(Constants.USER_DATA, toJson)
    }
    fun getUser() : User{
        val jsonString = getString(Constants.USER_DATA)
        val gson = Gson()
        return gson.fromJson(jsonString, User::class.java)
    }
}