package com.tailors.doctoria.application.core

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.androiddevs.mvvmnewsapp.application.MyApplication
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.data.sharedPreferences.SharedPreferencesHelper
import com.androiddevs.mvvmnewsapp.features.auth.signin.SignInActivity
import com.androiddevs.mvvmnewsapp.utils.Constants
import com.androiddevs.mvvmnewsapp.utils.dialogs.AppDialog
import com.androiddevs.mvvmnewsapp.utils.dialogs.ProgressDialog
import com.google.android.material.snackbar.Snackbar
import java.lang.reflect.ParameterizedType

/*
    created at 01/01/2024
    by Abdallah Marwad
    abdallahshehata311as@gmail.com
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    protected val binding by lazy { initBinding() }
    private val progressDialog by lazy { ProgressDialog() }
    protected fun showProgressDialog() {
        progressDialog.showProgressDialog(requireActivity())
    }

    protected fun hideProgressDialog() {
        progressDialog.dismissProgress()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

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
        message: String?, imgId : Int = R.drawable.err_img,
        scaleType: ScaleType = ScaleType.CENTER_INSIDE,
        cancelable: Boolean = true, onClick : () -> Unit ={}

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
    protected fun enterAnimationDeprecated() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            requireActivity().overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }
    protected fun logout() {
        val dialog = AppDialog()
        dialog.showDialog(
            requireActivity(),
            resources.getString(R.string.log_out),
            resources.getString(R.string.are_you_sure_to_logout),
            resources.getString(R.string.Yes),
            resources.getString(R.string.cancel),
            R.drawable.login,
            {
                SharedPreferencesHelper.remove(Constants.SIGNED_IN)
                SharedPreferencesHelper.remove(Constants.USER_DATA)
                val intent =  Intent(requireActivity(), SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            },
            { dialog.dismiss() }
        )
    }
}