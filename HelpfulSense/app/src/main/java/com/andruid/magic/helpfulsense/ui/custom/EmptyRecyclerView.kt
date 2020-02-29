package com.andruid.magic.helpfulsense.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.getResourceIdOrThrow
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.eezetensions.hide
import com.andruid.magic.eezetensions.show

/**
 * Custom [RecyclerView] for showing empty [View] when no data is available
 */
class EmptyRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RecyclerView(context, attrs, defStyleAttr) {
    private lateinit var emptyView: View

    private var emptyViewID: Int
    private var emptyClickListener: ((View) -> Unit)? = null

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
        emptyViewID = a.getResourceIdOrThrow(R.styleable.EmptyRecyclerView_emptyView)
        a.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        emptyView = (parent as View).findViewById<View>(emptyViewID).apply {
            setOnClickListener(emptyClickListener)
        }
        emptyObserver.onChanged()
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.let {
            it.registerAdapterDataObserver(emptyObserver)
            emptyObserver.onChanged()
        }
    }

    /**
     * Set onClick for when empty view is clicked
     * @param l lambda action as listener
     */
    fun setEmptyViewClickListener(l: (View) -> Unit) {
        emptyClickListener = l
    }

    /**
     * Hide [emptyView] if data is available else show it if dataSet is empty
     */
    private fun toggleVisibility() {
        if (!::emptyView.isInitialized)
            return
        if (adapter?.itemCount == 0) {
            emptyView.show()
            hide()
        } else {
            emptyView.hide()
            show()
        }
    }
}