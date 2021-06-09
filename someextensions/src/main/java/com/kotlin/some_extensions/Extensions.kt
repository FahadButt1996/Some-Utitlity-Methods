package com.kotlin.some_extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

var mProgressDialog: ProgressDialog? = null

fun Activity.showDialog(message: String, title: String, cancelable: Boolean) {
    mProgressDialog?.setMessage(message)
    mProgressDialog?.setTitle(title)
    mProgressDialog?.setCancelable(cancelable)
    mProgressDialog?.setTitle(title)
    mProgressDialog?.show()
}

fun ViewGroup.inflateView(layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Activity.showAlertDialog(
    title: String, message: String,
    positive_btn_text: String, negative_btn_text: String, alertButtonsCallback: AlertButtonsCallback
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(positive_btn_text)
        { dialog, _ ->
            alertButtonsCallback.pressedButton(positive_btn_text)
            dialog.cancel()
        }
        .setNegativeButton(negative_btn_text,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    alertButtonsCallback.pressedButton(negative_btn_text)
                    dialog.cancel()
                }
            }
        ).show()
}

fun Activity.dismissDialog() {
    if (mProgressDialog != null) {
        mProgressDialog?.cancel()
    }
}

fun Activity.shareApp(subject: String, share_text: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.setType("text/plain")
    intent.putExtra(Intent.EXTRA_SUBJECT, "subject changed")
    intent.putExtra(Intent.EXTRA_TEXT, share_text)
    startActivity(Intent.createChooser(intent, "Please select"))
}

fun Activity.rateApp(context: Context, rate_app_url: String) {
    val myUrl = rate_app_url
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(myUrl)))
}

@SuppressLint("MissingPermission")
fun Activity.getDeviceID(): String? {
    var deviceId = ""
    if (ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val telephonyMgr = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            deviceId = telephonyMgr.imei
        } else {
            deviceId = telephonyMgr.deviceId
        }
    }
    return deviceId

}

fun getMacAddress(context: Context): String? {
    val manager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val info = manager.connectionInfo
    var address = info.macAddress
    if (address == null) {
        address = ""
    }
    return address
}

@SuppressLint("MissingPermission")
fun checkNetworkConnectivity(context: Context): Boolean {
    val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

inline fun <reified T : Activity> Activity.navigate(params: List<IntentParams> = emptyList()) {
    val intent = Intent(this, T::class.java)
    for (parameter in params) {
        intent.putExtra(parameter.key, parameter.value)
    }
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    startActivity(intent)
    finish()
}

fun appToolBar(
    activity: Context, toolbar: Toolbar?, isSetDisplayHomeAsUpEnabled: Boolean = true
    , isSetDisplayShowHomeEnabled: Boolean = true, isSetDisplayShowTitleEnabled: Boolean = false
): Unit {

    (activity as AppCompatActivity).setSupportActionBar(toolbar!!)
    activity.supportActionBar!!.setDisplayHomeAsUpEnabled(isSetDisplayHomeAsUpEnabled)
    activity.supportActionBar!!.setDisplayShowHomeEnabled(isSetDisplayShowHomeEnabled)
    activity.supportActionBar!!.setDisplayShowTitleEnabled(isSetDisplayShowTitleEnabled)
}