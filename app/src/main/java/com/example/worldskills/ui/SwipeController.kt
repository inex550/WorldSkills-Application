package com.example.worldskills.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskills.ui.adapters.ShablonsAdapter

class SwipeController: ItemTouchHelper.Callback() {

    private var swipeBack = false
    private val swipeSize = 200f

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setOnTouchListener(recyclerView, viewHolder, dX)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListener(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float) {
        recyclerView.setOnTouchListener { _, event ->
            swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (dX < -swipeSize) {
                    (viewHolder as ShablonsAdapter.ViewHolder).showButtons(true)
                } else if (dX > swipeSize) {
                    (viewHolder as ShablonsAdapter.ViewHolder).showButtons(false)
                }
            }
            false
        }
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false
}