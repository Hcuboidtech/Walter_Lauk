package com.walterlauk.utils

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class DividerItemDecorator(var divider: Drawable?) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0..childCount - 2) {
            val child: View = parent.getChildAt(i)
            val params = child.getLayoutParams() as RecyclerView.LayoutParams
            val dividerTop: Int = child.getBottom() + params.bottomMargin
            val dividerBottom = dividerTop + divider!!.intrinsicHeight
            divider!!.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            divider!!.draw(canvas!!)
        }
    }
}