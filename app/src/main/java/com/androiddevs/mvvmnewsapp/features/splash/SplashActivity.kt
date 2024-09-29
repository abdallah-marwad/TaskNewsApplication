package com.androiddevs.mvvmnewsapp.features.splash

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.androiddevs.mvvmnewsapp.data.sharedPreferences.SharedPreferencesHelper
import com.androiddevs.mvvmnewsapp.databinding.ActivitySplashBinding
import com.androiddevs.mvvmnewsapp.features.auth.signin.SignInActivity
import com.androiddevs.mvvmnewsapp.features.shared.NewsActivity
import com.androiddevs.mvvmnewsapp.utils.Constants
import com.tailors.doctoria.application.core.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MySplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fadeIn = ObjectAnimator.ofFloat(binding.screenLogo, "alpha", 0f, 1f)
        fadeIn.duration = 1000
        fadeIn.start()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                delay(1300)
                if (SharedPreferencesHelper.getBoolean(Constants.SIGNED_IN)) {
                    startActivity(Intent(this@MySplashActivity, NewsActivity::class.java))
                    finish()
                    return@repeatOnLifecycle
                }
                startActivity(Intent(this@MySplashActivity, SignInActivity::class.java))
                finish()

            }
        }
    }
}