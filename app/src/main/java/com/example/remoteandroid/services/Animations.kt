package com.example.remoteandroid.services

import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.TranslateAnimation



object Animations {

    fun fromAtoB(fromX: Float, fromY: Float, toX: Float, toY: Float, speed: Int, listener: AnimationListener) : Animation {
        val fromAtoB: Animation = TranslateAnimation(
            Animation.ABSOLUTE,  //from xType
            fromX,
            Animation.ABSOLUTE,  //to xType
            toX,
            Animation.ABSOLUTE,  //from yType
            fromY,
            Animation.ABSOLUTE,  //to yType
            toY
        )

        fromAtoB.duration = speed.toLong()
        fromAtoB.interpolator = AnticipateOvershootInterpolator(1.0f)


        fromAtoB.setAnimationListener(listener)
        return fromAtoB
    }
}