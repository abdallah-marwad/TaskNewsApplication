package com.androiddevs.mvvmnewsapp.utils.security

import java.security.MessageDigest

class PasswordHash() {

    fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hashedBytes = md.digest(password.toByteArray())
        return hashedBytes.fold("") { str, byte -> str + "%02x".format(byte) }
    }

}