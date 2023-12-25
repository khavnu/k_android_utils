package com.khapv.search_view.expandable_layout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.FloatRange;

import com.khapv.expandable_layout.R;
import com.khapv.search_view.util.FastOutSlowInInterpolator;

public class HorizontalExpandableLayout extends FrameLayout {
    public interface State {
        int COLLAPSED = 0;
        int COLLAPSING = 1;
        int EXPANDING = 2;
        int EXPANDED = 3;
    }

    public static final String KEY_SUPER_STATE = "super_state";
    public static final String KEY_EXPANSION = "expansion";

    private static final int DEFAULT_DURATION = 300;

    private int duration = DEFAULT_DURATION;
    private float parallax;
    private float expansion;
    private int state;

    private int layoutWidth = -1;
    private ViewGroup.LayoutParams searchViewParams;

    private Interpolator interpolator = new FastOutSlowInInterpolator();
    private ValueAnimator animator;

    private OnExpansionUpdateListener listener;

    public HorizontalExpandableLayout(Context context) {
        this(context, null);
        init();
    }

    public HorizontalExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableLayout);
            duration = a.getInt(R.styleable.ExpandableLayout_el_duration, DEFAULT_DURATION);
            expansion = a.getBoolean(R.styleable.ExpandableLayout_el_expanded, false) ? 1 : 0;
            parallax = a.getFloat(R.styleable.ExpandableLayout_el_parallax, 1);
            a.recycle();

            state = expansion == 0 ? State.COLLAPSED : State.EXPANDED;
            setParallax(parallax);
        }
    }

    private void init() {
      //  layoutWidth = ExpandableLayoutScreenUtils.getScreenWidth(getContext());
        // searchViewParams = new LinearLayout.LayoutParams(screenWidth, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();

        expansion = isExpanded() ? 1 : 0;

        bundle.putFloat(KEY_EXPANSION, expansion);
        bundle.putParcelable(KEY_SUPER_STATE, superState);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        expansion = bundle.getFloat(KEY_EXPANSION);
        state = expansion == 1 ? State.EXPANDED : State.COLLAPSED;
        Parcelable superState = bundle.getParcelable(KEY_SUPER_STATE);

        super.onRestoreInstanceState(superState);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if(layoutWidth <= 0) {
            layoutWidth = width;
        }

        int size = width;

        if (expansion < elevation) {
            expansion = elevation;
            return;
        }

        int expansionDelta = size - Math.round(size * expansion);

        if (parallax > 0) {
            float parallaxDelta = expansionDelta * parallax;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int direction = -1;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1
                        && getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                    direction = 1;
                }
                //   child.setTranslationX(direction * parallaxDelta);
                //    LayoutParams layoutParams = child.getLayoutParams();
                if (child instanceof ViewGroup) {
                    View edtSearch = ((ViewGroup) child).getChildAt(0);
                    edtSearch.post(new Runnable() {
                        @Override
                        public void run() {
                            int searchWidth = (int) (layoutWidth * expansion);
                            if (searchViewParams == null) {
                                searchViewParams = edtSearch.getLayoutParams();
                            }
                            searchViewParams.width = searchWidth;
                            edtSearch.setLayoutParams(searchViewParams);
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (animator != null) {
            animator.cancel();
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Get expansion state
     *
     * @return one of {@link State}
     */
    public int getState() {
        return state;
    }

    public boolean isExpanded() {
        return state == State.EXPANDING || state == State.EXPANDED;
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean animate) {
        if (isExpanded()) {
            collapse(animate);
        } else {
            expand(animate);
        }
    }

    public void expand() {
        expand(true);
    }

    public void expand(boolean animate) {
        setExpanded(true, animate);
    }

    public void collapse() {
        collapse(true);
    }

    public void collapse(boolean animate) {
        setExpanded(false, animate);
    }

    /**
     * Convenience method - same as calling setExpanded(expanded, true)
     */
    public void setExpanded(boolean expand) {
        setExpanded(expand, true);
    }

    public void setExpanded(boolean expand, boolean animate) {
        if (expand == isExpanded()) {
            return;
        }

        int targetExpansion = expand ? 1 : 0;
        if (animate) {
            animateSize(targetExpansion);
        } else {
            setExpansion(targetExpansion);
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getExpansion() {
        return expansion;
    }

    public void setExpansion(float expansion) {
        if (this.expansion == expansion) {
            return;
        }

        // Infer state from previous value
        float delta = expansion - this.expansion;
        if (expansion == 0) {
            state = State.COLLAPSED;
        } else if (expansion == 1) {
            state = State.EXPANDED;
        } else if (delta < 0) {
            state = State.COLLAPSING;
        } else if (delta > 0) {
            state = State.EXPANDING;
        }

        this.expansion = expansion;
        requestLayout();

        if (listener != null) {
            listener.onExpansionUpdate(expansion, state);
        }
    }

    public float getParallax() {
        return parallax;
    }

    public void setParallax(float parallax) {
        // Make sure parallax is between 0 and 1
        parallax = Math.min(1, Math.max(0, parallax));
        this.parallax = parallax;
    }

    public void setOnExpansionUpdateListener(OnExpansionUpdateListener listener) {
        this.listener = listener;
    }

    private void animateSize(int targetExpansion) {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }

        animator = ValueAnimator.ofFloat(expansion, targetExpansion);
        animator.setInterpolator(interpolator);
        animator.setDuration(duration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setExpansion((float) valueAnimator.getAnimatedValue());
            }
        });

        animator.addListener(new ExpansionListener(targetExpansion));

        animator.start();
    }

    public interface OnExpansionUpdateListener {
        /**
         * Callback for expansion updates
         *
         * @param expansionFraction Value between 0 (collapsed) and 1 (expanded) representing the the expansion progress
         * @param state             One of {@link State} repesenting the current expansion state
         */
        void onExpansionUpdate(float expansionFraction, int state);
    }

    private class ExpansionListener implements Animator.AnimatorListener {
        private int targetExpansion;
        private boolean canceled;

        public ExpansionListener(int targetExpansion) {
            this.targetExpansion = targetExpansion;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            state = targetExpansion == 0 ? State.COLLAPSING : State.EXPANDING;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!canceled) {
                state = targetExpansion == 0 ? State.COLLAPSED : State.EXPANDED;
                setExpansion(targetExpansion);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            canceled = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }

    @FloatRange(from = 0.3f, to = 1.0f)
    private float elevation = 0.3f;

    public void setElevation(@FloatRange(from = 0.3f, to = 1.0f) float _elevation) {
        elevation = _elevation;
    }

    public void notifyLayoutButtonContainerChanged(LinearLayout layoutButtonContainer) {
        layoutButtonContainer.post(new Runnable() {
            @Override
            public void run() {
                int buttonContainerWidth = layoutButtonContainer.getWidth();
                float newElevation = 1 - ((float) buttonContainerWidth/layoutWidth);
                newElevation = rounded(newElevation, 2);
                setElevation(newElevation);
            }
        });
    }

    /**
     * Best solution
     * <a href = "https://stackoverflow.com/a/35833800/22773001">Link</a>
     * */
    private static float rounded(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++) {
            pow *= 10;
        }
        float tmp = number * pow;
        float tmpSub = tmp - (int) tmp;

        return ((float) ((int) (number >= 0
                ? (tmpSub >= 0.5f ? tmp + 1 : tmp)
                : (tmpSub >= -0.5f ? tmp : tmp - 1)
        ))) / pow;

    }
}
