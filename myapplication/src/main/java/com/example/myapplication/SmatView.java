package com.example.myapplication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Util.DisplayUtil;

import java.util.Date;

/**
 * @author ddddfg
 * @version 0.0
 *          Created by Tang on 2016/9/20.
 */
public class SmatView extends View {


    /**
     * 表盘外框的颜色
     */
    public static final int OUTRINGCOLOR = 0xFFF8F8F8;
    /**
     * 阴影
     */
    public static final int SHADOW_LAYER = 0x80000000;
    /**
     * 表盘外框的半径
     */
    public static final int OUTRINGRADIUS = 4;
    /**
     * 表盘最小的长度
     */
    public static final int MIN_SIZE = 200;
    /**
     * 外环的半径
     */
    public static final int RINGWIDTHPAINT = 10;
    /**
     * TAG
     */
    public static final String TAG = "TZF";
    /**
     * 表盘的颜色
     */
    public static final int OUTCIRCLECOLOR = 0xFFF0F0F0;
    /**
     * 中心红色小原点的颜色
     */
    public static final int INCIRCLECOLOR = 0xFFB55050;
    /**
     * 文本的颜色
     */
    public static final int TEXT_COLOR = 0xFF141414;
    /**
     * 文本的大小
     */
    public static final int TEXT_SIZE = 15;
    /**
     * 时针、分针共同的颜色
     */
    public static final int HOUR_MINUTE_COLOR = 0xFF141414;
    /**
     * 时针、分针的宽度
     */
    public static final int HOUR_MINUTE_WIDTH = 30;
    /**
     * 秒针颜色
     */
    public static final int SECOND_COLOR = 0xFFB55050;
    /**
     * 秒针宽度
     */
    public static final int SECOND_WIDTH = 8;

    /**
     * 表盘外层的Paint
     */
    private Paint outRingPaint;

    /**
     * Activity Context
     */
    private Context context;
    /**
     * 表盘的大小
     */
    private int size;

    /**
     * 绘制表盘的Paint
     */
    private Paint outCirclePaint;

    /**
     * 圆心的红点
     */
    private Paint inRedCirclePaint;

    /**
     * 时针画笔
     */
    private Paint hourPaint;
    /**
     * 分钟画笔
     */
    private Paint minutePaint;
    /**
     * 秒钟画笔
     */
    private Paint secondPaint;


    /**
     * 时间文本
     */
    private Paint timeTimePaint;

    /**
     * 中心的黑点
     */
    private Paint inCirclePaint;

    /**
     * 时间
     */
    private Calendar calendar;

    /**
     * 当前小时
     */
    private int hour;
    /**
     * 当前分钟
     */
    private int minute;
    /**
     * 当前多少秒
     */
    private int second;

    public SmatView(Context context) {
        super(context);
        this.context = context;
        initPaint();

    }

