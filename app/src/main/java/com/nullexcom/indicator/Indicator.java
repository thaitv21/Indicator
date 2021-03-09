package com.nullexcom.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;

public class Indicator extends View {

    private int count = 4;
    private int currentIndex = 2;
    private float offset = 0.5f;
    private float factor = 5f;
    private Paint indicatorPaint = new Paint();
    private Paint selectedPaint = new Paint();

    public Indicator(Context context) {
        super(context);
        init();
    }

    public Indicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        indicatorPaint.setStyle(Paint.Style.STROKE);
        indicatorPaint.setStrokeWidth(dp(1));
        indicatorPaint.setAntiAlias(true);

        selectedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        selectedPaint.setStrokeWidth(dp(1));
        selectedPaint.setAntiAlias(true);
    }

    private int dp(int dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return dp * metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            height = dp(10);
        }
        int r = height / 2;
        if (widthMode != MeasureSpec.EXACTLY) {
            width = (int) (count * factor * r) + 3 * r;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int size = height - dp(1);
        int r = size / 2;
        int cell = (int) (factor * r);
        for (int i = 0; i < count; i++) {
            if (i < currentIndex) {
                int cx = i * cell + cell / 2;
                if (offset < 0 && i == currentIndex - 1) {
                    int length = (int) (2 * r * (1 + Math.abs(offset)));
                    canvas.drawRoundRect(cx - r, dp(1), cx - r + length, height - dp(1), r, r, indicatorPaint);
                } else {
                    canvas.drawRoundRect(cx - r, dp(1), cx + r, height - dp(1), r, r, indicatorPaint);
                }
            } else if (i > currentIndex) {
                int cx = i * cell + cell / 2 + 3 * r;
                if (offset > 0 && i == currentIndex + 1) {
                    int length = (int) (2 * r * (1 + Math.abs(offset)));
                    canvas.drawRoundRect(cx + r - length, dp(1), cx + r, height - dp(1), r, r, indicatorPaint);
                } else {
                    canvas.drawRoundRect(cx - r, dp(1), cx + r, height - dp(1), r, r, indicatorPaint);
                }
            } else {
                int cx = i * cell + cell / 2 + 3 * r / 2;
                int length = Math.max(2 * r, (int) (cell * (1 - Math.abs(offset))));
                canvas.drawRoundRect(cx - length / 2f, dp(1), cx + length / 2f, height - dp(1), r, r, selectedPaint);
            }
        }
    }

    private void handleOffsetChanged(int index, float offset) {
        if (index < 0 || index > count) throw new IllegalArgumentException();
        currentIndex = index;
        this.offset = offset;
        invalidate();
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            handleOffsetChanged(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void attachToViewPager(ViewPager viewPager) {
        this.count = Objects.requireNonNull(viewPager.getAdapter()).getCount();
        viewPager.removeOnPageChangeListener(onPageChangeListener);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }
}
