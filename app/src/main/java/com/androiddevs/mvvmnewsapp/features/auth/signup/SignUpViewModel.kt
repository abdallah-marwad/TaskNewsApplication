package com.androiddevs.mvvmnewsapp.features.auth.signup

import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.application.MyApplication
import com.androiddevs.mvvmnewsapp.data.db.UserDao
import com.androiddevs.mvvmnewsapp.data.models.users.User
import com.androiddevs.mvvmnewsapp.data.sharedPreferences.SharedPreferencesHelper
import com.androiddevs.mvvmnewsapp.utils.Constants
import com.androiddevs.mvvmnewsapp.utils.Resource
import com.google.gson.Gson
import com.tailors.doctoria.application.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userDao: UserDao
) : BaseViewModel() {
    private val _userSavedFlow =
        Channel<Resource<Boolean>>()
    val userSavedFlow: Flow<Resource<Boolean>> by lazy { _userSavedFlow.receiveAsFlow() }

    fun saveUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val userID = userDao.insertNewUser(user)
            if (userID >= 0) {
                _userSavedFlow.send(Resource.Success(true))
                SharedPreferencesHelper.addBoolean(Constants.SIGNED_IN , true)
                SharedPreferencesHelper.saveUserData(user)
                return@launch
            }
            _userSavedFlow.send(Resource.Error(MyApplication
                .myAppContext.resources.getString(R.string.user_founded)))
        }
    }
}