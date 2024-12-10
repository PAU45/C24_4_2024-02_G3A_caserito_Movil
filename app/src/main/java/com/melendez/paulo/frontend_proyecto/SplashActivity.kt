package com.melendez.paulo.frontend_proyecto

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Apply fade-in animation
        val imageView: ImageView = findViewById(R.id.splash_image)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        imageView.startAnimation(fadeIn)

        // Delay for 3 seconds
        Handler().postDelayed({
            // Apply fade-out animation
            val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            imageView.startAnimation(fadeOut)

            // Start main activity after fade-out
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
        }, 3000) // 3000 milliseconds = 3 seconds
    }
}