package com.example.remoteandroid.screens.remote.delegates

import android.annotation.SuppressLint
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import com.example.remoteandroid.R
import com.example.remoteandroid.databinding.TouchPadLayoutBinding
import com.example.remoteandroid.screens.remote.models.MouseEvent
import com.example.remoteandroid.screens.remote.uidata.TouchPadUiData
import com.example.remoteandroid.ui.recycler.AdapterDelegate
import com.example.remoteandroid.ui.recycler.BaseViewHolder
import com.example.remoteandroid.ui.recycler.UiData
import java.util.TimerTask
import kotlin.math.abs
import kotlin.math.pow

class TouchPadAdapterDelegate(
    private val onMouseEvent: (MouseEvent) -> Unit,
) : AdapterDelegate {

    private var lastX = Float.NaN
    private var lastY = Float.NaN
    private var dx = 0f
    private var dy = 0f

    private var isDown = false
    private var isScroll = false
    private val wasScroll: Boolean = isScroll
    private var isMoving = false
    private val wasMoving = isMoving
    private var eventStart: Long = 0
    private var startX = 0f
    private var startY = 0f

    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder = ViewHolder(parent)

    override fun isValidForType(data: UiData): Boolean = data is TouchPadUiData

    inner class ViewHolder(parent: ViewGroup) : BaseViewHolder(
        parent,
        R.layout.touch_pad_layout
    ) {

        private lateinit var binding: TouchPadLayoutBinding


        @SuppressLint("ClickableViewAccessibility")
        override fun bind(data: UiData) {
            data as TouchPadUiData



            binding = TouchPadLayoutBinding.bind(itemView)
            binding.touchpad.setOnTouchListener { _, motionEvent ->

                isScroll = isScroll || motionEvent.pointerCount > 1

                when (motionEvent.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        isDown = true
                        eventStart = motionEvent.eventTime
                        startX = motionEvent.x
                        startY = motionEvent.y
                    }

                    MotionEvent.ACTION_UP -> {
                        isDown = false
                        isMoving = false
                        isScroll = false
                        lastX = Float.NaN
                        lastY = Float.NaN
                    }
                }

                if (!lastX.isNaN() || !lastY.isNaN()) {
                    dx = Math.round(motionEvent.x - lastX).toFloat()
                    dy = Math.round(motionEvent.y - lastY).toFloat()
                }

                val resultX =
                    if (motionEvent.x > binding.pointer.width) binding.pointer.width.toFloat() else motionEvent.x
                val resultY =
                    if (motionEvent.y > binding.pointer.height) binding.pointer.height.toFloat() else motionEvent.y

                binding.pointer.x = motionEvent.x
                binding.pointer.y = motionEvent.y

                lastX = motionEvent.x
                lastY = motionEvent.y
                println("motionEvent ${motionEvent.x} ,${motionEvent.y}")

                val xDistFromStart: Float = abs((motionEvent.x - startX))
                val yDistFromStart: Float = abs((motionEvent.y - startY))

                if (isDown && !isMoving) {
                    if (xDistFromStart > 10 && yDistFromStart > 10) {
                        isMoving = true
                    }
                }

                if (isDown && isMoving) {
                    if (dx != 0f && dy != 0f) {
                        // Scale dx and dy to simulate acceleration
                        val dxSign = if (dx >= 0) 1 else -1
                        val dySign = if (dy >= 0) 1 else -1

                        dx = (dxSign * Math.round(abs(dx.toDouble()).pow(1.1))).toFloat()
                        dy = (dySign * Math.round(abs(dy.toDouble()).pow(1.1))).toFloat()

                        if (!isScroll) {
                            println("sending move $dx ,$dx")
                            onMouseEvent.invoke(MouseEvent.Move(dx.toDouble(), dy.toDouble()))
                        } else {
                            val scrollDx = motionEvent.x - startX
                            val scrollDy = motionEvent.y - startY

                            onMouseEvent.invoke(
                                MouseEvent.Scroll(
                                    scrollDx.toDouble(),
                                    scrollDy.toDouble()
                                )
                            )
                            println("sending scroll $dx ,$dx")
                        }
                    }
                } else if (!isDown && !wasMoving) {
                    println("sending click $dx ,$dx")
                    onMouseEvent.invoke(MouseEvent.Click)
                } else if (!isDown && wasMoving && wasScroll) {
                    // release two fingers
                    dx = motionEvent.x - startX
                    dy = motionEvent.y - startY

                    onMouseEvent.invoke(MouseEvent.Scroll(dx.toDouble(), dy.toDouble()))
                    println("sending scroll $dx ,$dx")
                }

                if (!isDown) {
                    isMoving = false

                    //if (autoScrollTimerTask != null) {
                    //    autoScrollTimerTask.cancel()
                    //    autoScrollTimerTask = null

                    //   Log.d("main", "ending autoscroll")
                    //}
                }
                return@setOnTouchListener true
            }
        }

    }
}