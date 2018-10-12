package com.tompee.utilities.knowyourmeds.feature.detail.property

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class DividerDecorator(private val height: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount!! - 1) {
            outRect.bottom = height
        }
    }
}