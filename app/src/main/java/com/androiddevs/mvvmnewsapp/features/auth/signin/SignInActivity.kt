package com.androiddevs.mvvmnewsapp.features.auth.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.abdallah.ecommerce.utils.validation.isEmailValid
import com.abdallah.ecommerce.utils.validation.isPasswordValid
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.databinding.ActivitySignInBinding
import com.androiddevs.mvvmnewsapp.features.auth.signup.SignUpActivity
import com.androiddevs.mvvmnewsapp.features.shared.NewsActivity
import com.androiddevs.mvvmnewsapp.utils.Resource
import com.androiddevs.mvvmnewsapp.utils.security.PasswordHash
import com.tailors.doctoria.application.core.BaseActivityMVVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : BaseActivityMVVM<ActivitySignInBinding , SignInViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOnClick()
        signInCallBack()
    }

    private fun activityOnClick() {
        binding.signup.setOnClickListener {
            startActivity(Intent(this , SignUpActivity::class.java))
        }
        binding.signInBtn.setOnClickListener {
            if(isDataValid().not())
                return@setOnClickListener
            viewModel.signIn(binding.emailArea.editText.text.toString() ,getHashedPass())
        }
    }
    private fun signInCallBack() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signInFlow.collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            hideProgressDialog()
                            handleSuccessSignUp()
                        }
                        is Resource.Error -> {
                            hideProgressDialog()
                            showDialogWithMsg(result.message)
                        }

                        is Resource.Loading -> {
                            showProgressDialog()
                        }
                    }
                }
            }
        }
    }
    private fun getHashedPass(): String {
        return  PasswordHash().hashPassword(binding.passwordEd.passwordEd.text.toString())
    }

    private fun handleSuccessSignUp() {
        Toast.makeText(
            baseContext,
            resources.getString(R.string.successfully_sign_up),
            Toast.LENGTH_SHORT
        ).show()
        val intent =  Intent(this, NewsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    private fun isDataValid(): Boolean {
        var isValid  = true
        if(isEmailValid(binding.emailArea.editText).not())
            isValid = false
        if(isPasswordValid(binding.passwordEd.passwordEd ).not())
            isValid = false

        return isValid
    }
    override fun getViewModelClass(): Class<SignInViewModel> {
        return SignInViewModel::class.java
    }
}