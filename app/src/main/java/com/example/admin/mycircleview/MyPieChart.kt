package com.example.admin.mycircleview

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mypiechart.*

/**
 * Author:LC
 * Date:2018/11/27
 * Description:This is MyPieChart
 */
class MyPieChart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypiechart)
        val mList: List<PieChartData> = listOf(
            PieChartData(Color.parseColor("#0000FF"), "哈哈", 1,"人"),
            PieChartData(Color.parseColor("#8A2BE2"), "嘻嘻", 2,"人"),
            PieChartData(Color.parseColor("#A52A2A"), "呵呵", 3,"人"),
            PieChartData(Color.parseColor("#DEB887"), "啧啧", 4,"人"),
            PieChartData(Color.parseColor("#5F9EA0"), "弟弟", 5,"人"),
            PieChartData(Color.parseColor("#7FFF00"), "哥哥", 6,"人"),
            PieChartData(Color.parseColor("#D2691E"), "妹妹", 7,"人")
        )
        piechart.setData(mList)
        piechart.setType(PieChartType.NUM)
    }
}