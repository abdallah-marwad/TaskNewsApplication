package com.androiddevs.mvvmnewsapp.features.auth.signin

import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.application.MyApplication
import com.androiddevs.mvvmnewsapp.data.db.UserDao
import com.androiddevs.mvvmnewsapp.data.models.users.User
import com.androiddevs.mvvmnewsapp.data.sharedPreferences.SharedPreferencesHelper
import com.androiddevs.mvvmnewsapp.utils.Constants
import com.androiddevs.mvvmnewsapp.utils.Resource
import com.tailors.doctoria.application.core.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userDao: UserDao
) : BaseViewModel() {
    private val _signInFlow =
        Channel<Resource<Boolean>>()
    val signInFlow: Flow<Resource<Boolean>> by lazy { _signInFlow.receiveAsFlow() }

    fun signIn(email: String , password :  String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getUserByEmailAndPassword(email , password)
            if (user != null) {
                _signInFlow.send(Resource.Success(true))
                SharedPreferencesHelper.addBoolean(Constants.SIGNED_IN , true)
                SharedPreferencesHelper.saveUserData(user)

                return@launch
            }
            _signInFlow.send(
                Resource.Error(
                    MyApplication
                .myAppContext.resources.getString(R.string.user_not_found)))
        }
    }
}