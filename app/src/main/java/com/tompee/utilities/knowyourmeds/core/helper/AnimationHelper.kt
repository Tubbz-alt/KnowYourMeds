package com.tompee.utilities.knowyourmeds.core.helper

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Interpolator

object AnimationHelper {
    private const val VERTICAL_POSITION_PROPERTY = "Y"
    private const val VERTICAL_TRANSLATION_PROPERTY = "translationY"

    fun animateVerticalPosition(view: View, pxHeight: Float, duration: Int) {
        animateVerticalPosition(view, pxHeight, duration, null, 0, null)
    }

    fun animateVerticalPosition(view: View, pxHeight: Float, duration: Int, interpolator: Interpolator) {
        animateVerticalPosition(view, pxHeight, duration, interpolator, 0, null)
    }

    private fun animateVerticalPosition(view: View, pxHeight: Float, duration: Int,
                                        interpolator: Interpolator?, delay: Int,
                                        listener: Animator.AnimatorListener?) {
        val moveAnim = ObjectAnimator.ofFloat(view, VERTICAL_POSITION_PROPERTY, pxHeight)
        moveAnim.duration = duration.toLong()
        moveAnim.startDelay = delay.toLong()
        if (interpolator != null) {
            moveAnim.interpolator = interpolator
        }
        if (listener != null) {
            moveAnim.addListener(listener)
        }
        moveAnim.start()
    }

    fun animateVerticalTranslation(view: View, pxHeight: Float, duration: Int) {
        animateVerticalTranslation(view, pxHeight, duration, null, 0, null)
    }

    fun animateVerticalTranslation(view: View, pxHeight: Float, duration: Int,
                                   listener: Animator.AnimatorListener) {
        animateVerticalTranslation(view, pxHeight, duration, null, 0, listener)
    }

    private fun animateVerticalTranslation(view: View, pxHeight: Float, duration: Int,
                                           interpolator: Interpolator?, delay: Int,
                                           listener: Animator.AnimatorListener?) {
        val moveAnim = ObjectAnimator.ofFloat(view, VERTICAL_TRANSLATION_PROPERTY, pxHeight)
        moveAnim.duration = duration.toLong()
        moveAnim.startDelay = delay.toLong()
        if (interpolator != null) {
            moveAnim.setInterpolator(interpolator)
        }
        if (listener != null) {
            moveAnim.addListener(listener)
        }
        moveAnim.start()
    }
}