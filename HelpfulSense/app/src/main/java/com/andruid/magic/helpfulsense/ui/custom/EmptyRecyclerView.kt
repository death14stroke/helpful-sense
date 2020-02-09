package com.andruid.magic.helpfulsense.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.getResourceIdOrThrow
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.util.hide
import com.andruid.magic.helpfulsense.util.show

class EmptyRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RecyclerView(context, attrs, defStyleAttr) {
    private var emptyView: View
    private val emptyObserver = object : AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            toggleVisibility()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            toggleVisibility()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            toggleVisibility()
        }

        override fun onChanged() {
            super.onChanged()
            toggleVisibility()
        }
    }

    init {
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.EmptyRecyclerView, defStyleAttr, 0)
        val emptyViewID = a.getResourceIdOrThrow(R.styleable.EmptyRecyclerView_emptyView)
        emptyView = findViewById(emptyViewID)
        a.recycle()
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.let {
            it.registerAdapterDataObserver(emptyObserver)
            emptyObserver.onChanged()
        }
    }

    fun setEmptyViewClickListener(l: (View) -> Unit) {
        emptyView.setOnClickListener(l)
    }

    private fun toggleVisibility() {
        if (adapter?.itemCount == 0) {
            emptyView.show()
            hide()
        } else {
            emptyView.hide()
            show()
        }
    }
}