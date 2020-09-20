package com.andruid.magic.helpfulsense.ui.custom

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ItemClickListener(context: Context, recyclerView: RecyclerView) :
        RecyclerView.OnItemTouchListener {
    private val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    recyclerView.findChildViewUnder(e.x, e.y)?.let { child ->
                        onLongClick(child, recyclerView.getChildAdapterPosition(child))
                    }
                }
            })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        rv.findChildViewUnder(e.x, e.y)?.let { child ->
            if (gestureDetector.onTouchEvent(e))
                onClick(child, rv.getChildAdapterPosition(child))
        }

        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    open fun onClick(view: View, position: Int) {}
    open fun onLongClick(view: View, position: Int) {}
}