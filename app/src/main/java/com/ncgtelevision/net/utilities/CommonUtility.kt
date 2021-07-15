package com.ncgtelevision.net.utilities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import com.ncgtelevision.net.R
import java.util.regex.Pattern

class CommonUtility {
    companion object {
        val TAG = CommonUtility::class.java.simpleName

        @JvmStatic
        fun custom_loader(context: Context?): ProgressDialog {
            val dialog = ProgressDialog(context, R.style.MyProgressDialog)
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            dialog.setMessage(ConstantUtility.LOADING)
            return dialog
        }

        fun checkInternetConnection(context: Context?) {
            // Check network connection
            if (ConstantUtility.isNetworkConnected) {
                //todo when Internet has Connected
            } else {
                //todo when Internet do not Connect
                // Toast.makeText(context, ConstantUtility.CHECK_INTERNET_CONNECTION, Toast.LENGTH_SHORT).show();
            }
        }

        @JvmStatic
        fun showToast(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
        @JvmStatic
        fun isEmailValid(email : String?): Boolean {
            val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$"

            val pat: Pattern = Pattern.compile(emailRegex)
            return if (email == null) false else pat.matcher(email).matches()
        }
        @JvmStatic
        fun isStringEmpty(string : String?): Boolean {
            return string?.equals("") ?: true
        }
        /**
         * Setting animation when focus is lost
         */
        @JvmStatic
        fun focusOut(v: View, position: Int) {
            val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.05f, 1f)
            val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.05f, 1f)
            val set = AnimatorSet()
            set.play(scaleX).with(scaleY)
            set.start()
        }

        /**
         * Setting the animation when getting focus
         */
        @JvmStatic
        fun focusIn(v: View, position: Int) {
            val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.05f)
            val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.05f)
            val set = AnimatorSet()
            set.play(scaleX).with(scaleY)
            set.start()
        }
    }

    init {
        throw IllegalStateException("Utility class")
    }
}