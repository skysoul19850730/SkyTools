package com.skysoul.album.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

import com.skysoul.album.bean.MediaType;

public class RecordBtn extends View {
    private float circleWidth;//外圆环宽度
    private int outCircleColor = Color.parseColor("#E0E0E0");//外圆颜色
    private int innerCircleColor = Color.WHITE;//内圆颜色
    private int progressColor = Color.RED;//进度条颜色
    private final Paint outRoundPaint = new Paint(); //外圆画笔
    private final Paint mCPaint = new Paint();//进度画笔
    private final Paint innerRoundPaint = new Paint();
    private float width; //自定义view的宽度
    private float height; //自定义view的高度
    private float outRaduis; //外圆半径
    private float innerRaduis;//内圆半径
    private boolean isLongClick;//是否长按
    private float mSweepAngle;//扫过的角度
    private int mLoadingTime = 30;
    private ValueAnimator angleAnimator;
    MediaType mMediaType = MediaType.ALL;

    public RecordBtn(Context context) {
        this(context, null);
    }

    public RecordBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置最多录制的时长
     */
    public void setLoadingTime(int loadingTime) {
        this.mLoadingTime = loadingTime;
    }

    public void setOutCircleColor(int outCircleColor) {
        this.outCircleColor = outCircleColor;
    }

    public void setInnerCircleColor(int innerCircleColor) {
        this.innerCircleColor = innerCircleColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public void setMediaType(MediaType type) {
        mMediaType = type;
    }

    private void resetParams() {
        width = getWidth();
        height = getHeight();
        circleWidth = width * 0.13f;
        outRaduis = (float) (Math.min(width, height) / 2.4);
        innerRaduis = outRaduis - circleWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width > height) {
            setMeasuredDimension(height, height);
        } else {
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        resetParams();
        //画外圆
        outRoundPaint.setAntiAlias(true);
        outRoundPaint.setColor(outCircleColor);
        if (isLongClick) {
            canvas.scale(1.2f, 1.2f, width / 2, height / 2);
        }
        canvas.drawCircle(width / 2, height / 2, outRaduis, outRoundPaint);
        //画内圆
        innerRoundPaint.setAntiAlias(true);
        innerRoundPaint.setColor(innerCircleColor);
        if (isLongClick) {
            canvas.drawCircle(width / 2, height / 2, innerRaduis / 2.0f, innerRoundPaint);
            //画外原环
            mCPaint.setAntiAlias(true);
            mCPaint.setColor(progressColor);
            mCPaint.setStyle(Paint.Style.STROKE);
            mCPaint.setStrokeCap(Paint.Cap.ROUND);
            mCPaint.setStrokeWidth(circleWidth / 2);
            RectF rectF = new RectF(0 + circleWidth, 0 + circleWidth, width - circleWidth, height - circleWidth);
            //开始角度
            float startAngle = -90;
            canvas.drawArc(rectF, startAngle, mSweepAngle, false, mCPaint);
        } else {
            canvas.drawCircle(width / 2, height / 2, innerRaduis, innerRoundPaint);
        }
    }

    public void start() {
        //起点
        float mmSweepAngleStart = 0f;
        //终点
        float mmSweepAngleEnd = 360f;
        angleAnimator = ValueAnimator.ofFloat(mmSweepAngleStart, mmSweepAngleEnd);
        angleAnimator.setInterpolator(new LinearInterpolator());
        angleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSweepAngle = (float) valueAnimator.getAnimatedValue();
                //获取到需要绘制的角度，重新绘制
                invalidate();
            }
        });
        angleAnimator.setDuration(mLoadingTime * 1000);
        angleAnimator.setInterpolator(new LinearInterpolator());
        angleAnimator.addListener(new AnimatorListenerAdapter() {
            boolean isCancel = false;
            @Override
            public void onAnimationCancel(Animator animation) {
                isCancel = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clearAnimation();
                if (!isCancel) {
                    isLongClick = false;
                    angleAnimator = null;
                    postInvalidate();
                    if (listener != null) {
                        listener.onAnimatorFinish();
                    }
                }
            }
        });
        angleAnimator.start();
    }

    private long lastOnClickTime = 0L;
    private final Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMediaType != MediaType.IMAGE) {
                isLongClick = true;
                start();
                if (listener != null) {
                    listener.onLongClick();
                }
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isLongClick = false;
                postDelayed(longPressRunnable, ViewConfiguration.getLongPressTimeout());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isLongClick) {
                    isLongClick = false;
                    postInvalidate();
                    if (angleAnimator != null) {
                        angleAnimator.cancel();
                        angleAnimator = null;
                    }
                    if (this.listener != null) {
                        this.listener.onLongClickUp();
                    }
                } else {
                    // 点击
                    removeCallbacks(longPressRunnable);
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - lastOnClickTime > 300) {
                        lastOnClickTime = currentTimeMillis;
                        if (listener != null && (mMediaType == MediaType.IMAGE || mMediaType == MediaType.ALL)) {
                            listener.onClick();
                        }
                    }
                }
                break;
        }
        return true;
    }

    private OnProgressTouchListener listener;

    public void setOnProgressTouchListener(OnProgressTouchListener listener) {
        this.listener = listener;
    }

    /**
     * 进度触摸监听
     */
    public interface OnProgressTouchListener {
        /**
         * 单击
         */
        void onClick();

        /**
         * 长按
         */
        void onLongClick();

        /**
         * 长按抬起
         */
        void onLongClickUp();

        /**
         * 动画结束
         */
        void onAnimatorFinish();
    }
}
