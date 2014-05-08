package com.example.WordsLearner.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class OneSideViewPager extends ViewPager {

        public OneSideViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public OneSideViewPager(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            return false;
        }
    }