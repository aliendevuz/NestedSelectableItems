package uz.alien.nested.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class DragSelectRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    interface OnUnitSelectListener {
        fun onSingleSelect(position: Int)
        fun onStartDragSelection(position: Int)
        fun onDragSelection(position: Int, selectionMode: Boolean)
        fun onEndSelection()
    }

    var listener: OnUnitSelectListener? = null
    private var isDragging = false
    private var isLongPressDetected = false
    private var lastSelectedPosition = -1
    private var startX = 0f
    private var startY = 0f
    private var lastY = 0f // Oldingi Y pozitsiyasi skroll uchun
    private var selectionMode: Boolean? = null
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private val longPressTimeout = 600L // Uzun bosish vaqti 600 ms
    private val autoScrollThreshold = 50 // Chegaraga yaqinlik (piksel)
    private val autoScrollSpeed = 10 // Skroll tezligi (piksel har 16ms da)
    private var isAutoScrolling = false
    private var isScrolling = false
    private var moveEventCount = 0
    private var totalDeltaY = 0f

    private val longPressRunnable = Runnable {
        if (!isDragging && !isAutoScrolling && !isScrolling && lastSelectedPosition != NO_POSITION) {
            // 600 ms ichida harakatni tahlil qilish
            if (totalDeltaY < touchSlop * 2) {
                // Kichik masofa: tanlash rejimi
                isDragging = true
                isLongPressDetected = true
                isScrolling = false
                listener?.onStartDragSelection(lastSelectedPosition)
            } else {
                // Katta masofa: scroll rejimi
                isScrolling = true
            }
        }
    }

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            if (isDragging && isAutoScrolling) {
                val scrolled = performAutoScroll()
                if (scrolled) {
                    findChildViewUnder(startX, startY)?.let { view ->
                        val position = getChildAdapterPosition(view)
                        if (position != NO_POSITION && position != lastSelectedPosition) {
                            lastSelectedPosition = position
                            selectionMode?.let { mode ->
                                listener?.onDragSelection(position, mode)
                            }
                        }
                    }
                    handler?.postDelayed(this, 200)
                } else {
                    isAutoScrolling = false
                }
            }
        }
    }

    init {
        overScrollMode = OVER_SCROLL_NEVER
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                lastY = event.y
                lastSelectedPosition = -1
                selectionMode = null
                isLongPressDetected = false
                isAutoScrolling = false
                isScrolling = false
                moveEventCount = 0
                totalDeltaY = 0f
                findChildViewUnder(event.x, event.y)?.let { view ->
                    val position = getChildAdapterPosition(view)
                    if (position != NO_POSITION) {
                        lastSelectedPosition = position
                        handler?.postDelayed(longPressRunnable, longPressTimeout)
                    }
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = abs(event.x - startX)
                val deltaY = abs(event.y - startY)
                val currentY = event.y
                moveEventCount++
                totalDeltaY += abs(currentY - lastY)

                if (!isDragging && !isLongPressDetected) {
                    if (deltaY > touchSlop * 3 && deltaY > deltaX) {
                        // Vertikal harakat katta bo'lsa va gorizontal harakatdan ustun bo'lsa
                        isScrolling = true
                        handler?.removeCallbacks(longPressRunnable)
                    }
                }

                if (isDragging) {
                    startX = event.x
                    startY = event.y
                    checkAutoScroll(event.y)
                    findChildViewUnder(event.x, event.y)?.let { view ->
                        val position = getChildAdapterPosition(view)
                        if (position != NO_POSITION && position != lastSelectedPosition) {
                            lastSelectedPosition = position
                            selectionMode?.let { mode ->
                                listener?.onDragSelection(position, mode)
                            }
                        }
                    }
                    return true
                } else if (isScrolling) {
                    // Maxsus skroll logikasi: faqat vertikal harakat
                    val scrollDistance = (lastY - currentY).toInt()
                    scrollBy(0, scrollDistance)
                    lastY = currentY
                    return true
                }
                lastY = currentY
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                handler?.removeCallbacks(longPressRunnable)
                handler?.removeCallbacks(autoScrollRunnable)
                if (!isDragging && !isLongPressDetected && !isScrolling && lastSelectedPosition != NO_POSITION) {
                    listener?.onSingleSelect(lastSelectedPosition)
                }
                resetState()
                listener?.onEndSelection()
                return true
            }
        }
        return true
    }

    private fun checkAutoScroll(y: Float) {
        val height = height
        val canScrollUp = canScrollVertically(-1)
        val canScrollDown = canScrollVertically(1)

        isAutoScrolling = when {
            y < autoScrollThreshold && canScrollUp -> {
                handler?.post(autoScrollRunnable)
                true
            }
            y > height - autoScrollThreshold && canScrollDown -> {
                handler?.post(autoScrollRunnable)
                true
            }
            else -> {
                handler?.removeCallbacks(autoScrollRunnable)
                false
            }
        }
    }

    private fun performAutoScroll(): Boolean {
        val height = height
        val canScrollUp = canScrollVertically(-1)
        val canScrollDown = canScrollVertically(1)

        return when {
            startY < autoScrollThreshold && canScrollUp -> {
                scrollBy(0, -autoScrollSpeed)
                true
            }
            startY > height - autoScrollThreshold && canScrollDown -> {
                scrollBy(0, autoScrollSpeed)
                true
            }
            else -> false
        }
    }

    fun setSelectionMode(isSelected: Boolean) {
        selectionMode = !isSelected
    }

    private fun resetState() {
        isDragging = false
        isLongPressDetected = false
        lastSelectedPosition = -1
        selectionMode = null
        isAutoScrolling = false
        isScrolling = false
        moveEventCount = 0
        totalDeltaY = 0f
        handler?.removeCallbacks(autoScrollRunnable)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return true // Barcha hodisalarni o'zimiz boshqaramiz
    }
}