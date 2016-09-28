package com.tompee.utilities.knowyourmeds.view.custom;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.tompee.utilities.knowyourmeds.model.ListSwipeHolder;

public class SwipeListItemView extends FrameLayout {
    private static final String HORIZONTAL_TRANSLATION_PROPERTY = "translationX";
    private static final int MIN_LOCK_DISTANCE = 50;
    private static final int ANIMATION_DURATION = 150;
    private OnDeleteClickListener mListener;
    private int mStartX;
    private boolean mIsSwipeEnabled;
    private boolean mIsSwipeDetected;
    private ViewState mViewState;
    private ListSwipeHolder mListHolder;
    private int mPreviousBackgroundColor;
    private ColorDrawable mOnClickBackgroundColor;

    public SwipeListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsSwipeEnabled = true;
        setViewState(ViewState.CLOSED);
        mIsSwipeDetected = false;
        mOnClickBackgroundColor = null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mListener = (OnDeleteClickListener) ((AdapterView) getParent()).getAdapter();
        mListHolder = (ListSwipeHolder) getTag();
    }

    public void setEnableSwipeDetection(boolean mode) {
        mIsSwipeEnabled = mode;
    }

    public void setOnClickBackgroundColor(int resId) {
        mOnClickBackgroundColor = new ColorDrawable(ContextCompat.getColor(getContext(), resId));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getX();
                mIsSwipeDetected = false;
                if (getViewState() == ViewState.CLOSED && mOnClickBackgroundColor != null) {
                    mPreviousBackgroundColor = ((ColorDrawable) mListHolder.getFrontView().
                            getBackground()).getColor();
                    mListHolder.getFrontView().setBackgroundColor(mOnClickBackgroundColor.getColor());
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!mIsSwipeEnabled) {
                    return true;
                }
                int deltaX = (int) event.getX() - mStartX;
                if (deltaX > MIN_LOCK_DISTANCE) {
                    mIsSwipeDetected = true;
                    performSlideToRight();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (mOnClickBackgroundColor != null) {
                    mListHolder.getFrontView().setBackgroundColor(mPreviousBackgroundColor);
                }
                if (getViewState() == ViewState.OPEN) {
                    if (!mIsSwipeDetected) {
                        if (mStartX < mListHolder.getBackView().getWidth()) {
                            performOnDeleteClick();
                        } else {
                            performSlideToLeft();
                        }
                    }
                } else {
                    performAdapterViewItemClick();
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                if (mOnClickBackgroundColor != null) {
                    mListHolder.getFrontView().setBackgroundColor(mPreviousBackgroundColor);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void performOnDeleteClick() {
        AdapterView view = (AdapterView) getParent();
        int position = view.getPositionForView(SwipeListItemView.this);
        mListener.onDeleteClick(position);
    }

    private void performAdapterViewItemClick() {
        AdapterView view = (AdapterView) getParent();
        int position = view.getPositionForView(SwipeListItemView.this);
        if (position != AdapterView.INVALID_POSITION) {
            view.performItemClick(view.getChildAt(position - view.getFirstVisiblePosition()),
                    position, view.getAdapter().getItemId(position));
        }
    }

    private void performSlideToRight() {
        if (getViewState() == ViewState.CLOSED) {
            getParent().requestDisallowInterceptTouchEvent(true);
            ObjectAnimator anim = ObjectAnimator.ofFloat(mListHolder.getFrontView(),
                    HORIZONTAL_TRANSLATION_PROPERTY, mListHolder.getBackView().getWidth());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
            setViewState(ViewState.OPEN);
        }
    }

    private void performSlideToLeft() {
        if (getViewState() == ViewState.OPEN) {
            getParent().requestDisallowInterceptTouchEvent(true);
            ObjectAnimator anim = ObjectAnimator.ofFloat(mListHolder.getFrontView(),
                    HORIZONTAL_TRANSLATION_PROPERTY, 0);
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
            setViewState(ViewState.CLOSED);
        }
    }

    public void resetView() {
        performSlideToLeft();
    }

    public boolean isResetRequired() {
        return getViewState() == ViewState.OPEN;
    }

    private ViewState getViewState() {
        return mViewState;
    }

    private void setViewState(ViewState state) {
        mViewState = state;
    }

    public enum ViewState {
        OPEN,
        CLOSED,
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}
