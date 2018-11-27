package com.example.admin.mycircleview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.text.DecimalFormat

/**
 * Author:LC
 * Date:2018/11/22
 * Description:This is 圆形进度条
 */
class MyCircleProgressView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    //是否开启抗锯齿
    private var antiAlias: Boolean? = null
    //圆心位置
    private var centerPosition: Point? = null
    //半径
    private var raduis: Float? = null
    //外圆半径
    private var mOuterRaduis: Float? = null

    //声明边界矩形
    private var mRectF: RectF? = null

    //声明背景圆画笔
    private var mBgCirPaint: Paint? = null//画笔
    private var mBgCirColor: Int? = null//颜色
    private var mBgCirWidth: Float? = null//宽度

    //声明进度圆的画笔
    private var mCirPaint: Paint? = null//画笔
    private var mCirColor: Int? = null//颜色
    private var mCirWidth: Float? = null//宽度

    //绘制的起始角度和滑过角度
    private var mStartAngle: Float? = null
    private var mSweepAngle: Float? = null

    //动画时间
    private var mAnimTime: Int? = null
    //属性动画
    private var mAnimator: ValueAnimator? = null
    //动画进度
    private var mPercent: Float? = null
    //进度值
    private var mValue: String? = null
    //最大值
    private var mMaxValue: Float? = null
    //绘制数值
    private var mValuePaint: TextPaint? = null
    private var mValueSize: Float? = null
    private var mValueColor: Int? = null
    //绘制单位
    private var mUnitPaint: TextPaint? = null
    private var mUnit: CharSequence? = "%"
    private var mUnitSize: Float? = null
    private var mUnitColor: Int? = null
    //绘制描述
    private var mHint: CharSequence? = null
    private var mHintPaint: TextPaint? = null
    private var mHintSize: Float? = null
    private var mHintColor: Int? = null
    //颜色渐变色
    private var isGradient: Boolean? = null
    private var mGradientColors: IntArray? = intArrayOf(Color.BLACK, Color.GRAY, Color.BLUE)
    private var mGradientColor: Int? = null
    private var mSweepGradient: SweepGradient? = null
    //开始的小圆点
    private var mSmallCirPaint: Paint? = null
    private var mSmallCirColor: Int? = null
    private var mSmallCirWidth: Float? = null
    private var mSmallCirEnable: Boolean? = null
    //阴影
    private var mShadowColor: Int? = null
    private var mShadowSize: Float? = null
    private var mShadowIsShow: Boolean? = null
    //是否是整数
    private var mDigit: Int? = null
    //是否需要动画
    private var isAnim: Boolean? = null

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPercent = 0f
        centerPosition = Point()//初始化圆心属性
        mRectF = RectF()//初始化圆弧
        mAnimator = ValueAnimator()//初始化属性动画
        initAttrs(attrs, context)//初始化属性
        initPaint()//初始化画笔
    }


    /**
     * 初始化属性
     */
    private fun initAttrs(attrs: AttributeSet?, context: Context?) {
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.MyCircleProgressView)

        antiAlias = typedArray!!.getBoolean(R.styleable.MyCircleProgressView_antiAlias, Constant.ANTI_ALIAS)
        mSmallCirEnable =
                typedArray.getBoolean(R.styleable.MyCircleProgressView_smallCirEnable, Constant.SMALLCIRCLE_ENABLE)
        isAnim = typedArray.getBoolean(R.styleable.MyCircleProgressView_isanim, Constant.IS_ANIM)

        mDigit = typedArray.getInt(R.styleable.MyCircleProgressView_digit, Constant.DEFALUT_DIGIT)

        mBgCirColor = typedArray.getColor(R.styleable.MyCircleProgressView_mBgCirColor, Color.GRAY)
        mBgCirWidth =
                typedArray.getDimension(R.styleable.MyCircleProgressView_mBgCirWidth, Constant.DEFAULT_BGCIR_WIDTH)

        mCirColor = typedArray.getColor(R.styleable.MyCircleProgressView_mCirColor, Color.YELLOW)
        mCirWidth = typedArray.getDimension(R.styleable.MyCircleProgressView_mCirWidth, Constant.DEFAULT_CIR_WIDTH)

        mSmallCirColor = typedArray.getColor(R.styleable.MyCircleProgressView_smallCirColor, Color.WHITE)
        mSmallCirWidth =
                typedArray.getDimension(
                    R.styleable.MyCircleProgressView_smallCirWidth,
                    Constant.DEFAULT_SMALLCIRCLE_WIDTH
                )

        mAnimTime = typedArray.getInt(R.styleable.MyCircleProgressView_animTime, Constant.DEFAULT_ANIMTIME)

        mValue = typedArray.getString(R.styleable.MyCircleProgressView_value)
        mMaxValue = typedArray.getFloat(R.styleable.MyCircleProgressView_maxvalue, Constant.DEFAULT_MAX_VALUE)

        mStartAngle = typedArray.getFloat(R.styleable.MyCircleProgressView_startAngle, Constant.DEFAULT_START_ANGLE)
        mSweepAngle = typedArray.getFloat(R.styleable.MyCircleProgressView_sweepAngle, Constant.DEFAULT_SWEEP_ANGLE)

        mValueSize = typedArray.getDimension(R.styleable.MyCircleProgressView_valueSize, Constant.DEFAULT_VALUE_SIZE)
        mValueColor = typedArray.getColor(R.styleable.MyCircleProgressView_valueColor, Color.BLACK)

        mHint = typedArray.getString(R.styleable.MyCircleProgressView_hint)
        mHintSize = typedArray.getDimension(R.styleable.MyCircleProgressView_hintSize, Constant.DEFAULT_HINT_SIZE)
        mHintColor = typedArray.getColor(R.styleable.MyCircleProgressView_hintColor, Color.GRAY)

        mUnit = typedArray.getString(R.styleable.MyCircleProgressView_unit)
        mUnitSize = typedArray.getDimension(R.styleable.MyCircleProgressView_unitSize, Constant.DEFAULT_UNIT_SIZE)
        mUnitColor = typedArray.getColor(R.styleable.MyCircleProgressView_unitColor, Color.GRAY)

        mShadowColor = typedArray.getColor(R.styleable.MyCircleProgressView_shadowColor, Color.BLACK)
        mShadowIsShow = typedArray.getBoolean(R.styleable.MyCircleProgressView_shadowShow, Constant.SHADOW_SHOW)
        mShadowSize = typedArray.getFloat(R.styleable.MyCircleProgressView_shadowSize, Constant.DEFAULT_SHADOW_SIZE)

        isGradient = typedArray.getBoolean(R.styleable.MyCircleProgressView_isGradient, Constant.IS_GRADIENT)
        mGradientColor = typedArray.getResourceId(R.styleable.MyCircleProgressView_gradient, 0)
        if (mGradientColor != 0) {
            mGradientColors = resources.getIntArray(mGradientColor!!)
        }

        typedArray.recycle()

    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        //圆画笔
        mCirPaint = Paint()
        mCirPaint!!.isAntiAlias = antiAlias!!//是否开启抗锯齿
        mCirPaint!!.style = Paint.Style.STROKE//画笔样式  //STROKE 只绘制图形轮廓（描边） FILL 只绘制图形内容 FILL_AND_STROKE 既绘制轮廓也绘制内容
        mCirPaint!!.strokeWidth = mCirWidth!!//画笔宽度
        mCirPaint!!.strokeCap =
                Paint.Cap.ROUND//笔刷样式 //当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式Cap.ROUND,或方形样式Cap.SQUARE
        mCirPaint!!.color = mCirColor!!//画笔颜色

        //背景圆画笔
        mBgCirPaint = Paint()
        mBgCirPaint!!.isAntiAlias = antiAlias!!
        mBgCirPaint!!.style = Paint.Style.STROKE
        mBgCirPaint!!.strokeWidth = mBgCirWidth!!
        mBgCirPaint!!.strokeCap = Paint.Cap.ROUND
        mBgCirPaint!!.color = mBgCirColor!!

        //小圆画笔
        mSmallCirPaint = Paint()
        mSmallCirPaint!!.isAntiAlias = antiAlias!!
        mSmallCirPaint!!.style = Paint.Style.FILL
        //mSmallCirPaint!!.strokeWidth = mSmallCirWidth!!
        mSmallCirPaint!!.strokeCap = Paint.Cap.ROUND
        mSmallCirPaint!!.color = mSmallCirColor!!

        //初始化字体画笔
        mValuePaint = TextPaint()
        mValuePaint!!.isAntiAlias = antiAlias!!//是否抗锯齿
        mValuePaint!!.textSize = mValueSize!!//字体大小
        mValuePaint!!.color = mValueColor!!//字体颜色
        mValuePaint!!.textAlign = Paint.Align.CENTER//从中间向两边绘制，不需要再次计算文字
        mValuePaint!!.typeface = Typeface.DEFAULT_BOLD//字体加粗

        mHintPaint = TextPaint()
        mHintPaint!!.isAntiAlias = antiAlias!!
        mHintPaint!!.textSize = mHintSize!!
        mHintPaint!!.color = mHintColor!!
        mHintPaint!!.textAlign = Paint.Align.CENTER

        mUnitPaint = TextPaint()
        mUnitPaint!!.isAntiAlias = antiAlias!!
        mUnitPaint!!.textSize = mUnitSize!!
        mUnitPaint!!.color = mUnitColor!!
        mUnitPaint!!.textAlign = Paint.Align.LEFT

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //圆心位置
        centerPosition!!.x = w / 2
        centerPosition!!.y = h / 2
        //半径
        val maxCirWidth = Math.max(mCirWidth!!, mBgCirWidth!!)
        val minWidth =
            Math.min(w - paddingLeft - paddingRight - 2 * maxCirWidth, h - paddingBottom - paddingTop - 2 * maxCirWidth)
        raduis = minWidth / 2
        mOuterRaduis = raduis!! + maxCirWidth / 2
        //矩形坐标
        mRectF!!.left = centerPosition!!.x - raduis!! - maxCirWidth / 2
        mRectF!!.top = centerPosition!!.y - raduis!! - maxCirWidth / 2
        mRectF!!.right = centerPosition!!.x + raduis!! + maxCirWidth / 2
        mRectF!!.bottom = centerPosition!!.y + raduis!! + maxCirWidth / 2

        if (isGradient!!) {
            sweepGradientCircle()//圆环颜色渐变
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawText(canvas)
        drawCircle(canvas)
        if (mSmallCirEnable!!) {
            drawSmallCircle(canvas)
        }
    }

    /**
     * 画小圆
     * */
    private fun drawSmallCircle(canvas: Canvas?) {
        var x: Float? = null
        var y: Float? = null
        when {
            mStartAngle!! in 0.0..90.0 -> {
                x = (centerPosition!!.x + mOuterRaduis!! * Math.sin(Math.PI * (90 - mStartAngle!!) / 180)).toFloat()
                y = (centerPosition!!.y + mOuterRaduis!! * Math.cos(Math.PI * (90 - mStartAngle!!) / 180)).toFloat()
            }
            mStartAngle!! in 90.0..180.0 -> {
                x = (centerPosition!!.x - mOuterRaduis!! * Math.sin(Math.PI * (180 - mStartAngle!!) / 180)).toFloat()
                y = (centerPosition!!.y + mOuterRaduis!! * Math.cos(Math.PI * (180 - mStartAngle!!) / 180)).toFloat()
            }
            mStartAngle!! in 180.0..270.0 -> {
                x = (centerPosition!!.x - mOuterRaduis!! * Math.sin(Math.PI * (270 - mStartAngle!!) / 180)).toFloat()
                y = (centerPosition!!.y - mOuterRaduis!! * Math.cos(Math.PI * (270 - mStartAngle!!) / 180)).toFloat()
            }
            mStartAngle!! in 270.0..360.0 -> {
                x = (centerPosition!!.x + mOuterRaduis!! * Math.sin(Math.PI * (mStartAngle!! - 270) / 180)).toFloat()
                y = (centerPosition!!.y - mOuterRaduis!! * Math.cos(Math.PI * (mStartAngle!! - 270) / 180)).toFloat()
            }
        }

        canvas!!.drawCircle(
            x!!,
            y!!,
            mSmallCirWidth!! / 2,
            mSmallCirPaint!!
        )
    }

    /**
     * 画字
     */
    private fun drawText(canvas: Canvas?) {

        canvas!!.drawText(
            mValue!!,
            centerPosition!!.x.toFloat(),
            centerPosition!!.y.toFloat(),
            mValuePaint!!
        )

        if (mUnit != null || mUnit != "") {
            canvas.drawText(
                mUnit.toString(),
                centerPosition!!.x + mValuePaint!!.measureText(mValue.toString()) / 2,
                centerPosition!!.y.toFloat(),
                mUnitPaint!!
            )
        }

        if (mHint != null || mHint != "") {
            canvas.drawText(
                mHint.toString(),
                centerPosition!!.x.toFloat(),
                centerPosition!!.y - mHintPaint!!.ascent() + 3,
                mHintPaint!!
            )
        }

    }

    /**
     * 使用渐变色画圆
     */
    private fun sweepGradientCircle() {
        mSweepGradient =
                SweepGradient(centerPosition!!.x.toFloat(), centerPosition!!.y.toFloat(), mGradientColors!!, null)
        mCirPaint!!.shader = mSweepGradient
    }

    /**
     * 画圆
     */
    private fun drawCircle(canvas: Canvas?) {
        canvas!!.save()
        if (mShadowIsShow!!) {
            mCirPaint!!.setShadowLayer(mShadowSize!!, 0f, 0f, mShadowColor!!)//设置阴影
        }
        //画背景圆
        canvas.drawArc(mRectF!!, mStartAngle!!, mSweepAngle!!, false, mBgCirPaint!!)
        //画圆
        canvas.drawArc(mRectF!!, mStartAngle!!, mSweepAngle!! * mPercent!!, false, mCirPaint!!)
        canvas.restore()
    }

    /**
     * 设值
     */
    fun setValue(value: String, maxValue: Float): MyCircleProgressView {
        if (isNum(value)) {
            mValue = value
            mMaxValue = maxValue
            val start = mPercent
            val end = value.toFloat() / maxValue
            startAnim(start!!, end, mAnimTime!!)
        } else {
            mValue = value
        }
        return this
    }

    /**
     * 动画
     * */
    private fun startAnim(start: Float, end: Float, animTime: Int) {
        mAnimator = ValueAnimator.ofFloat(start, end)
        mAnimator!!.duration = animTime.toLong()
        mAnimator!!.addUpdateListener {
            mPercent = it.animatedValue as Float?
            mValue = if (isAnim!!) {
                roundByScale((mPercent!! * mMaxValue!!).toDouble(), mDigit!!)
            } else {
                roundByScale(mValue!!.toDouble(), mDigit!!)
            }
            postInvalidate()
        }
        mAnimator!!.start()
    }

    /**
     * 设置动画时常
     * */
    fun setAnimTime(animTime: Int): MyCircleProgressView {
        this.mAnimTime = animTime
        invalidate()
        return this
    }


    /**
     * 是否渐变色
     * */
    fun setIsGradient(isGradient: Boolean): MyCircleProgressView {
        this.isGradient = isGradient
        invalidate()
        return this
    }

    /**
     * 设置渐变色
     * */
    fun setGradientColors(gradientColors: IntArray): MyCircleProgressView {
        mGradientColors = gradientColors
        sweepGradientCircle()
        return this
    }

    /**
     * 是否显示起始小圆
     * */
    fun setSmallCircleEnable(enable: Boolean): MyCircleProgressView {
        mSmallCirEnable = enable
        invalidate()
        return this
    }

    /**
     * 是否显示阴影
     * */
    fun setShadowEnable(enable: Boolean): MyCircleProgressView {
        mShadowIsShow = enable
        invalidate()
        return this
    }

    /**
     * 设置小数位数
     * */
    fun setDigit(digit: Int): MyCircleProgressView {
        mDigit = digit
        invalidate()
        return this
    }

    /**
     * 将double格式化为指定小数位的String，不足小数位用0补全
     *
     * @param v     需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    fun roundByScale(v: Double, scale: Int): String {
        if (scale < 0) {
            throw  IllegalArgumentException(
                "The   scale   must   be   a   positive   integer   or   zero"
            )
        }
        if (scale == 0) {
            return DecimalFormat("0").format(v)
        }
        var formatStr = "0."

        for (i in 0 until scale) {
            formatStr += "0"
        }
        return DecimalFormat(formatStr).format(v);
    }

    fun isNum(str: String): Boolean {
        try {
            val toDouble = str.toDouble()
        } catch (e: Exception) {

            return false
        }

        return true
    }

}