package com.tompee.utilities.knowyourmeds.feature.detail.menu

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.jakewharton.rxbinding2.view.RxView
import com.nineoldandroids.animation.Animator
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseDialog
import com.tompee.utilities.knowyourmeds.model.Type
import kotlinx.android.synthetic.main.dialog_menu.*

class MenuDialog(context: Context,
                 private val listener: MenuDialogListener,
                 private val menuPresenter: MenuPresenter) :
        BaseDialog(context), MenuView, View.OnClickListener, Animator.AnimatorListener {

    private val menuList = mutableListOf<View>()

    //region MenuDialog
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        window?.setBackgroundDrawableResource(R.color.colorPrimaryDarkAlpha)
        super.onCreate(savedInstanceState)
        setCancelable(false)
        setOnShowListener {
            animateViewEntry()
            YoYo.with(Techniques.RotateInDownLeft).playOn(exit)
        }
        setOnDismissListener { resetAnimation() }
        RxView.clicks(exit).subscribe { dismiss() }
        menuPresenter.attachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        menuPresenter.detachView()
    }

    private fun animateViewEntry() {
        for (view in menuList) {
            if (view.visibility != View.VISIBLE) {
                view.visibility = View.VISIBLE
                YoYo.with(Techniques.SlideInUp).duration(100).withListener(this).playOn(view)
                break
            }
        }
    }

    private fun resetAnimation() {
        for (view in menuList) {
            if (view.visibility == View.VISIBLE) {
                view.visibility = View.INVISIBLE
            }
        }
    }
    //endregion

    //region BaseDialog
    override fun layoutId(): Int = R.layout.dialog_menu
    //endregion


    //region AnimatorListener
    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        animateViewEntry()
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }
    //endregion

    //region OnClickListener

    override fun onClick(view: View?) {
        var position = 0
        for (menu in menuList) {
            if (menu.findViewById<View>(R.id.button) === view) {
                listener.onMenuClicked(menu.tag as Type)
                dismiss()
                resetAnimation()
                break
            }
            position++
        }
    }

    //endregion

    //region MenuView
    @SuppressLint("InflateParams")
    override fun createMenuView(stringId: Int, colorId: Int, type: Type) {
        val menuView = LayoutInflater.from(context).inflate(R.layout.view_menu, null)
        menuView.visibility = View.INVISIBLE
        menuView.tag = type
        val textView = menuView.findViewById<View>(R.id.text) as TextView
        textView.setText(stringId)
        val button = menuView.findViewById<FloatingActionButton>(R.id.button)
        button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorId))
        button.setOnClickListener(this)
        menuList.add(menuView)
        container.addView(menuView)
    }
    //endregion
}