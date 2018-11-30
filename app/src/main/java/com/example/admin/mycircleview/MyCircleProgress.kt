package com.example.admin.mycircleview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mycircleprogress.*


/**
 * Author:LC
 * Date:2018/11/27
 * Description:This is MyCircleProgress
 */
class MyCircleProgress: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycircleprogress)
        cirView
            .setValue("95", 100f)
           // .setAnimTime(6000)
           // .setIsGradient(true)
           // .setGradientColors(intArrayOf(Color.parseColor("#ffa20f"), Color.parseColor("#ff7c0d"), Color.parseColor("#ffa20f")))
           // .setSmallCircleEnable(true)
           // .setShadowEnable(false)
            .setDigit(2)
    }
}