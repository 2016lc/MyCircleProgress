package com.example.admin.mycircleview

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_circleprogress.setOnClickListener {
            run {
                var intent = Intent(this, MyCircleProgress::class.java)
                startActivity(intent)
            }
        }

        btn_piechart.setOnClickListener {
            run {
                var intent = Intent(this, MyPieChart::class.java)
                startActivity(intent)
            }
        }

        btn_barchart.setOnClickListener {
            run {
                var intent = Intent(this, MyBarChartActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