    public SmatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initPaint();

    }

    public SmatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();

    }

    /**
     * 初始化所有的Paint
     * 这里一定要加上 @SuppressLint("NewApi")
     * 同时里面要加上setLayerType(LAYER_TYPE_SOFTWARE,null);
     * 这样关闭硬件加速 才可以显示阴影
     */
    @SuppressLint("NewApi")
    private void initPaint() {

        calendar = Calendar.getInstance();
        outRingPaint = new Paint();
        outRingPaint.setColor(OUTRINGCOLOR);
        outRingPaint.setStyle(Paint.Style.STROKE);
        outRingPaint.setAntiAlias(true);

        // TODO: 2016/9/21 这里为什么需要使用到dip转换px？

        outRingPaint.setStrokeWidth(DisplayUtil.dip2px(context, RINGWIDTHPAINT));
        outRingPaint.setShadowLayer(OUTRINGRADIUS, 2, 2, SHADOW_LAYER);


        outCirclePaint = new Paint();
        outCirclePaint.setColor(OUTCIRCLECOLOR);
        outCirclePaint.setAntiAlias(true);
        outCirclePaint.setShadowLayer(5, 0, 0, SHADOW_LAYER);

        inRedCirclePaint = new Paint();
        inRedCirclePaint.setAntiAlias(true);
        inRedCirclePaint.setColor(INCIRCLECOLOR);
//        inRedCirclePaint.setShadowLayer(4,0,0,SHADOW_LAYER);

        timeTimePaint = new Paint();
        timeTimePaint.setAntiAlias(true);
        timeTimePaint.setColor(TEXT_COLOR);
        timeTimePaint.setTextSize(DisplayUtil.dip2px(context, TEXT_SIZE));
//        timeTimePaint.setShadowLayer(4,0,0,SHADOW_LAYER);

        hourPaint = new Paint();
        hourPaint.setAntiAlias(true);
        hourPaint.setShadowLayer(5, 0, 0, SHADOW_LAYER);
        hourPaint.setColor(HOUR_MINUTE_COLOR);
        hourPaint.setStrokeCap(Paint.Cap.ROUND);
        hourPaint.setStrokeWidth(HOUR_MINUTE_WIDTH );

        minutePaint = new Paint();
        minutePaint.setStrokeWidth(HOUR_MINUTE_WIDTH  );
        minutePaint.setAntiAlias(true);
        minutePaint.setShadowLayer(5, 0, 0, SHADOW_LAYER);
        minutePaint.setColor(HOUR_MINUTE_COLOR);

        minutePaint.setStrokeCap(Paint.Cap.ROUND);


        secondPaint = new Paint();
        secondPaint.setAntiAlias(true);
        secondPaint.setColor(SECOND_COLOR);
        secondPaint.setStrokeWidth(SECOND_WIDTH);
        secondPaint.setShadowLayer(4, 0, 0, SHADOW_LAYER);
        secondPaint.setStrokeCap(Paint.Cap.ROUND);

        inCirclePaint = new Paint();
        inCirclePaint.setAntiAlias(true);
        inCirclePaint.setShadowLayer(4, 0, 0, SHADOW_LAYER);
        inCirclePaint.setColor(HOUR_MINUTE_COLOR);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    /**
     * 这个函数主要是为了在我们XML中的
     * Match_Parent与Wrap_content有区别
     * 最后用setMeasuredDimension方法将
     * 他们Width与Height保存下来。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        size = DisplayUtil.dip2px(context, MIN_SIZE);
        setMeasuredDimension(size, size);
    }

    /**
     * 绘制
     */

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calendar.setTime(new Date());
        getTime();

        canvas.translate(size / 2, size / 2);

        drawOutCircle(canvas);
        drawOutRing(canvas);

        drawTimeTimePaint(canvas);
        drawHour(canvas);
        drawMinute(canvas);
        drawInCirclePaint(canvas);
        drawSecond(canvas);

        drawInRedCirclePaint(canvas);

        postInvalidateDelayed(1000);
        Log.i(TAG, "onDraw: ------------------------------");

    }


    /**
     * 获取时间
     */
    @TargetApi(Build.VERSION_CODES.N)
    private void getTime() {
        hour=calendar.get(Calendar.HOUR)+8;
        minute =calendar.get(Calendar.MINUTE);
        second=calendar.get(Calendar.SECOND);
        Log.i(TAG, "getTime: "+hour+","+minute+","+second);
    }

    /**
     * 绘制中心黑点
     *
     * @param canvas 该View的总Canvas（帆布）
     */
    private void drawInCirclePaint(Canvas canvas) {
        int radius = size / 20;
        canvas.save();
        canvas.drawCircle(0, 0, radius, inCirclePaint);
        canvas.restore();
    }

    /**
     * 绘制秒针
     *
     * @param canvas 该View的总Canvas（帆布）
     */
    private void drawSecond(Canvas canvas) {
        int length = size / 2;
        canvas.save();
        float degrees= (float) (second*6);
        Log.i(TAG, "drawSecond: "+second);
        canvas.rotate(degrees, 0, 0);
        canvas.drawLine(0, length / 5, 0, -length * 4 / 5, secondPaint);
        canvas.restore();
    }

    /**
     * 绘制分针
     *
     * @param canvas 该View的总Canvas（帆布）
     */
    private void drawMinute(Canvas canvas) {
        int length = size / 3;
        canvas.save();
        float degrees= (float) (minute*6+second*0.05);
        Log.i(TAG, "drawMinute: "+minute);
        canvas.rotate(degrees, 0, 0);
        canvas.drawLine(0, 0, 0, -length, minutePaint);
        canvas.restore();
    }

    /**
     * 绘制时针
     *
     * @param canvas 该View的总Canvas（帆布）
     */
    private void drawHour(Canvas canvas) {
        int length = size / 4;
        canvas.save();
        float degrees= (float) (hour%12*30+minute*0.6);
//        Log.i(TAG, "drawHour: degrees--->"+degrees+",hour--->"+hour);
        canvas.rotate(degrees,0,0);
        canvas.drawLine(0, 0, 0, -length, hourPaint);
        canvas.restore();
    }

    /**
     * 绘制时间刻度
     *
     * @param canvas 该View的总Canvas（帆布）
     */
    private void drawTimeTimePaint(Canvas canvas) {
        int radius = size / 2 - DisplayUtil.dip2px(context, RINGWIDTHPAINT) / 2;
        canvas.save();
        float scaleWidth = timeTimePaint.measureText("12");
        canvas.drawText("12", -scaleWidth / 2, -radius + 30 + DisplayUtil.dip2px(context, TEXT_SIZE), timeTimePaint);
        canvas.rotate(90, 0, 0);
        scaleWidth = timeTimePaint.measureText("3");
        canvas.drawText("3", -scaleWidth / 2, -radius + 30 + DisplayUtil.dip2px(context, TEXT_SIZE), timeTimePaint);
        canvas.rotate(-90, 0, 0);
        canvas.restore();
    }

    /**
     * 绘制中心的红色小圆点
     *
     * @param canvas 这个是总界面的帆布
     */
    private void drawInRedCirclePaint(Canvas canvas) {
        canvas.save();
        Log.i(TAG, "drawInRedCirclePaint: ");
        canvas.drawCircle(0, 0, size / 40, inRedCirclePaint);
        canvas.restore();
    }

    /**
     * 绘制OutCircle
     *
     * @param canvas 这个是总界面的帆布
     */
    private void drawOutCircle(Canvas canvas) {
        canvas.save();
        canvas.drawCircle(0, 0, size / 2 - OUTRINGRADIUS * 2, outCirclePaint);
        canvas.restore();
    }


    /**
     * @param canvas 这个是总界面的帆布
     */
    private void drawOutRing(Canvas canvas) {
        canvas.save();
        // TODO: 2016/9/21  这里还需要进一步解析
        float radius = size / 2 - DisplayUtil.dip2px(context, RINGWIDTHPAINT + 6) / 2;
        RectF rectF = new RectF(-radius, -radius, radius, radius);
        outRingPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF, 0, 360, false, outRingPaint);
        Log.i(TAG, "drawOutRing: ");
        canvas.restore();
    }
}
