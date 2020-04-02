package com.octored.wifiscanner.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.octored.wifiscanner.R
import kotlinx.android.synthetic.main.dialog_login.view.*
import java.lang.ClassCastException


class LoginDialog(val ssid: String) : DialogFragment() {

    lateinit var loggingListener: LoggingListener

    interface LoggingListener{
        fun onSignInClick(dialog: DialogFragment, ssid: String, password: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater

        val view = inflater.inflate(R.layout.dialog_login, null)

        view.dialog_idLabel_tv.text = ssid
        view.dialog_pass_et.requestFocus()

        builder.setView(view) // Add action buttons
            .setPositiveButton("Sign in",
                DialogInterface.OnClickListener { dialog, id ->
                   loggingListener.onSignInClick(this, view.dialog_idLabel_tv.text.toString(), view.dialog_pass_et.text.toString())
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->

                })


        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            loggingListener = context as LoggingListener
        }catch (e: ClassCastException){
            Log.e("Error", context.toString() + "Does not implement LoggingListener")
        }
    }
}