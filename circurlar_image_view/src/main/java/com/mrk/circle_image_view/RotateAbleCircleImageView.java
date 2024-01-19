package com.mrk.circle_image_view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

public class RotateAbleCircleImageView extends CircleImageView{
    public RotateAbleCircleImageView(Context context) {
        super(context);
        applyAnim();
    }

    public RotateAbleCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAnim();
    }

    public RotateAbleCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyAnim();
    }


    ObjectAnimator anim;
    long durationInMillis = 20000;
    private void applyAnim() {
        anim = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 360f)
                .setDuration(durationInMillis);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
    }

    public void startAnim() {
        if(anim == null) {
            applyAnim();
        }
        anim.start();
    }

    public void setAnimDuration(long newDuration) {
        durationInMillis = newDuration;
        anim.setDuration(durationInMillis);
    }

    public void pauseAnim() {
        anim.pause();
    }

    public void startOrResumeAnim() {
        if(anim == null || !anim.isStarted()) {
            startAnim();
        } else {
            anim.resume();
        }
    }

    public void reverseAnim() {
        anim.reverse();
    }

    public void cancelAnim() {
        if(anim != null) {
            anim.end();
            anim.cancel();
            anim = null;
        }
    }

    public void resetAnim() {
        cancelAnim();
        applyAnim();
    }

    public boolean isAnimPausedOrStop() {
        return anim == null || !anim.isStarted() || anim.isPaused();
    }
}
