package com.mrk.circle_image_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

public class RotateAbleDiscShapeImageView extends RotateAbleCircleImageView {
    public RotateAbleDiscShapeImageView(Context context) {
        super(context);
    }

    public RotateAbleDiscShapeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateAbleDiscShapeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawCenterPoint(canvas, getBorderRect().centerX(), getBorderRect().centerY(), getBorderRect().width());
    }

    Bitmap centerRingBm;

    private void drawCenterPoint(Canvas canvas, float circleCenterX, float circleCenterY, float containerSize) {
        if (centerRingBm == null) {
            centerRingBm = getBitmap(R.drawable.disc_center_ring);
        }

        int ringBmSize = centerRingBm.getWidth();
        int transparentSize = (int) ((ringBmSize * 12)/20 ) ;

        Paint mPaint = new Paint();
        setLayerType(LAYER_TYPE_HARDWARE, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvas.drawCircle(getBorderRect().centerX(), getBorderRect().centerY(), transparentSize/2, mPaint);
        //IMPORTANCE
        mPaint.setXfermode(null);
        //End firstLayer
        canvas.drawBitmap(centerRingBm, circleCenterX - ringBmSize / 2, circleCenterY - ringBmSize / 2, mPaint);
    }

    private Bitmap getBitmap(int drawableResID) {
        return BitmapFactory.decodeResource(getContext().getResources(), drawableResID);
    }
}
