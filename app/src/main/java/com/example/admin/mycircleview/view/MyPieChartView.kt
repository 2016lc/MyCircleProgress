package com.example.admin.mycircleview.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.example.admin.mycircleview.*
import kotlinx.android.synthetic.main.item_piechart.view.*
import java.util.ArrayList

/**
 * Author:LC
 * Date:2018/11/27
 * Description:This is 扇形图
 */
class MyPieChartView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var mPieChartPaint: Paint? = null//画笔
    private var mPieChartWidth: Float? = null
    //圆心位置
    private var centerPosition: Point? = null
    //半径
    private var raduis: Float? = null
    private var dataRaduis: Float? = null
    //声明边界矩形
    private var mRectF: RectF? = null
    //数据
    private var mData: MutableList<PieChartData>? = ArrayList()
    //总数
    private var mTotalNum: Float? = 0f
    //开始角度
    private var mStartAngle: Float? = 0f
    //扫过的角度
    private var mSweepAngle: Float? = 0f
    //动画时间
    private var mAnimTime: Int? = null
    //属性动画
    private var mAnimator: ValueAnimator? = null
    //动画进度
    private var mPercent: Float? = null
    //字体
    private var mDataPaint: TextPaint? = null
    private var mDataSize: Float? = null
    private var mDataColor: Int? = null
    //单位
    private var mUnitPaint: TextPaint? = null
    private var mUnitSize: Float? = null
    private var mUnitColor: Int? = null
    //样式选择
    private var mType: PieChartType? = PieChartType.CONTENT_PERCENT
    //列表说明
    private var mRecyclerView: RecyclerView? = null
    //上下文声明
    private var mContext: Context? = null
    //声明adapter
    private var adapter: mAdapter? = null
    //布局样式
    private var mLayoutType: String? = null //default 普通样式  vertical 竖向布局  horizontal 横向布局  pointingInstructions 指向说明
    //横向间距
    private var mHoriMargin: Float? = null
    //纵向间距
    private var mVerticalMargin: Float? = null
    //指向说明
    private var mPointingPaint: Paint? = null
    private var mPointingWidth: Float? = null
    private var mPointingColor: Int? = null

    init {
        mContext = context
        mPercent = 0f
        mAnimator = ValueAnimator()//初始化属性动画
        centerPosition = Point()//初始化圆心属性
        mRectF = RectF()
        initAttrs(attrs, context)//初始化属性
        initPaint()//初始化画笔
        addLabel()
    }


    private fun initAttrs(attrs: AttributeSet?, context: Context?) {
        val typedArray = context!!.obtainStyledAttributes(
            attrs,
            R.styleable.MyPieChartView
        )
        mPieChartWidth = typedArray.getDimension(
            R.styleable.MyPieChartView_piechartWidth,
            PieChartConstant.DEFAULT_PIECHART_WIDTH
        )

        mDataSize = typedArray.getDimension(R.styleable.MyPieChartView_dataSize, PieChartConstant.DEFAULT_DATA_SIZE)
        mDataColor = typedArray.getColor(R.styleable.MyPieChartView_dataColor, Color.WHITE)

        mUnitColor = typedArray.getColor(R.styleable.MyPieChartView_numColor, Color.WHITE)
        mUnitSize = typedArray.getFloat(R.styleable.MyPieChartView_numSize, PieChartConstant.DEFAULT_UNIT_SIZE)

        mHoriMargin =
                typedArray.getDimension(R.styleable.MyPieChartView_horiMargin, PieChartConstant.DEFAULT_HORI_MARGIN)
        mVerticalMargin = typedArray.getDimension(
            R.styleable.MyPieChartView_verticalMargin,
            PieChartConstant.DEFAULT_VERTICAL_MARGIN
        )

        mAnimTime = typedArray.getInt(R.styleable.MyPieChartView_animTime, PieChartConstant.DEFAULT_ANIM_TIME)


        mPointingColor = typedArray.getColor(R.styleable.MyPieChartView_pointingColor, Color.GRAY)
        mPointingWidth = typedArray.getDimension(
            R.styleable.MyPieChartView_pointingWidth,
            PieChartConstant.DEFAULT_POINTING_WIDTH
        )



        mLayoutType = typedArray.getString(R.styleable.MyPieChartView_layoutType)
        if (mLayoutType == null) {
            mLayoutType = "default"
        }

        typedArray.recycle()
    }

    private fun initPaint() {
        mPieChartPaint = Paint()
        mPieChartPaint!!.isAntiAlias = true//是否开启抗锯齿
        mPieChartPaint!!.isDither = true//防抖动
        mPieChartPaint!!.style =
                Paint.Style.FILL//画笔样式  //STROKE 只绘制图形轮廓（描边） FILL 只绘制图形内容 FILL_AND_STROKE 既绘制轮廓也绘制内容
        mPieChartPaint!!.strokeWidth = mPieChartWidth!!//画笔宽度
        /*mPieChartPaint!!.strokeCap =
                Paint.Cap.SQUARE*///笔刷样式 //当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式Cap.ROUND,或方形样式Cap.SQUARE
        //mPieChartPaint!!.color = Color.RED

        mDataPaint = TextPaint()
        mDataPaint!!.isDither = true
        mDataPaint!!.isAntiAlias = true//是否抗锯齿
        mDataPaint!!.textSize = mDataSize!!//字体大小
        mDataPaint!!.color = mDataColor!!//字体颜色
        mDataPaint!!.textAlign = Paint.Align.CENTER//从中间向两边绘制，不需要再次计算文字


        mUnitPaint = TextPaint()
        mUnitPaint!!.isDither = true
        mUnitPaint!!.isAntiAlias = true//是否抗锯齿
        mUnitPaint!!.textSize = mUnitSize!!//字体大小
        mUnitPaint!!.color = mUnitColor!!//字体颜色
        mUnitPaint!!.textAlign = Paint.Align.CENTER//从中间向两边绘制，不需要再次计算文字

        mPointingPaint = Paint()
        mPointingPaint!!.isAntiAlias = true//是否开启抗锯齿
        mPointingPaint!!.isDither = true//防抖动
        mPointingPaint!!.color = mPointingColor!!
        mPointingPaint!!.strokeWidth = mPointingWidth!!


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var minWidth: Int? = 0
        when (mLayoutType) {
            "horizontal" -> {
                //圆心位置
                centerPosition!!.x = w / 2 - mHoriMargin!!.toInt()
                centerPosition!!.y = h / 2
                //半径
                minWidth =
                        Math.min(
                            w - paddingLeft - paddingRight - mHoriMargin!!.toInt() * 2,
                            h - paddingBottom - paddingTop
                        )
            }
            "vertical" -> {
                //圆心位置
                centerPosition!!.x = w / 2
                centerPosition!!.y = h / 2 - mVerticalMargin!!.toInt()
                //半径
                minWidth =
                        Math.min(
                            w - paddingLeft - paddingRight,
                            h - paddingBottom - paddingTop - mVerticalMargin!!.toInt() * 2
                        )
            }
            else -> {
                //圆心位置
                centerPosition!!.x = w / 2
                centerPosition!!.y = h / 2
                //半径
                minWidth =
                        Math.min(w - paddingLeft - paddingRight, h - paddingBottom - paddingTop)
            }
        }

        raduis = if (mLayoutType == "pointingInstructions") {
            (minWidth!! / 2).toFloat() - 75
        } else {
            (minWidth!! / 2).toFloat()
        }
        dataRaduis = raduis!! * 3 / 4
        //矩形坐标
        mRectF!!.left = centerPosition!!.x - raduis!!
        mRectF!!.top = centerPosition!!.y - raduis!!
        mRectF!!.right = centerPosition!!.x + raduis!!
        mRectF!!.bottom = centerPosition!!.y + raduis!!

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

    }


    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        drawPieChart(canvas)
    }

    private fun drawPieChart(canvas: Canvas?) {
        canvas!!.save()
        mStartAngle = 0f
        mSweepAngle = 0f
        for (i in 0 until mData!!.size) {
            mPieChartPaint!!.color = mData!![i].color!!

            mSweepAngle = (mData!![i].num!! / mTotalNum!!) * 360 * mPercent!!

            //画圆
            canvas.drawArc(mRectF!!, mStartAngle!!, mSweepAngle!!, true, mPieChartPaint!!)
            mStartAngle = mStartAngle!! + mSweepAngle!!
            if (mLayoutType == "pointingInstructions") {
                //指向说明
                pointData(canvas, i)
            } else {
                //画数据
                drawData(canvas, i)
            }
        }
        canvas.restore()
    }

    private fun pointData(canvas: Canvas, i: Int) {
        val xP = centerPosition!!.x + raduis!! *
                Math.sin(Math.toRadians((90 + mStartAngle!! - mSweepAngle!! / 2).toDouble())).toFloat()
        val yP = centerPosition!!.y - raduis!! *
                Math.cos(Math.toRadians((90 + mStartAngle!! - mSweepAngle!! / 2).toDouble())).toFloat()
        val xEdP = centerPosition!!.x + (raduis!! + 20) *
                Math.sin(Math.toRadians((90 + mStartAngle!! - mSweepAngle!! / 2).toDouble())).toFloat()
        val yEdP = centerPosition!!.y - (raduis!! + 20) *
                Math.cos(Math.toRadians((90 + mStartAngle!! - mSweepAngle!! / 2).toDouble())).toFloat()
        var xLast = 0f
        xLast = if (mStartAngle!! - mSweepAngle!! / 2 >= 270 || mStartAngle!! - mSweepAngle!! / 2 <= 90) {
            xEdP + 30
        } else {
            xEdP - 30
        }
        canvas.drawLine(xP, yP, xEdP, yEdP, mPointingPaint!!)
        canvas.drawLine(xEdP, yEdP, xLast, yEdP, mPointingPaint!!)
        canvas.drawText(mData!![i].name!!, xLast, yEdP, mDataPaint!!)
        canvas.drawText(
            mData!![i].num!!.toString() + mData!![i].unit,
            xLast,
            yEdP - mDataPaint!!.ascent() + 5,
            mUnitPaint!!
        )
    }

    private fun drawData(canvas: Canvas, i: Int) {
        val x = centerPosition!!.x + dataRaduis!! *
                Math.sin(Math.toRadians((90 + mStartAngle!! - mSweepAngle!! / 2).toDouble())).toFloat()
        val y = centerPosition!!.y - dataRaduis!! *
                Math.cos(Math.toRadians((90 + mStartAngle!! - mSweepAngle!! / 2).toDouble())).toFloat()

        when (mType) {
            PieChartType.CONTENT_NUM -> {
                canvas.drawText(mData!![i].name!!, x, y, mDataPaint!!)
                canvas.drawText(
                    mData!![i].num!!.toString() + mData!![i].unit,
                    x,
                    y - mDataPaint!!.ascent() + 5,
                    mUnitPaint!!
                )
            }
            PieChartType.CONTENT_PERCENT -> {
                canvas.drawText(mData!![i].name!!, x, y, mDataPaint!!)
                canvas.drawText(
                    Util.roundByScale(mData!![i].num!!.toDouble() * 100 / mTotalNum!!.toDouble(), 2) + "%",
                    x,
                    y - mDataPaint!!.ascent() + 5,
                    mUnitPaint!!
                )
            }
            PieChartType.NUM -> {
                canvas.drawText(
                    mData!![i].num!!.toString() + mData!![i].unit,
                    x,
                    y,
                    mUnitPaint!!
                )

            }
            PieChartType.PERCENT -> {
                canvas.drawText(
                    Util.roundByScale(mData!![i].num!!.toDouble() * 100 / mTotalNum!!.toDouble(), 2) + "%",
                    x,
                    y,
                    mUnitPaint!!
                )
            }
        }
    }


    /**
     * 增加列表说明
     * */
    private fun addLabel() {
        when (mLayoutType) {
            "horizontal" -> addHorizontal()
            "vertical" -> addVertical()
        }
    }

    private fun addHorizontal() {
        mRecyclerView = RecyclerView(context)
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.isNestedScrollingEnabled = false
        if (adapter == null) {
            adapter = mAdapter(mContext!!, mData)
            mRecyclerView!!.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }

        val relativeLayout = RelativeLayout(context)
        val params = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        relativeLayout.layoutParams = params
        val p2 = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        relativeLayout.addView(mRecyclerView, p2)
        addView(relativeLayout)
    }

    private fun addVertical() {
        mRecyclerView = RecyclerView(context)
        mRecyclerView!!.layoutManager = GridLayoutManager(context, 3)
        mRecyclerView!!.isNestedScrollingEnabled = false
        if (adapter == null) {
            adapter = mAdapter(mContext!!, mData)
            mRecyclerView!!.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }

        val relativeLayout = RelativeLayout(context)
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        relativeLayout.layoutParams = params
        val p2 = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        relativeLayout.addView(mRecyclerView, p2)
        addView(relativeLayout)
    }

    /**
     * 动画
     * */
    private fun startAnim(animTime: Int) {
        mAnimator = ValueAnimator.ofFloat(0f, 1f)
        mAnimator!!.duration = animTime.toLong()
        mAnimator!!.addUpdateListener {
            mPercent = it.animatedValue as Float?
            postInvalidate()
        }
        mAnimator!!.start()
    }


    /**
     * 设置数据
     * */
    fun setData(data: List<PieChartData>): MyPieChartView {
        if (data.isNotEmpty()) {
            for (i in 0 until data.size) {
                mTotalNum = data[i].num!! + mTotalNum!!
            }
        }
        mData!!.addAll(data)
        startAnim(mAnimTime!!)
        if (mLayoutType != "default" && mLayoutType != "pointingInstructions") {
            if (adapter == null) {
                adapter = mAdapter(mContext!!, mData)
                mRecyclerView!!.adapter = adapter
            } else {
                adapter!!.notifyDataSetChanged()
            }
        }
        invalidate()
        return this
    }

    /**
     * 设置显示类型
     * */
    fun setType(type: PieChartType): MyPieChartView {
        this.mType = type
        invalidate()
        return this
    }

    /**
     * 适配器
     * */
    class mAdapter(private val context: Context, private val mData: List<PieChartData>?) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {

            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_piechart, p0, false))
        }

        override fun getItemCount(): Int {
            return mData!!.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            with(holder.itemView) {
                tv_name.text = mData!![position].name
                icon.setBackgroundColor(mData[position].color!!)
            }
        }


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }

}

