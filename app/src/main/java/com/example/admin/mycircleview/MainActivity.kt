package com.example.admin.mycircleview

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cirView
            .setValue("哈哈哈", 200f)
            .setAnimTime(2000)
            .setIsGradient(true)
            .setGradientColors(intArrayOf(Color.parseColor("#ffa20f"),Color.parseColor("#ff7c0d"), Color.parseColor("#ffa20f")))
            .setSmallCircleEnable(true)
            .setShadowEnable(false)
            .setDigit(2)
    }
}
