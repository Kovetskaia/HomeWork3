package com.example.nastya.homework3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class MyLayout extends ViewGroup {

    public int position;
    int upperDistance = 20;
    float deviceWidth;

    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyLayout);
        position = typedArray.getInt(R.styleable.MyLayout_android_gravity, Gravity.LEFT);
        typedArray.recycle();
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int currentRowTop = 0;
        int totalHeight = 0;
        int currentMaxHeight = 0;
        int totalWidth = 0;
        int currentMaxWidth = 0;
        int countRow = 1;
        int distance = 0;
        int height = 0;
        int count = getChildCount();


        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            distance = layoutParams.distance;
            height = layoutParams.myHeight;

            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                currentMaxWidth += child.getMeasuredWidth() + distance;
                totalWidth = Math.max(totalWidth, child.getMeasuredWidth() + distance);

                if ((currentMaxWidth / deviceWidth) >= 1) {
                    currentRowTop = totalHeight;
                    currentMaxHeight = height + upperDistance;
                    currentMaxWidth = child.getMeasuredWidth() + distance;
                    countRow++;
                } else {
                    totalWidth = Math.max(totalWidth, (int) currentMaxWidth);
                    currentMaxHeight = Math.max(currentMaxHeight, height + upperDistance);
                }
                totalHeight = Math.max(currentRowTop + currentMaxHeight, totalHeight);
            }
            child.measure(MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }
        if ((heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) & totalHeight > heightSize) {
            int limitHeight = (int) (heightSize - upperDistance * countRow) / countRow;
            if ((widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) & totalWidth > widthSize) {
                int averageCount = count / countRow;
                int limitWidth = (int) (widthSize - distance * averageCount) / averageCount;
                for (int i = 0; i < count; i++) {
                    final View child = getChildAt(i);
                    child.measure(MeasureSpec.makeMeasureSpec(limitWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(limitHeight, MeasureSpec.AT_MOST));
                }
                totalWidth = widthSize;
                totalHeight = heightSize;
            } else {
                for (int i = 0; i < count; i++) {
                    final View child = getChildAt(i);
                    child.measure(MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(limitHeight, MeasureSpec.AT_MOST));
                }
                totalHeight = heightSize;
            }

        } else if ((widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) & totalWidth > widthSize) {

            int limitWidth = (int) (widthSize - distance * count) / (count / countRow);
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                child.measure(MeasureSpec.makeMeasureSpec(limitWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.AT_MOST));
            }
            totalWidth = widthSize;
        }

        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int currentWidth, currentHeight;

        final int leftPosition = getPaddingLeft();
        final int topPosition = getPaddingTop();
        final int rightPosition = right - left - getPaddingRight();

        int currentLeft = leftPosition;
        int currentRight = rightPosition;
        int currentTop = topPosition + upperDistance;
        int maxHeight = 0;
        int distance = 0;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            distance = layoutParams.distance;

            if (child.getVisibility() != GONE) {
                currentWidth = child.getMeasuredWidth() + distance;
                currentHeight = child.getMeasuredHeight();
                if (position == Gravity.RIGHT) {
                    if ((currentRight - currentWidth) < leftPosition) {
                        currentRight = rightPosition;
                        currentTop += maxHeight + upperDistance;
                        maxHeight = 0;
                    }
                    child.layout(currentRight - currentWidth,
                            currentTop,
                            currentRight - distance,
                            currentTop + currentHeight);

                    maxHeight = Math.max(maxHeight, currentHeight);
                    currentRight -= currentWidth;
                } else {
                    if (currentLeft + currentWidth > rightPosition) {
                        currentLeft = leftPosition;
                        currentTop += maxHeight + upperDistance;
                        maxHeight = 0;
                    }
                    child.layout(currentLeft + distance, currentTop, currentLeft + currentWidth, currentTop + currentHeight);
                    if (maxHeight < currentHeight)
                        maxHeight = currentHeight;
                    currentLeft += currentWidth;
                }
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }


    public static class LayoutParams extends MarginLayoutParams {
        int distance = 20;
        int myHeight = 100;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MyLayout);
            distance = a.getDimensionPixelOffset(R.styleable.MyLayout_ml_distance, distance);
            myHeight = a.getDimensionPixelOffset(R.styleable.MyLayout_ml_my_height, myHeight);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
