package com.example.admin.mycircleview.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.admin.mycircleview.BarChartConstant
import com.example.admin.mycircleview.BarChartData
import com.example.admin.mycircleview.R
import com.example.admin.mycircleview.Util

/**
 * Author:LC
 * Date:2018/12/4
 * Description:This is 柱状图
 */
class MyBarChartView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mPaint_xy: Paint? = null//x y轴
    private var mPaint_line: Paint? = null//网格线
    private var mPaint_bar: Paint? = null//画柱子
    private var mPaint_text: TextPaint? = null//字体画笔
    private var mTextSize: Int? = null//字体大小
    private var mStrokeWidth: Int? = null//线宽度
    private var mWidth: Int? = null//控件宽
    private var mHeight: Int? = null//控件高
    private var mMargin: Int? = null//间距
    private var mTextMargin: Int? = null//x轴字体间距
    private var mMaxValue: Int? = null//集合最大值，用于y轴取值分段
    private var mSegment: Int = BarChartConstant.DEFAULT_SEGMENT//y轴分为几段
    private var mIsUnit: Boolean = BarChartConstant.DEFAULT_ISUNIT//是否显示单位
    private var mBarWidth: Float? = null//柱子的宽度
    private var mData: MutableList<BarChartData> = ArrayList()
    private var mRectF: RectF? = null
    private var leftMoving: Float = 0f
    private var lastPointX: Float = 0f
    private var movingLeftThisTime = 0f
    private var lineStartX: Float = 0f
    private var leftSpaceRect: RectF? = null
    private var rightSpaceRect: RectF? = null

    init {
        mMargin = Util.dip2px(context!!, 8f)
        mTextMargin = Util.dip2px(context, 35f)
        mStrokeWidth = Util.dip2px(context, 0.7f)
        mTextSize = Util.dip2px(context, 10f)
        mRectF = RectF()
        leftSpaceRect = RectF()
        rightSpaceRect = RectF()
        initAttrs(attrs, context)//初始化属性
        initPaint()//初始化画笔
    }

    private fun initAttrs(attrs: AttributeSet?, context: Context?) {
        val typedArray = context!!.obtainStyledAttributes(
            attrs,
            R.styleable.MyBarChartView
        )
        mBarWidth = typedArray.getDimension(R.styleable.MyBarChartView_barWidth, BarChartConstant.DEFAULT_BARWIDTH)
    }

    private fun initPaint() {
        mPaint_xy = Paint()
        mPaint_xy!!.isDither = true
        mPaint_xy!!.strokeWidth = mStrokeWidth!!.toFloat()
        mPaint_xy!!.color = resources.getColor(R.color.xy_color)
        mPaint_xy!!.style = Paint.Style.FILL

        mPaint_line = Paint()
        mPaint_line!!.isDither = true
        mPaint_line!!.strokeWidth = mStrokeWidth!!.toFloat()
        mPaint_line!!.color = resources.getColor(R.color.line_color)
        mPaint_line!!.style = Paint.Style.FILL

        mPaint_text = TextPaint()
        mPaint_text!!.color = resources.getColor(R.color.text_color)
        mPaint_text!!.isAntiAlias = true
        mPaint_text!!.textSize = mTextSize!!.toFloat()//字体大小
        mPaint_text!!.textAlign = Paint.Align.CENTER//从中间向两边绘制，不需要再次计算文字

        mPaint_bar = Paint()
        mPaint_bar!!.isDither = true
        mPaint_bar!!.strokeWidth = mBarWidth!!
        mPaint_bar!!.color = resources.getColor(R.color.bar_color)
        mPaint_xy!!.style = Paint.Style.FILL

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawXy(canvas)
        drawTextAndBar(canvas)
    }

    /**
     * 画x y轴字体 和 柱子
     * */
    private fun drawTextAndBar(canvas: Canvas?) {
        if (mData.size == 0) {
            return
        }

        if (mWidth!! - mPaint_text!!.measureText(mMaxValue.toString()) - 3 * mMargin!! - Util.dip2px(
                context,
                5f
            ) > mTextMargin!! * mData.size
        ) {
            val margin = (mWidth!! - mPaint_text!!.measureText(mMaxValue.toString()) - 3 * mMargin!! - Util.dip2px(
                context,
                5f
            )) / mData.size
            mTextMargin = margin.toInt()
        }

        //y轴
        if (mMaxValue!! < mSegment) {
            mMaxValue = mSegment
        }


        val itemValue = Math.ceil(mMaxValue!! / mSegment.toDouble()).toInt()
        val itemHeight = (mHeight!! - 3 * mMargin!! - Util.dip2px(
            context,
            15f
        ) - (mPaint_text!!.descent() - mPaint_text!!.ascent())) / mSegment //（ 总高度 - 上面一个margin - 下面连个margin - 距离上面的高度 - x轴字体的高度 ）/分成的段数


        lineStartX = mPaint_text!!.measureText(mMaxValue.toString()) + 2 * mMargin!!
        val lineStartY = mMargin!! + Util.dip2px(
            context,
            15f
        )
        val lineStopX = lineStartX + Util.dip2px(context, 8f)


        val textX = mMargin!!.toFloat() + mPaint_text!!.measureText(mMaxValue.toString()) / 2 // y轴字的x坐标
        val textY = lineStartY + (mPaint_text!!.descent() - mPaint_text!!.ascent()) / 4 //y轴字的y坐标


        for (i in 0 until mSegment) {
            //y轴数字
            canvas!!.drawText(
                ((mSegment - i) * itemValue).toString(),
                textX,
                textY + itemHeight * i,
                mPaint_text!!
            )

            //网格线头
            canvas.drawLine(
                lineStartX,
                lineStartY + itemHeight * i,
                lineStopX,
                lineStartY + itemHeight * i,
                mPaint_xy!!
            )

            //网格线
            canvas.drawLine(
                lineStartX + Util.dip2px(context, 8f),
                lineStartY + itemHeight * i,
                mWidth!!.toFloat() - mMargin!!,
                lineStartY + itemHeight * i,
                mPaint_line!!
            )

        }
        //画柱
        val realHeight =
            (mHeight!! - 3 * mMargin!! - Util.dip2px(
                context,
                15f
            ) - (mPaint_text!!.descent() - mPaint_text!!.ascent()))

        val bottom = mHeight!!.toFloat() - 2 * mMargin!! - (mPaint_text!!.descent() - mPaint_text!!.ascent())


        for (i in 0 until mData.size) {
            /*if (leftMoving > mTextMargin!!) {
                leftMoving -= mTextMargin!!
            }*/
            mRectF!!.left = lineStartX + mTextMargin!! * (i + 1) - leftMoving
            Log.i("12312312321", leftMoving.toString() +"!!!!!!!!!!!!!!!!!!!!!!!!!!!!${ lineStartX + mTextMargin!! * (i + 1) - leftMoving}")
            mRectF!!.right = mRectF!!.left + mBarWidth!!
            mRectF!!.top = (itemValue * mSegment - mData[i].value!!) / (itemValue * mSegment).toFloat() * realHeight +
                    mMargin!! + Util.dip2px(
                context,
                15f
            )
            mRectF!!.bottom = bottom
            canvas!!.drawRect(mRectF!!, mPaint_bar!!)

            //画字
            canvas.drawText(
                mData[i].name!!,
                mRectF!!.left + mBarWidth!! / 2,
                mHeight!!.toFloat() - mMargin!!,
                mPaint_text!!
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w - paddingLeft - paddingRight
        mHeight = h - paddingTop - paddingBottom
    }


    /**
     * 手势控制
     * */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val type = event.action

        when (type) {
            MotionEvent.ACTION_DOWN -> lastPointX = event.rawX

            MotionEvent.ACTION_MOVE -> {
                val x = event.rawX
                movingLeftThisTime = lastPointX - x

                leftMoving += movingLeftThisTime
                lastPointX = x

                invalidate()
            }

            MotionEvent.ACTION_UP ->
                //smooth scroll
                Thread(SmoothScrollThread(movingLeftThisTime)).start()

            else -> return super.onTouchEvent(event)
        }

        return true
    }


    private inner class SmoothScrollThread(internal var lastMoving: Float) : Runnable {
        internal var scrolling = true

        init {
            scrolling = true
        }

        override fun run() {
            while (scrolling) {
                val start = System.currentTimeMillis()
                lastMoving = (0.9f * lastMoving).toInt().toFloat()
                leftMoving += lastMoving

                checkLeftMoving()
                postInvalidate()

                if (Math.abs(lastMoving) < 5) {
                    scrolling = false
                }

                val end = System.currentTimeMillis()
                if (end - start < 20) {
                    try {
                        Thread.sleep(20 - (end - start))
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }

    private fun checkLeftMoving() {
        if (leftMoving < 0) {
            leftMoving = 0f
        }

        /*if (leftMoving > maxRight - minRight) {
            leftMoving = (maxRight - minRight).toFloat()
        }*/
    }


    /**
     * 画x y轴
     * */
    private fun drawXy(canvas: Canvas?) {
        val stopY = mHeight!!.toFloat() - 2 * mMargin!! - (mPaint_text!!.descent() - mPaint_text!!.ascent())
        //x轴
        canvas!!.drawLine(
            lineStartX,
            stopY,
            mWidth!!.toFloat() - mMargin!!,
            stopY,
            mPaint_xy!!
        )
        //y轴
        canvas.drawLine(
            lineStartX,
            mMargin!!.toFloat(),
            lineStartX,
            stopY,
            mPaint_xy!!
        )

        //画x轴三角形
        val path = Path()
        path.close()
        path.moveTo(
            mWidth!!.toFloat() - mMargin!!,
            stopY
        )
        path.lineTo(
            mWidth!!.toFloat() - mMargin!! - Util.dip2px(context, 5f),
            mHeight!!.toFloat() - 2 * mMargin!! - Util.dip2px(
                context,
                3f
            ) - (mPaint_text!!.descent() - mPaint_text!!.ascent())
        )
        path.lineTo(
            mWidth!!.toFloat() - mMargin!! - Util.dip2px(context, 5f),
            mHeight!!.toFloat() - 2 * mMargin!! + Util.dip2px(
                context,
                3f
            ) - (mPaint_text!!.descent() - mPaint_text!!.ascent())
        )
        canvas.drawPath(path, mPaint_xy!!)
        //画y轴三角形
        path.moveTo(lineStartX, mMargin!!.toFloat())
        path.lineTo(
            lineStartX + Util.dip2px(context, 3f),
            mMargin!! + Util.dip2px(context, 5f).toFloat()
        )
        path.lineTo(
            lineStartX - Util.dip2px(context, 3f),
            mMargin!! + Util.dip2px(context, 5f).toFloat()
        )
        canvas.drawPath(path, mPaint_xy!!)


        leftSpaceRect!!.left = 0f
        leftSpaceRect!!.top = 0f
        leftSpaceRect!!.right = lineStartX
        leftSpaceRect!!.bottom = mHeight!!.toFloat()
        //canvas.drawRect(leftSpaceRect, mPaint_bar)

    }


    /**
     * 设置数据
     * */
    fun setData(mData: List<BarChartData>) {
        this.mData.addAll(mData)
        mMaxValue = Math.ceil(mData[0].value!!.toDouble()).toInt()
        for (i in 0 until mData.size) {
            if (Math.ceil(mData[i].value!!.toDouble()).toInt() > mMaxValue!!) {
                mMaxValue = Math.ceil(mData[i].value!!.toDouble()).toInt()
            }
        }
        invalidate()
    }

    /**
     * y轴自定义
     * 分为几段，是否需要单位
     * */
    fun setYLine(segment: Int?, unit: Boolean?) {
        if (segment == null) {
            throw Exception("segment不能为空")
        }
        if (unit == null) {
            throw Exception("unit不能为空")
        }
        this.mSegment = segment
        this.mIsUnit = unit
        invalidate()
    }

}

