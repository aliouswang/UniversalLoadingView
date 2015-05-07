package com.sw.library.widget.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Universal Loading View , it
 * Created by aliouswang on 15/5/6.
 */
public class UniversalLoadingView extends ViewGroup{

    private static final String LOADING_TIP = "加载中...";
    private static final String LOAD_FAILED_TIP = "加载失败\n点击重试";
    private static final String LOAD_EMPTY_TIP = "暂无数据";

    private MaterialCircleView materialCircleView;
    private TextView mTipTextView;

    private int sWidth;
    private int sHeight;

    /**
     * need gradient or not
     */
    private boolean bGradient;

    private int circleColor;
    private int circleWidth;
    private int radius;

    private boolean bTransparent;
    private int alpha;

    private State mLoadState = State.LOADING;

    private Handler mHandler;

    private ReloadListner mReloadListener;

    public boolean isbTransparent() {
        return bTransparent;
    }

    public void setbTransparent(boolean bTransparent) {
        this.bTransparent = bTransparent;
    }

    public enum State{
        GONE,
        LOADING,
        LOADING_FALIED,
        LOADING_EMPTY
    }


    public UniversalLoadingView(Context context) {
        this(context, null);
    }

    public UniversalLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UniversalLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray t = null;
        try {
            t = context.obtainStyledAttributes(attrs, R.styleable.MaterialCircleView,
                    0, defStyleAttr);
            bGradient = t.getBoolean(R.styleable.MaterialCircleView_bGradient, true);
            circleColor = t.getColor(R.styleable.MaterialCircleView_circleColor,
                    getResources().getColor(android.R.color.holo_blue_light));
            circleWidth = t.getDimensionPixelSize(R.styleable.MaterialCircleView_circleWidth,
                    10);
            radius = t.getDimensionPixelSize(R.styleable.MaterialCircleView_radius,
                    MaterialCircleView.dpToPx(50, getResources()));
        } finally {
            if (t != null) {
                t.recycle();
            }
        }

        try {
            t = context.obtainStyledAttributes(attrs, R.styleable.UniversalLoadingView,
                    0, defStyleAttr);
            setbTransparent(t.getBoolean(R.styleable.UniversalLoadingView_bg_transparent, false));
            alpha = t.getDimensionPixelSize(R.styleable.UniversalLoadingView_bg_alpha,
                    255);
        } finally {
            if (t != null) {
                t.recycle();
            }
        }

        materialCircleView = new MaterialCircleView(context, attrs, defStyleAttr);
//        materialCircleView.setbGradient(false);
        addView(materialCircleView);

        mTipTextView = new TextView(context);
        mTipTextView.setText(LOADING_TIP);
        mTipTextView.setTextSize(16f);
        mTipTextView.setGravity(Gravity.CENTER);
        mTipTextView.setSingleLine(false);
        mTipTextView.setMaxLines(2);
        mTipTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        addView(mTipTextView);

        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLoadState == State.LOADING_EMPTY || mLoadState == State.LOADING_FALIED) {
                    if (mReloadListener != null) {
                        mReloadListener.reload();
                    }
                }
            }
        });

        mHandler = new Handler();

        if (isbTransparent()) {
            setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }


    public void postLoadState(State state) {
        mLoadState = state;
        if(mLoadState == State.GONE) {
            this.setVisibility(View.GONE);
        }
        else if (mLoadState == State.LOADING_FALIED) {
            this.setVisibility(View.VISIBLE);
            materialCircleView.setVisibility(View.GONE);
            mTipTextView.setText(LOAD_FAILED_TIP);

            LayoutParams tipParams = (LayoutParams) mTipTextView.getLayoutParams();
            tipParams.top = tipParams.top - 2 * radius;
        }else if(mLoadState == State.LOADING_EMPTY) {
            this.setVisibility(View.VISIBLE);
            materialCircleView.setVisibility(View.VISIBLE);
            mTipTextView.setText(LOAD_EMPTY_TIP);

            LayoutParams tipParams = (LayoutParams) mTipTextView.getLayoutParams();
            tipParams.top = tipParams.top + 2 * radius;
        }
        else if(mLoadState == State.LOADING) {
            this.setVisibility(View.VISIBLE);
            materialCircleView.setVisibility(View.VISIBLE);
            mTipTextView.setText(LOADING_TIP);

            LayoutParams tipParams = (LayoutParams) mTipTextView.getLayoutParams();
            tipParams.top = tipParams.top + 2 * radius;
        }
        requestLayout();
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        LayoutParams params = (LayoutParams) materialCircleView.getLayoutParams();
        sWidth = MeasureSpec.getSize(widthMeasureSpec);
        sHeight = MeasureSpec.getSize(heightMeasureSpec);
        params.left = (sWidth - radius) / 2;
        params.top = (sHeight - radius) / 2 - radius;
        params.width = radius;
        params.height = radius;

        LayoutParams tipParams = (LayoutParams) mTipTextView.getLayoutParams();
        int tipWidth = MaterialCircleView.dpToPx(100, getResources());
        int tipHeight = MaterialCircleView.dpToPx(50, getResources());
        tipParams.left = (sWidth - tipWidth) / 2;
        tipParams.top = (sHeight - radius) / 2 ;
        tipParams.width = tipWidth;
        tipParams.height = tipHeight;

        setMeasuredDimension(sWidth, sHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LayoutParams params = (LayoutParams) materialCircleView.getLayoutParams();
        materialCircleView.layout(params.left, params.top, params.left + params.width
                    , params.top + params.height);
        LayoutParams tipParams = (LayoutParams) mTipTextView.getLayoutParams();
        mTipTextView.layout(tipParams.left, tipParams.top, tipParams.left + tipParams.width,
                tipParams.top + tipParams.height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setTransparent(boolean transparent) {
        this.bTransparent = transparent;
        requestLayout();
        invalidate();
    }

    public boolean getIsTransparent () {
        return this.bTransparent;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(
            AttributeSet attrs) {
        return new UniversalLoadingView.LayoutParams(getContext(), attrs);
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(
            android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof UniversalLoadingView.LayoutParams;
    }

    private static class LayoutParams extends ViewGroup.LayoutParams {

        public int left = 0;
        public int top = 0;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    public void setOnReloadListener(ReloadListner listener) {
        this.mReloadListener = listener;
    }

    /**
     * reload interface
     */
    public interface ReloadListner {
        public void reload();
    }
}
