package com.andruid.magic.helpfulsense.ui.adapter

/**
 * Interface for swiping a view left/right or dragging it top/bottom
 */
interface SwipeListener {
    /**
     * Callback for swipe left/right
     */
    fun onSwipe(position: Int, direction: Int)
    /**
     * Callback for drag top/bottom
     */
    fun onMove(fromPosition: Int, toPosition: Int)
}