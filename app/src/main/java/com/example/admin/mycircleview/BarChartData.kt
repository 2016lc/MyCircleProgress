package com.example.admin.mycircleview

/**
 * Author:LC
 * Date:2018/11/27
 * Description:This is PieChartData
 */
class BarChartData {


    var name: String? = null
    var value: Float? = null
    var unit:String?=null
    constructor(name: String?, value: Float?,unit:String?) {
        this.name = name
        this.value = value
        this.unit=unit
    }
}