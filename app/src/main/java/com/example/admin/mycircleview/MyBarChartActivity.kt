package com.example.admin.mycircleview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_barchart.*

/**
 * Author:LC
 * Date:2018/12/5
 * Description:This is MyBarChartActivity
 */
class MyBarChartActivity: AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barchart)

        val mList: List<BarChartData> = listOf(
            BarChartData("哈哈", 12.35f),
            BarChartData("嘻嘻", 3.36f),
            BarChartData("呵呵", 190.96f),
            BarChartData("啧啧", 7f),
            BarChartData( "弟弟", 100f),
            BarChartData( "哥哥", 65f),
            BarChartData("哈哈", 12f),
            BarChartData("嘻嘻", 3f),
            BarChartData("呵呵", 190f),
            BarChartData("啧啧", 7f),
            BarChartData( "弟弟", 100f),
            BarChartData( "哥哥", 65f),
            BarChartData("哈哈", 12f),
            BarChartData("嘻嘻", 3f),
            BarChartData("呵呵", 190f),
            BarChartData("啧啧", 7f),
            BarChartData( "弟弟", 100f),
            BarChartData( "哥哥", 65f),
            BarChartData("哈哈", 12f),
            BarChartData("嘻嘻", 3f),
            BarChartData("呵呵", 190f),
            BarChartData("啧啧", 7f),
            BarChartData( "弟弟", 100f),
            BarChartData( "哥哥", 65f),
            BarChartData("哈哈", 12f),
            BarChartData("嘻嘻", 3f),
            BarChartData("呵呵", 190f),
            BarChartData("啧啧", 7f),
            BarChartData( "弟弟", 100f),
            BarChartData( "哥哥", 65f),
            BarChartData("哈哈", 12f),
            BarChartData("嘻嘻", 3f),
            BarChartData("呵呵", 190f),
            BarChartData("啧啧", 7f),
            BarChartData( "弟弟", 100f),
            BarChartData( "哥哥", 65f),
            BarChartData("哈哈", 12f),
            BarChartData("嘻嘻", 3f),
            BarChartData("呵呵", 190f),
            BarChartData("啧啧", 7f),
            BarChartData( "弟弟", 100f),
            BarChartData( "哥哥", 65f),
            BarChartData("哈哈", 12f),
            BarChartData("嘻嘻", 3f),
            BarChartData("呵呵", 190f),
            BarChartData("啧啧", 7f),
            BarChartData( "弟弟", 100f),
            BarChartData( "哥哥", 65f),
            BarChartData("哈哈", 12f),
            BarChartData("呵呵", 190f),
            BarChartData("啧啧", 7f),
            BarChartData( "弟弟", 100f),
            BarChartData( "哥哥", 65f),
            BarChartData( "妹", 201f)
        )
        barchart.setData(mList)
    }
}