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
            BarChartData("哈哈", 12f,"人"),
            BarChartData("嘻嘻", 3f,"人"),
            BarChartData("呵呵", 190f,"人"),
            BarChartData("啧啧", 7f,"人"),
            BarChartData( "弟弟", 100f,"人"),
            BarChartData( "哥哥", 65f,"人"),
            BarChartData("哈哈", 12f,"人"),
            BarChartData("嘻嘻", 3f,"人"),
            BarChartData("呵呵", 190f,"人"),
            BarChartData("啧啧", 7f,"人"),
            BarChartData( "弟弟", 100f,"人"),
            BarChartData( "哥哥", 65f,"人"),
            BarChartData("哈哈", 12f,"人"),
            BarChartData("嘻嘻", 3f,"人"),
            BarChartData("呵呵", 190f,"人"),
            BarChartData("啧啧", 7f,"人"),
            BarChartData( "弟弟", 100f,"人"),
            BarChartData( "哥哥", 65f,"人"),
            BarChartData("哈哈", 12f,"人"),
            BarChartData("嘻嘻", 3f,"人"),
            BarChartData("呵呵", 190f,"人"),
            BarChartData("啧啧", 7f,"人"),
            BarChartData( "弟弟", 100f,"人"),
            BarChartData( "哥哥", 65f,"人"),
            BarChartData("哈哈", 12f,"人"),
            BarChartData("嘻嘻", 3f,"人"),
            BarChartData("呵呵", 190f,"人"),
            BarChartData("啧啧", 7f,"人"),
            BarChartData( "弟弟", 100f,"人"),
            BarChartData( "哥哥", 65f,"人"),
            BarChartData("哈哈", 12f,"人"),
            BarChartData("嘻嘻", 3f,"人"),
            BarChartData("呵呵", 190f,"人"),
            BarChartData("啧啧", 7f,"人"),
            BarChartData( "弟弟", 100f,"人"),
            BarChartData( "哥哥", 65f,"人"),
            BarChartData("哈哈", 12f,"人"),
            BarChartData("嘻嘻", 3f,"人"),
            BarChartData("呵呵", 190f,"人"),
            BarChartData("啧啧", 7f,"人"),
            BarChartData( "弟弟", 100f,"人"),
            BarChartData( "哥哥", 65f,"人"),
            BarChartData("哈哈", 12f,"人"),
            BarChartData("嘻嘻", 3f,"人"),
            BarChartData("呵呵", 190f,"人"),
            BarChartData("啧啧", 7f,"人"),
            BarChartData( "弟弟", 100f,"人"),
            BarChartData( "哥哥", 65f,"人"),
            BarChartData("哈哈", 12f,"人"),
            BarChartData("嘻嘻", 3f,"人"),
            BarChartData("呵呵", 190f,"人"),
            BarChartData("啧啧", 7f,"人"),
            BarChartData( "弟弟", 100f,"人"),
            BarChartData( "哥哥", 65f,"人"),
            BarChartData( "妹妹", 3f,"人")
        )
        barchart.setData(mList)
    }
}