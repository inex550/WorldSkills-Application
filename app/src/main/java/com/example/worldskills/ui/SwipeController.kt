package com.example.worldskills.ui

import android.annotation.SuppressLint
import android.graphics.*
import android.provider.CalendarContract
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

enum class ButtonsState {
    GONE,
    VISIBLE
}

class SwipeController: Callback() {

    private var swipeBack: Boolean = false
    private var buttonsState: ButtonsState = ButtonsState.GONE
    private val buttonWight = 150

    private var buttonsInstances: List<Rect>? = null

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, LEFT)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ACTION_STATE_SWIPE)
            setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        drawButtons(c, viewHolder)
    }

    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val itemView = viewHolder.itemView
        val p = Paint()

        val btn1 = Rect(itemView.right - buttonWight, itemView.top, itemView.right, itemView.bottom)
        p.color = Color.RED
        c.drawRect(btn1, p)
        drawText("Удалить", c, btn1, p)

        val btn2 = Rect(itemView.right - buttonWight * 2, itemView.top, itemView.right, itemView.bottom)
        p.color = Color.GRAY
        c.drawRect(btn2, p)
        drawText("Изменить", c, btn2, p)

        buttonsInstances = null
        if (buttonsState == ButtonsState.VISIBLE)
            buttonsInstances = listOf(btn1)
    }

    private fun drawText(text: String, c: Canvas, btn: Rect, p: Paint) {
        val textSize = 40f
        p.color = Color.WHITE
        p.textSize = textSize

        val textWidth = p.measureText(text)
        c.drawText(text, btn.centerX() - textWidth / 2, btn.centerY() + textSize / 2, p)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener{ _, event ->
                swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
                if (swipeBack) {
                    if (dX < -buttonWight) buttonsState = ButtonsState.VISIBLE
                    if (buttonsState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        setItemsClickable(recyclerView, false)
                    }
                }
                false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
                false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    super@SwipeController.onChildDraw(c, recyclerView, viewHolder, 0f, dY, actionState, isCurrentlyActive)
                    recyclerView.setOnTouchListener { _, _ -> false }
                    setItemsClickable(recyclerView, true)
                    swipeBack = false
                    buttonsState = ButtonsState.GONE
                }
                false
        }
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
}