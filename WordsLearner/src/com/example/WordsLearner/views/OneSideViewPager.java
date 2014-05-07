package com.example.WordsLearner.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class OneSideViewPager extends ViewPager {

        float lastX = 0;
        boolean lockScroll = false;

        public OneSideViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public OneSideViewPager(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = ev.getX();
                    lockScroll = false;
                    return super.onTouchEvent(ev);
                case MotionEvent.ACTION_MOVE:
                    if (lastX > ev.getX()) {
                        lockScroll = true; //swipe right
                    } else {
                        lockScroll = false; //swipe left
                    }

                    lastX = ev.getX();
                    break;
            }

            lastX = ev.getX();

            if(lockScroll) {
                return false;
            } else {
                return super.onTouchEvent(ev);
            }
        }
    }