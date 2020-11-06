package com.regera.justbreathe

import android.content.Intent
import android.graphics.drawable.AnimatedImageDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private lateinit var topAnim: Animation
    private lateinit var bottomAnim: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation)


        sloganImage.animation = topAnim
        slogan.animation = bottomAnim

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}