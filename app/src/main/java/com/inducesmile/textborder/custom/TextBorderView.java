package com.inducesmile.textborder.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import com.inducesmile.textborder.R;

public class TextBorderView extends android.support.v7.widget.AppCompatTextView {

    private static final String TAG = TextBorderView.class.getSimpleName();

    private Context context;

    private Rect textBoundingRect;

    private String userText;

    private int thickness = 5;

    private Paint rectPaint;

    private float initialXPosition;
    private float initialYPosition;

    private float finalXPosition;
    private float finalYPosition;

    private int rectLeft;
    private int rectRight;
    private int rectTop;
    private int rectBottom;



    public TextBorderView(Context context) {
        super(context);
        initCustomView(context);
    }


    public TextBorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCustomView(context);
    }


    public TextBorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomView(context);
    }


    private void initCustomView(Context context) {
        this.context = context;

        textBoundingRect = new Rect();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStrokeWidth(thickness);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setTypeface(Typeface.DEFAULT);
        rectPaint.setTextSize(20);
        rectPaint.setColor(getResources().getColor(R.color.colorAccent));

        getRectBoundingSize();
    }


    private void getRectBoundingSize() {
        rectPaint.getTextBounds(getText().toString(), 0, getText().toString().length(), textBoundingRect);

        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        rectLeft = (int)convertDpToPixel(textBoundingRect.left, context) + pLeft;
        rectRight = (int)convertDpToPixel(textBoundingRect.right, context) + pRight;

        rectTop = getTop() + pTop + thickness;
        rectBottom = (int)(convertDpToPixel(textBoundingRect.bottom, context) + pBottom + getTextSize()) + thickness;

    }


    private boolean viewHasText() {
        if (getText().toString().equals("")) {
            return false;
        }
        return true;
    }


    private void drawRectangleTextBorder(Canvas canvas, Paint paint) {
        Rect rectToDraw = new Rect(rectLeft, rectTop, rectRight, rectBottom);
        canvas.drawRect(rectToDraw, paint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), rectPaint);
        if (viewHasText()) {
            drawRectangleTextBorder(canvas, rectPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchAction  = event.getAction();

        switch (touchAction){
            case MotionEvent.ACTION_DOWN:
                actionToPerformOnDown(event);
                return true;

            case MotionEvent.ACTION_UP:
                initialXPosition = finalXPosition;
                initialYPosition = finalYPosition;
                return true;

            case MotionEvent.ACTION_MOVE:
                finalXPosition = event.getX();
                int changeInXPosition  =  (int)(finalXPosition - initialXPosition);
                finalYPosition = event.getY();
                int changeInYPosition = (int)(finalYPosition - initialYPosition);
                moveTextHorizontally(changeInXPosition, changeInYPosition);
                return true;
        }

        return false;
    }


    private void actionToPerformOnDown(MotionEvent event){
        initialXPosition = event.getX();
        initialYPosition = event.getY();
        Log.d(TAG, "Action down has been called");
    }


    private void moveTextHorizontally(int left, int top){
        rectLeft = rectLeft + left;
        rectRight = rectLeft + 300;
        rectTop = rectTop + top;
        rectBottom = rectTop + 40;
        this.setX(rectLeft);
        this.setY(rectTop);
    }


    public void setUserText(String userText) {
        this.userText = userText;
        invalidate();
    }


    public float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
