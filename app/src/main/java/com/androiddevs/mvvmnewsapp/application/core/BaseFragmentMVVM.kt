package com.tailors.doctoria.application.core

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.androiddevs.mvvmnewsapp.application.MyApplication
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.utils.dialogs.AppDialog
import com.androiddevs.mvvmnewsapp.utils.dialogs.ProgressDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

/*
    created at 01/01/2024
    by Abdallah Marwad
    abdallahshehata311as@gmail.com
 */

abstract class BaseFragmentMVVM<VB : ViewBinding, VM : BaseViewModel> : Fragment() {
    protected val binding by lazy { initBinding() }
    protected lateinit var viewModel: VM
    private val progressDialog by lazy { ProgressDialog() }
    fun showProgressDialog() {
        progressDialog.showProgressDialog(requireActivity())
    }

    fun hideProgressDialog() {
        progressDialog.dismissProgress()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        noInternetCallBack()
        return binding.root
    }

    protected abstract fun getViewModelClass(): Class<VM>

    @Suppress("UNCHECKED_CAST")
    private fun initBinding(): VB {
        val superclass = javaClass.genericSuperclass as ParameterizedType
        val method = (superclass.actualTypeArguments[0] as Class<Any>)
            .getDeclaredMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    }

    protected fun showLongToast(msg: String) {
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_LONG
        ).show()
    }

    protected fun showShortToast(msg: String) {
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    protected fun showLongSnackBar(msg: String) {
        Snackbar.make(
            binding.root,
            msg,
            Snackbar.LENGTH_LONG
        ).show()
    }

    protected fun showShortSnackBar(msg: String) {
        Snackbar.make(
            binding.root,
            msg,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    protected fun showDialogWithMsg(
        message: String?, imgId: Int = R.drawable.err_img,
        scaleType: ScaleType = ScaleType.CENTER_INSIDE,
        cancelable: Boolean = true, onClick: () -> Unit = {}

    ) {
        val dialog = AppDialog()
        dialog.showDialog(
            requireActivity(),
            "",
            message,
            MyApplication.myAppContext.resources.getString(R.string.cancel),
            "",
            imgId,
            {
                dialog.dismiss()
                onClick()
            },
            {
                dialog.dismiss()
            },
            showNegativeBtn = false,
            cancelable = cancelable,
            scaleType
        )
    }

    protected open fun noInternetCallBack() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.noInternet.collect {
                    showShortSnackBar(resources.getString(R.string.chech_internet_connection))
                    Log.d("test", "noInternetCallBack parent fragment")
                }
            }
        }
    }

    protected fun enterAnimationDeprecated() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            requireActivity().overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }
}