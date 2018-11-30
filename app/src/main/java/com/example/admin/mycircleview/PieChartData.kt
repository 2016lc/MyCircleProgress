package com.example.admin.mycircleview

/**
 * Author:LC
 * Date:2018/11/27
 * Description:This is PieChartData
 */
class PieChartData {

    var color: Int? = null
    var name: String? = null
    var num: Int? = null
    var unit:String?=null

    constructor(color: Int?, name: String?, num: Int?,unit:String?) {
        this.color = color
        this.name = name
        this.num = num
        this.unit=unit
    }
}