package com.example.admin.mycircleview.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
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
    private var mSpacePaint: Paint? = null//左右的空间间隔
    private var mPaint_text: TextPaint? = null//字体画笔
    private var mTextSize: Int? = null//字体大小
    private var mStrokeWidth: Int? = null//线宽度
    private var mWidth: Int? = null//控件宽
    private var mHeight: Int? = null//控件高
    private var mMargin: Int? = null//间距
    private var mTextMargin: Int? = null//x轴字体间距
    private var mMaxValue: Int? = null//集合最大值，用于y轴取值分段
    private var mSegment: Int = BarChartConstant.DEFAULT_SEGMENT//y轴分为几段
    private var mBarWidth: Float? = null//柱子的宽度
    private var mData: MutableList<BarChartData> = ArrayList()
    private var mRectF: RectF? = null
    private var leftMoving: Float = 0f
    private var lastPointX: Float = 0f
    private var movingLeftThisTime = 0f
    private var lineStartX: Float = 0f
    private var leftSpaceRect: RectF? = null
    private var rightSpaceRect: RectF? = null
    private var mBg: Int? = null//背景颜色
    private var canScorllSpace: Float = 0f
    private var mExtraSpace: Float = 0f
    private var yUnit: String? = null
    private var mDigit: Int? = null
    private var mBarColor: Int? = null
    private var mAnimator: ValueAnimator? = null//属性动画
    private var mPercent: Float = 0f//动画进度
    private var isAnim: Boolean? = null//是否需要动画
    private var animTime: Int? = null
    private var isShowTopNum: Boolean? = null//是否显示柱子顶端数字
    private var isShowGridLine: Boolean? = null//是否显示网格线

    init {
        mMargin = Util.dip2px(context!!, 8f)
        mTextMargin = Util.dip2px(context, 50f)
        mStrokeWidth = Util.dip2px(context, 0.7f)
        mTextSize = Util.dip2px(context, 10f)
        mRectF = RectF()
        leftSpaceRect = RectF()
        rightSpaceRect = RectF()
        mAnimator = ValueAnimator()
        initAttrs(attrs, context)//初始化属性
        initPaint()//初始化画笔
    }

    private fun initAttrs(attrs: AttributeSet?, context: Context?) {
        val typedArray = context!!.obtainStyledAttributes(
            attrs,
            R.styleable.MyBarChartView
        )
        mBarWidth = typedArray.getDimension(R.styleable.MyBarChartView_barWidth, BarChartConstant.DEFAULT_BARWIDTH)
        mBg = typedArray.getColor(R.styleable.MyBarChartView_bg, Color.WHITE)
        yUnit = typedArray.getString(R.styleable.MyBarChartView_yUnit)
        mSegment = typedArray.getInt(R.styleable.MyBarChartView_mSegment, BarChartConstant.DEFAULT_SEGMENT)
        mDigit = typedArray.getInt(R.styleable.MyBarChartView_mDigit, BarChartConstant.DEFAULT_DIGIT)
        mBarColor = typedArray.getColor(R.styleable.MyBarChartView_barColor, Color.BLACK)
        isAnim = typedArray.getBoolean(R.styleable.MyBarChartView_isAnim, BarChartConstant.DEFAULT_ISANIM)
        animTime = typedArray.getInt(R.styleable.MyBarChartView_animTime, BarChartConstant.DEFAULT_ANIMTIME)
        isShowGridLine = typedArray.getBoolean(
            R.styleable.MyBarChartView_isShowGridLine,
            BarChartConstant.DEFAULT_ISSHOWGRIDLINE
        )
        isShowTopNum =
                typedArray.getBoolean(R.styleable.MyBarChartView_isShowTopNum, BarChartConstant.DEFAULT_ISSHOWTOPNUM)

        if (yUnit == null) {
            yUnit = ""
        }
        typedArray.recycle()
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
        mPaint_text!!.textAlign = Paint.Align.RIGHT

        mPaint_bar = Paint()
        mPaint_bar!!.isDither = true
        mPaint_bar!!.strokeWidth = mBarWidth!!
        mPaint_bar!!.color = mBarColor!!
        mPaint_bar!!.style = Paint.Style.FILL

        mSpacePaint = Paint()
        mSpacePaint!!.isDither = true
        mSpacePaint!!.color = mBg!!
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawColor(mBg!!)
        drawTextAndBar(canvas)
        drawXy(canvas)
    }

    /**
     * 画x y轴字体 和 柱子
     * */
    private fun drawTextAndBar(canvas: Canvas?) {
        if (mData.size == 0) {
            return
        }

        //向左偏移量
        mExtraSpace = if (mBarWidth!! >= mPaint_text!!.measureText(mMaxValue.toString())) {
            0f
        } else {
            (mPaint_text!!.measureText(mMaxValue.toString()) - mBarWidth!!) / 2
        }

        //如果说按照设置的间距无法铺满x轴，那么就将数据平分（数据很少的情况）
        if (mWidth!! - mPaint_text!!.measureText(mMaxValue.toString()) - 3 * mMargin!! - Util.dip2px(
                context,
                5f
            ) > mTextMargin!! * mData.size
        ) {

            val margin = (mWidth!! - mPaint_text!!.measureText(mMaxValue.toString()) - 3 * mMargin!! - Util.dip2px(
                context,
                5f
            ) - mExtraSpace) / mData.size

            mTextMargin = margin.toInt()
        }

        //y轴
        if (mMaxValue!! < mSegment) {
            mMaxValue = mSegment
        }

        //y轴的每一个数字
        val itemValue = Math.ceil(mMaxValue!! / mSegment.toDouble()).toInt()
        val itemHeight = (mHeight!! - 3 * mMargin!! - Util.dip2px(
            context,
            15f
        ) - (mPaint_text!!.descent() - mPaint_text!!.ascent())) / mSegment //（ 总高度 - 上面一个margin - 下面连个margin - 距离上面的高度 - x轴字体的高度 ）/分成的段数


        lineStartX = mPaint_text!!.measureText(mMaxValue.toString()) + mPaint_text!!.measureText(yUnit) + 2 * mMargin!!
        val lineStartY = mMargin!! + Util.dip2px(
            context,
            15f
        )
        val lineStopX = lineStartX + Util.dip2px(context, 8f)


        val textX = mMargin!!.toFloat() + mPaint_text!!.measureText(mMaxValue.toString())  // y轴字的x坐标
        val textY = lineStartY + (mPaint_text!!.descent() - mPaint_text!!.ascent()) / 4 //y轴字的y坐标


        val realHeight =
            (mHeight!! - 3 * mMargin!! - Util.dip2px(
                context,
                15f
            ) - (mPaint_text!!.descent() - mPaint_text!!.ascent()))

        val bottom = mHeight!!.toFloat() - 2 * mMargin!! - (mPaint_text!!.descent() - mPaint_text!!.ascent())


        if (isShowGridLine!!) {
            for (i in 0 until mSegment) {
                //网格线头
                canvas!!.drawLine(
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
        }

        for (i in 0 until mData.size) {
            //画柱
            mRectF!!.right = lineStartX + mTextMargin!! * (i + 1) - leftMoving

            mRectF!!.left = mRectF!!.right - mBarWidth!!

            mRectF!!.bottom = bottom

            mRectF!!.top = mRectF!!.bottom - mData[i].value!! / (itemValue * mSegment).toFloat() * realHeight * mPercent

            canvas!!.drawRect(mRectF!!, mPaint_bar!!)

            //画字
            canvas.drawText(
                mData[i].name!!,
                mRectF!!.right + mPaint_text!!.measureText(mData[i].name.toString()) / 2 - mBarWidth!! / 2,
                mHeight!!.toFloat() - mMargin!!,
                mPaint_text!!
            )

            //柱子顶端画字
            if (isShowTopNum!!) {
                canvas.drawText(
                    Util.roundByScale(mData[i].value!!.toDouble() * mPercent, mDigit!!),
                    mRectF!!.right + mPaint_text!!.measureText(
                        Util.roundByScale(
                            mData[i].value!!.toDouble() * mPercent,
                            mDigit!!
                        )
                    ) / 2 - mBarWidth!! / 2,
                    mRectF!!.top - Util.dp2px(context, 5f),
                    mPaint_text!!
                )
            }
        }


        //用于遮挡左右的间距，不然得滑出屏幕过后才消失（待优化）
        leftSpaceRect!!.left = 0f
        leftSpaceRect!!.top = 0f
        leftSpaceRect!!.right = lineStartX
        leftSpaceRect!!.bottom = mHeight!!.toFloat()
        canvas!!.drawRect(leftSpaceRect!!, mSpacePaint!!)
        rightSpaceRect!!.left = mWidth!!.toFloat() - mMargin!! - Util.dip2px(context, 5f)
        rightSpaceRect!!.top = 0f
        rightSpaceRect!!.right = mWidth!!.toFloat()
        rightSpaceRect!!.bottom = mHeight!!.toFloat()
        canvas.drawRect(rightSpaceRect!!, mSpacePaint!!)


        //保证向左滑动时有一屏的显示，不会全部滑出屏幕外
        canScorllSpace = mTextMargin!! *
                mData.size + mExtraSpace -
                (mWidth!! - 3 * mMargin!! - Util.dip2px(
                    context,
                    5f
                ) - mPaint_text!!.measureText(mMaxValue.toString()))


        for (i in 0 until mSegment) {
            //y轴数字
            canvas!!.drawText(
                ((mSegment - i) * itemValue).toString(),
                textX,
                textY + itemHeight * i,
                mPaint_text!!
            )
            //y轴单位
            canvas.drawText(
                yUnit,
                textX + mPaint_text!!.measureText(yUnit),
                textY + itemHeight * i,
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

        if (leftMoving > canScorllSpace) {
            leftMoving = canScorllSpace
        }
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
    }


    /**
     * 设置数据
     * */
    fun setData(mDatas: List<BarChartData>) {
        if (mData.size > 0) {
            mData.clear()
        }
        mData.addAll(mDatas)
        mMaxValue = Math.ceil(mData[0].value!!.toDouble()).toInt()
        for (i in 0 until mData.size) {
            if (Math.ceil(mData[i].value!!.toDouble()).toInt() > mMaxValue!!) {
                mMaxValue = Math.ceil(mData[i].value!!.toDouble()).toInt()
            }
        }

        if (isAnim!!) {
            startAnim(0f, 1f, animTime!!)
        } else {
            mPercent = 1F
            invalidate()
        }

    }


    /**
     * 动画
     * */
    private fun startAnim(start: Float, end: Float, animTime: Int) {
        mAnimator = ValueAnimator.ofFloat(start, end)
        mAnimator!!.duration = animTime.toLong()
        mAnimator!!.addUpdateListener {

            mPercent = it.animatedValue as Float

            postInvalidate()
        }
        mAnimator!!.start()
    }


}

