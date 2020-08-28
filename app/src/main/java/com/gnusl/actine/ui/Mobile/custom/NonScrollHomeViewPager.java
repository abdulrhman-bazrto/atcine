package com.gnusl.actine.ui.Mobile.custom;

import android.content.Context;

import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.MotionEvent;


public class NonScrollHomeViewPager extends ViewPager {

    private boolean isPagingEnabled = false;
    private int mCurrentPagePosition = 0;
    private Boolean mAnimStarted = false;

    public NonScrollHomeViewPager(Context context) {
        super(context);
    }

    public NonScrollHomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int mode = MeasureSpec.getMode(heightMeasureSpec);
//        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
//        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
//        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
//            // super has to be called in the beginning so the child views can be initialized.
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            int height = 0;
//            for (int i = 0; i < getChildCount(); i++) {
//                View child = getChildAt(i);
//                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//                int h = child.getMeasuredHeight();
//                if (h > height) height = h;
//            }
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        }
//        // super has to be called again so the new specs are treated as exact measurements
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        try {
//            int mode = MeasureSpec.getMode(heightMeasureSpec);
//            // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
//            // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
//            if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
//                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//                View child = getChildAt(mCurrentPagePosition);
//                if (child != null) {
//                    child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//                    int h = child.getMeasuredHeight();
//
//                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

//    public void reMeasureCurrentPage(int position) {
//        mCurrentPagePosition = position;
//        requestLayout();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        if(!mAnimStarted && null != getAdapter()) {
//            int height = 0;
//            View child = null;
//            if(getAdapter() instanceof ViewPagerAdapter){
//
//                child= ((ViewPagerAdapter) getAdapter()).getItem(getCurrentItem()).getView();
//                if (child != null) {
//                    child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//                    height = child.getMeasuredHeight();
//                    if (height < getMinimumHeight()) {
//                        height = getMinimumHeight();
//                    }
//                }
//                int newHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//                if (getLayoutParams().height != 0 && heightMeasureSpec != newHeight) {
//                    final int targetHeight = height;
//                    final int currentHeight = getLayoutParams().height;
//                    final int heightChange = targetHeight - currentHeight;
//
//                    // Not the best place to put this animation, but it works pretty good.
//                    Animation a = new Animation() {
//                        @Override
//                        protected void applyTransformation(float interpolatedTime, Transformation t) {
//                            if (interpolatedTime >= 1) {
//                                getLayoutParams().height = targetHeight;
//                            } else {
//                                int stepHeight = (int) (heightChange * interpolatedTime);
//                                getLayoutParams().height = currentHeight + stepHeight;
//                            }
//                            requestLayout();
//                        }
//
//                        @Override
//                        public boolean willChangeBounds() {
//                            return true;
//                        }
//                    };
//
//                    a.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//                            mAnimStarted = true;
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            mAnimStarted = false;
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//                        }
//                    });
//
//                    a.setDuration(700);
//                    startAnimation(a);
//                    mAnimStarted = true;
//                } else {
//                    heightMeasureSpec = newHeight;
//                }
//            }
//
//        }
//
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
}