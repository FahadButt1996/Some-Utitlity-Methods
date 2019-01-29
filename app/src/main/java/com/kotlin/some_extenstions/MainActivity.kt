package com.kotlin.some_extenstions

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.kotlin.some_extensions.AlertButtonsCallback
import com.kotlin.some_extensions.checkNetworkConnectivity
import com.kotlin.some_extensions.showAlertDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = checkNetworkConnectivity(this@MainActivity)
        showToast(data.toString())

        // Method to move to move between activities
//        textview.setOnClickListener{
//            navigate<SecondActivity>()
//        }
//


        showAlertDialog("Title", "Message", "OK", "Cancel",
            object : AlertButtonsCallback {
                override fun pressedButton(btn: String) {
                    showToast("You Pressed :: " + btn)
                }
            })
    }

    private fun showToast(value: String) {
        Toast.makeText(this@MainActivity, value, Toast.LENGTH_SHORT).show()
    }
}
