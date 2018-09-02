package com.eugenetereshkov.testtinkoff.ui.global

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.eugenetereshkov.testtinkoff.R


class SimpleDividerDecorator(
        private val dividerSizePx: Int,
        dividerColor: Int
) : RecyclerView.ItemDecoration() {
    private val paint = Paint().apply { color = dividerColor }

    constructor(context: Context) : this(
            context.resources.getDimensionPixelSize(R.dimen.divider_size),
            ContextCompat.getColor(context, R.color.divider)
    )

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) return

        outRect.top = dividerSizePx
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + dividerSizePx

            val divRect = Rect(dividerLeft, dividerTop, dividerRight, dividerBottom)
            canvas.drawRect(divRect, paint)
        }
    }
}
