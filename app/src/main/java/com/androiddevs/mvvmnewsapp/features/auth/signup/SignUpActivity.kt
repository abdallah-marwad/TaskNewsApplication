package com.androiddevs.mvvmnewsapp.features.auth.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.abdallah.ecommerce.utils.validation.isEmailValid
import com.abdallah.ecommerce.utils.validation.isNotEdEmpty
import com.abdallah.ecommerce.utils.validation.isPasswordsTheSame
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.data.models.users.User
import com.androiddevs.mvvmnewsapp.databinding.ActivitySignUpBinding
import com.androiddevs.mvvmnewsapp.features.shared.NewsActivity
import com.androiddevs.mvvmnewsapp.utils.Resource
import com.androiddevs.mvvmnewsapp.utils.security.PasswordHash
import com.tailors.doctoria.application.core.BaseActivityMVVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : BaseActivityMVVM<ActivitySignUpBinding, SignUpViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        activityOnClick()
        signUpCallBack()
    }

    private fun initViews() {
        binding.confirmPasswordEd.filledTextField.hint = getString(R.string.confirm_password)
    }

    private fun activityOnClick() {
        binding.signUpBtn.setOnClickListener {
            if(isDataValid().not())
                return@setOnClickListener
            viewModel.saveUser(getUserFromInputs())
        }
        binding.passwordEd.passwordEd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                binding.passwordEd.filledTextField.isPasswordVisibilityToggleEnabled = true
            }
        })
        binding.confirmPasswordEd.passwordEd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                binding.confirmPasswordEd.filledTextField.isPasswordVisibilityToggleEnabled = true
            }
        })
    }
    private fun getUserFromInputs(): User {
        return User(
            username = binding.userNameED.editText.text.toString(),
            email = binding.emailEd.editText.text.toString(),
            password = getHashedPass())
    }

    private fun getHashedPass(): String {
        return  PasswordHash().hashPassword(binding.passwordEd.passwordEd.text.toString())
    }

    private fun signUpCallBack() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userSavedFlow.collect { result ->
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

    private fun handleSuccessSignUp() {
        Toast.makeText(
            baseContext,
            resources.getString(R.string.successfully_sign_up),
            Toast.LENGTH_SHORT
        ).show()
        val intent =  Intent(this@SignUpActivity, NewsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    private fun isDataValid(): Boolean {
        var isValid  = true
        if(isNotEdEmpty(binding.userNameED.editText).not())
            isValid = false
        if(isEmailValid(binding.emailEd.editText).not())
            isValid = false
        if(isPasswordsTheSame(binding.passwordEd.passwordEd , binding.confirmPasswordEd.passwordEd ,
                binding.passwordEd.filledTextField , binding.confirmPasswordEd.filledTextField).not())
            isValid = false

        return isValid
    }
    override fun getViewModelClass(): Class<SignUpViewModel> {
        return SignUpViewModel::class.java
    }
}