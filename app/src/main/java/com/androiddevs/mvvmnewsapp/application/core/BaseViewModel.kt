package com.tailors.doctoria.application.core

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

/*
    created at 01/01/2024
    by Abdallah Marwad
    abdallahshehata311as@gmail.com
 */
@HiltViewModel
open class BaseViewModel @Inject constructor(
) : ViewModel() {
    protected val _noInternet by lazy { Channel<Boolean>() }
    val noInternet: Flow<Boolean> by lazy { _noInternet.receiveAsFlow() }

//    fun <T> fetchData(
//        call: Call<GeneralResponseWithGenericType<T?>>,
//        flow: Channel<Resource<T>>
//    ) {
//        if (!InternetConnection().hasInternetConnection(MyApplication.myAppContext)) {
//            viewModelScope.launch { _noInternet.send(true) }
//            return
//        }
//        viewModelScope.launch { flow.send(Resource.Loading()) }
//        call.enqueue(object : retrofit2.Callback<GeneralResponseWithGenericType<T?>> {
//            override fun onResponse(
//                call: Call<GeneralResponseWithGenericType<T?>>,
//                response: Response<GeneralResponseWithGenericType<T?>>
//            ) {
//                viewModelScope.launch {
//                    if (response.body() != null && response.body()!!.status == 200) {
//                        if (response.body()!!.data != null) {
//                            flow.send(
//                                Resource.Success(response.body()!!.data)
//                            )
//                        }
//                        return@launch
//                    }
//                    if (response.body() != null && response.body()!!.status == 401) {
//                        val errMsg = response.body()!!.message
//                        if (errMsg != null) {
//                            _unAuthorized.send(errMsg[0])
//                            return@launch
//                        }
//                    }
//                    if (response.body() != null && response.body()!!.status != 200) {
//                        val errMsg = response.body()!!.message
//                        if (errMsg != null) {
//                            errMsg.forEach { errMsg ->
//                                if (errMsg != null)
//                                    flow.send(Resource.Failure(errMsg))
//                            }
//                            return@launch
//                        }
//                    }
//                    try {
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.getCurrentAct()?.resources?.getString(R.string.some_err_happened)
//                            )
//                        )
//                    }catch (e : Exception){
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.resources.getString(R.string.some_err_happened)
//                            )
//                        )
//                    }
//
//                    return@launch
//                }
//            }
//            override fun onFailure(call: Call<GeneralResponseWithGenericType<T?>>, t: Throwable) {
//                viewModelScope.launch {
//                    if (t is IOException) {
//                        try {
//                            flow.send(
//                                Resource.Failure(
//                                    MyApplication.myAppContext.getCurrentAct()?.resources?.getString(
//                                        R.string.chech_internet_connection)
//                                )
//                            )
//                        }catch (e : Exception){
//                            flow.send(
//                                Resource.Failure(
//                                    MyApplication.myAppContext.resources.getString(R.string.chech_internet_connection)
//                                )
//                            )
//                        }
//
//                        return@launch
//                    }
//                    try {
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.getCurrentAct()?.resources?.getString(
//                                    R.string.some_err_happened)
//                            )
//                        )
//                    }catch (e : Exception){
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.resources.getString(R.string.some_err_happened)
//                            )
//                        )
//                    }
//                }
//            }
//        })
//    }
//    fun <T> postData(
//        call: Call<GeneralResponseWithGenericType<T?>>,
//        flow: Channel<Resource<T>>
//    ) {
//        if (!InternetConnection().hasInternetConnection(MyApplication.myAppContext)) {
//            viewModelScope.launch { _noInternet.send(true) }
//            return
//        }
//        viewModelScope.launch { flow.send(Resource.Loading()) }
//        call.enqueue(object : retrofit2.Callback<GeneralResponseWithGenericType<T?>> {
//            override fun onResponse(
//                call: Call<GeneralResponseWithGenericType<T?>>,
//                response: Response<GeneralResponseWithGenericType<T?>>
//            ) {
//                viewModelScope.launch {
//                    if (response.body() != null && response.body()!!.status == 200) {
//                            flow.send(
//                                Resource.Success(response.body()!!.data)
//                            )
//                        return@launch
//                    }
//                    if (response.body() != null && response.body()!!.status == 401) {
//                        val errMsg = response.body()!!.message
//                        if (errMsg != null) {
//                            _unAuthorized.send(errMsg[0])
//                            return@launch
//                        }
//                    }
//                    if (response.body() != null && response.body()!!.status != 200) {
//                        val errMsg = response.body()!!.message
//                        if (errMsg != null) {
//                            errMsg.forEach { errMsg ->
//                                if (errMsg != null)
//                                    flow.send(Resource.Failure(errMsg))
//                            }
//                            return@launch
//                        }
//                    }
//                    try {
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.getCurrentAct()?.resources?.getString(R.string.some_err_happened)
//                            )
//                        )
//                    }catch (e : Exception){
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.resources.getString(R.string.some_err_happened)
//                            )
//                        )
//                    }
//                    return@launch
//                }
//            }
//            override fun onFailure(call: Call<GeneralResponseWithGenericType<T?>>, t: Throwable) {
//                viewModelScope.launch {
//                    if (t is IOException) {
//                        try {
//                            flow.send(
//                                Resource.Failure(
//                                    MyApplication.myAppContext.getCurrentAct()?.resources?.getString(
//                                        R.string.chech_internet_connection)
//                                )
//                            )
//                        }catch (e : Exception){
//                            flow.send(
//                                Resource.Failure(
//                                    MyApplication.myAppContext.resources.getString(R.string.chech_internet_connection)
//                                )
//                            )
//                        }
//
//                        return@launch
//                    }
//                    try {
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.getCurrentAct()?.resources?.getString(
//                                    R.string.some_err_happened)
//                            )
//                        )
//                    }catch (e : Exception){
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.resources.getString(R.string.some_err_happened)
//                            )
//                        )
//                    }
//                }
//            }
//        })
//    }
//
//    protected fun <T> handleOnFailure(
//        t: Throwable,
//        flow: Channel<Resource<T>>
//    ) {
//        viewModelScope.launch {
//            if (t is IOException) {
//                flow.send(
//                    Resource.Failure(
//                        MyApplication.myAppContext.resources.getString(R.string.chech_internet_connection)
//                    )
//                )
//                return@launch
//            }
//            flow.send(
//                Resource.Failure(
//                    MyApplication.myAppContext.resources.getString(R.string.some_err_happened)
//                )
//            )
//        }
//
//    }
//    fun <T> fetchDataPaging(
//        call: Call<GeneralResponseWithGenericType<T?>>,
//        flow: Channel<Resource<GeneralResponseWithGenericType<T?>>>
//    ) {
//        if (!InternetConnection().hasInternetConnection(MyApplication.myAppContext)) {
//            viewModelScope.launch { _noInternet.send(true) }
//            return
//        }
//        viewModelScope.launch { flow.send(Resource.Loading()) }
//        call.enqueue(object : retrofit2.Callback<GeneralResponseWithGenericType<T?>> {
//            override fun onResponse(
//                call: Call<GeneralResponseWithGenericType<T?>>,
//                response: Response<GeneralResponseWithGenericType<T?>>
//            ) {
//                viewModelScope.launch {
//                    if (response.body() != null && response.body()!!.status == 200) {
//                        if (response.body()!!.data != null) {
//                            flow.send(
//                                Resource.Success(response.body())
//                            )
//                        }
//                        return@launch
//                    }
//                    if (response.body() != null && response.body()!!.status == 401) {
//                        val errMsg = response.body()!!.message
//                        if (errMsg != null) {
//                            _unAuthorized.send(errMsg[0])
//                            return@launch
//                        }
//                    }
//                    if (response.body() != null && response.body()!!.status != 200) {
//                        val errMsg = response.body()!!.message
//                        if (errMsg != null) {
//                            errMsg.forEach { errMsg ->
//                                if (errMsg != null)
//                                    flow.send(Resource.Failure(errMsg))
//                            }
//                            return@launch
//                        }
//                    }
//                    try {
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.getCurrentAct()?.resources?.getString(R.string.some_err_happened)
//                            )
//                        )
//                    }catch (e : Exception){
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.resources.getString(R.string.some_err_happened)
//                            )
//                        )
//                    }
//
//                    return@launch
//                }
//            }
//            override fun onFailure(call: Call<GeneralResponseWithGenericType<T?>>, t: Throwable) {
//                viewModelScope.launch {
//                    if (t is IOException) {
//                        try {
//                            flow.send(
//                                Resource.Failure(
//                                    MyApplication.myAppContext.getCurrentAct()?.resources?.getString(
//                                        R.string.chech_internet_connection)
//                                )
//                            )
//                        }catch (e : Exception){
//                            flow.send(
//                                Resource.Failure(
//                                    MyApplication.myAppContext.resources.getString(R.string.chech_internet_connection)
//                                )
//                            )
//                        }
//
//                        return@launch
//                    }
//                    try {
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.getCurrentAct()?.resources?.getString(
//                                    R.string.some_err_happened)
//                            )
//                        )
//                    }catch (e : Exception){
//                        flow.send(
//                            Resource.Failure(
//                                MyApplication.myAppContext.resources.getString(R.string.some_err_happened)
//                            )
//                        )
//                    }
//                }
//            }
//        })
//    }
}
