package com.tompee.utilities.knowyourmeds.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

public class SwipeListView extends ListView {
    private static final int MAX_DISTANCE = 20;
    private boolean mIsResetRequired;
    private boolean mIsSelfResetRequired;
    private int mActivePosition;
    private int mStartX;
    private int mStartY;

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsResetRequired = false;
        mIsSelfResetRequired = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) ev.getX();
                mStartY = (int) ev.getY();
                mActivePosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                if (mActivePosition != AdapterView.INVALID_POSITION) {
                    for (int position = getFirstVisiblePosition(); position <= getLastVisiblePosition();
                         position++) {
                        if (position != mActivePosition) {
                            SwipeListItemView view = (SwipeListItemView) getChildAt(position -
                                    getFirstVisiblePosition());
                            if (view.isResetRequired()) {
                                mIsResetRequired = true;
                            }
                        }
                    }
                    if (mIsResetRequired) {
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePosition != AdapterView.INVALID_POSITION) {
                    SwipeListItemView view = (SwipeListItemView) getChildAt(mActivePosition -
                            getFirstVisiblePosition());
                    if (view.isResetRequired()) {
                        int deltaX = (int) ev.getX() - mStartX;
                        int deltaY = (int) ev.getY() - mStartY;
                        //noinspection SuspiciousNameCombination
                        int distanceWhenActive = (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                        if (distanceWhenActive > MAX_DISTANCE) {
                            mIsSelfResetRequired = true;
                            return true;
                        }
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (mIsResetRequired) {
                    if (mActivePosition != AdapterView.INVALID_POSITION) {
                        for (int position = getFirstVisiblePosition(); position <= getLastVisiblePosition();
                             position++) {
                            if (position != mActivePosition) {
                                SwipeListItemView view = (SwipeListItemView) getChildAt(position -
                                        getFirstVisiblePosition());
                                view.resetView();
                            }
                        }
                    }
                    mIsResetRequired = false;
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                if (mIsSelfResetRequired) {
                    resetCurrentView();
                    return false;
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsSelfResetRequired) {
                    resetCurrentView();
                    mIsSelfResetRequired = false;
                    return false;
                }
        }
        return super.onTouchEvent(ev);
    }

    private void resetCurrentView() {
        if (mActivePosition != AdapterView.INVALID_POSITION) {
            SwipeListItemView view = (SwipeListItemView) getChildAt(mActivePosition -
                    getFirstVisiblePosition());
            view.resetView();
        }
    }

    public void resetVisibleView() {
        for (int position = getFirstVisiblePosition(); position <= getLastVisiblePosition();
             position++) {
            SwipeListItemView view = (SwipeListItemView) getChildAt(position -
                    getFirstVisiblePosition());
            view.resetView();
        }
    }
}