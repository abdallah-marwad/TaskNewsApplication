package com.abdallah.ecommerce.utils.validation

import android.telephony.PhoneNumberUtils
import android.util.Patterns
import android.widget.EditText
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.application.MyApplication
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

private const val MIN_PASSWORD_LENGTH = 6
fun isEmailValid( ed: EditText): Boolean {
    val email = ed.text.toString()
    return when {
        email.isEmpty() -> {
            ed.error = MyApplication.myAppContext.resources.getString(R.string.required_field)
            return false
        }

        Patterns.EMAIL_ADDRESS.matcher(email).matches().not() -> {
            ed.error = MyApplication.myAppContext.resources.getString(R.string.enter_vaild_email)
            return false
        }

        else -> true
    }
}
fun isNotEdEmpty( ed: EditText): Boolean {
    return when {
        ed.text.isEmpty() -> {
            ed.error = MyApplication.myAppContext.resources.getString(R.string.required_field)
            return false
        }
        else -> true
    }
}

fun isPasswordValid( ed: TextInputEditText , minDigits : Int = MIN_PASSWORD_LENGTH): Boolean {
    val password = ed.text.toString()
    return when {
        password.isEmpty() -> {
            ed.error = MyApplication.myAppContext.resources.getString(R.string.required_field)
            return false
        }

        password.length < minDigits -> {
            ed.error = MyApplication.myAppContext.resources.getString(R.string.password_validation)
            return false
        }

        else -> true
    }
}

fun isInputNotEmpty(ed: EditText, errTxt: String): Boolean {
    if (ed.text.toString().isEmpty()) {
        ed.error = errTxt
        return false
    }

    return true
}

fun isPasswordsTheSame(password: TextInputEditText, confirmPassword: EditText,
                        passwordLayout: TextInputLayout ,
                        confirmPasswordLayout: TextInputLayout ,
    ): Boolean {
    val resources = MyApplication.myAppContext.resources
    if(isInputNotEmpty(password , resources.getString(R.string.required_field)).not()) {
        passwordLayout.isPasswordVisibilityToggleEnabled = false
        return false
    }
    if(isPasswordValid(password).not()) {
        passwordLayout.isPasswordVisibilityToggleEnabled = false
        return false
    }
    if(isInputNotEmpty(confirmPassword , resources.getString(R.string.required_field)).not()) {
        confirmPasswordLayout.isPasswordVisibilityToggleEnabled = false
        return false
    }
    if(password.text.toString() != confirmPassword.text.toString()) {
        confirmPasswordLayout.isPasswordVisibilityToggleEnabled = false
        confirmPassword.error = resources.getString(R.string.password_should_be_the_same)
        return false
    }
    return true
}


