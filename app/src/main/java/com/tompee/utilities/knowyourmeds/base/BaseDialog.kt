package com.tompee.utilities.knowyourmeds.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater

abstract class BaseDialog(context: Context) : Dialog(context, android.R.style.Theme_Wallpaper_NoTitleBar) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LayoutInflater.from(context).inflate(layoutId(), null))
    }

    @LayoutRes
    abstract fun layoutId(): Int
}