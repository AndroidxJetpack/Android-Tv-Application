package com.ncgtelevision.net.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.ncgtelevision.net.R
import com.ncgtelevision.net.home_screen.HomeActivity
import com.ncgtelevision.net.local_storage.TokenStorage
import com.ncgtelevision.net.signin.SignInActivity
import com.ncgtelevision.net.utilities.CommonUtility
import com.ncgtelevision.net.utilities.NetworkUtils

class SplashActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageView:ImageView= findViewById(R.id.splash_logo)
        val animation1: Animation = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.fade_in
        )
        imageView.startAnimation(animation1)

        val network = NetworkUtils(applicationContext)
        network.registerNetworkCallback()

        Handler(Looper.myLooper()!!).postDelayed({
            if (CommonUtility.isStringEmpty(TokenStorage.readSharedToken(this@SplashActivity))) {
                startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            }
        }, 3000)

    }
}