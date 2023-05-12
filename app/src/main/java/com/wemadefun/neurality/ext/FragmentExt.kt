package com.wemadefun.neurality.ext

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.wemadefun.neurality.R
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.ui.dialogs.InternetErrorDialog
import com.wemadefun.neurality.ui.dialogs.LoadingDialog

fun Fragment.easyToastShort(text: String) {
    Toast.makeText(requireActivity().applicationContext, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.easyToastLong(text: String) {
    Toast.makeText(requireActivity().applicationContext, text, Toast.LENGTH_LONG).show()
}

fun Fragment.openBrowser(url: String) {
    try {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    } catch (e: Exception) {
        easyToastLong(getString(R.string.msg_browser_error))
        FireCrashlytics.report(e)
    }
}

fun Fragment.hideKeyboard() {
    val view: View = requireActivity().currentFocus?: return
    val imm: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.isNetworkAvailable(): Boolean {
    var result = false
    val connectivityManager =
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }
    return result
}

fun getLoadingDialog(text: String): LoadingDialog {
    return LoadingDialog(text)
}

fun getConnectionErrorDialog(): InternetErrorDialog {
    return InternetErrorDialog()
}