package com.staqu.healthmonitoring

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomDividerItemDecoration(context: Context, orientation: Int, margin: Int)
    : RecyclerView.ItemDecoration() {


    private val ATTRS: IntArray = intArrayOf(android.R.attr.listDivider)
    private val HORIZONTAL_LIST: Int = LinearLayoutManager.HORIZONTAL
    val VERTICAL_LIST: Int = LinearLayoutManager.VERTICAL

    private var mDivider: Drawable? = null
    private var context: Context = context
    private var mOrientation: Int = 0
    private var margin: Int = 0

    init {
        this.margin = margin
        val a: TypedArray = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
        setOrientation(orientation)
    }

    fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw IllegalArgumentException("invalid orientation")
        }
        mOrientation = orientation
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight
            mDivider?.setBounds(left + dpToPx(margin), top, right - dpToPx(margin), bottom);
            mDivider?.draw(c)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {

        val top: Int = parent.paddingTop
        val bottom: Int = parent.height - parent.paddingBottom
        val childCount: Int = parent.childCount


        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + mDivider!!.intrinsicHeight
            mDivider?.setBounds(left, top + dpToPx(margin), right, bottom - dpToPx(margin));
            mDivider?.draw(c)
        }
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
        } else {
            outRect.set(0, 0, mDivider!!.intrinsicWidth, 0)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val r: Resources = context.resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }
}