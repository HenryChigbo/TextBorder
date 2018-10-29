package com.inducesmile.textborder.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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

    private Paint rectPaint;
    private int padding = 12;

    private int viewWidth;
    private int viewHeighth;

    private float initialXPosition;
    private float initialYPosition;

    private float finalXPosition;
    private float finalYPosition;


    private enum Directions{
        LEFT,
        RIGHT,
        QOWN,
        UP
    }

    private Directions movementDirection;


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
        rectPaint.setStrokeWidth(5);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setTypeface(Typeface.DEFAULT);// your preference here
        rectPaint.setTextSize(20);
        rectPaint.setColor(getResources().getColor(R.color.colorAccent));
    }


    private RectF getRectBoundingSize() {
        rectPaint.getTextBounds(getText().toString(), 0, getText().toString().length(), textBoundingRect);

        int rectLeft = (int)convertDpToPixel(textBoundingRect.left, context);
        int rectRight = (int)convertDpToPixel(textBoundingRect.right, context);
        int rectTop = (int)convertDpToPixel(textBoundingRect.top, context);
        int rectBottom = (int)convertDpToPixel(textBoundingRect.bottom, context);

        int height = (int)convertDpToPixel(textBoundingRect.height(), context);
        int width = (int)convertDpToPixel(textBoundingRect.width(), context);

        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        Log.d(TAG, "Dimens " + rectLeft + " " + rectRight + " " + rectTop + " " + (rectBottom - rectTop) + " width height " + width + " " + height);
        return new RectF(rectLeft + pLeft - 10, rectTop + pTop + 20, rectRight + pRight + 10, height + pBottom + 20);
    }


    private boolean viewHasText() {
        if (getText().toString().equals("")) {
            return false;
        }
        return true;
    }


    private void drawRectangleTextBorder(Canvas canvas, Paint paint) {
        RectF rectToDraw = getRectBoundingSize();
        canvas.drawRect(rectToDraw, paint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeighth = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawColor(Color.BLUE);

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
                finalXPosition = event.getX();
                finalYPosition = event.getY();
                Log.d(TAG, "Action up has been called");
                return true;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "Action move has been called");
                return true;
        }

        return false;
    }


    private void actionToPerformOnDown(MotionEvent event){
        initialXPosition = event.getX();
        initialYPosition = event.getY();
        Log.d(TAG, "Action down has been called");
    }


    private void moveTextHorizontally(int left){
        setLeft(left);
        invalidate();
    }


    private void actionToPerformOnUp(MotionEvent event){
        finalXPosition = event.getX();
        finalYPosition = event.getY();
        Log.d(TAG, "Action up has been called");

        if(finalXPosition > initialXPosition){
            // box is move to the right
            movementDirection = Directions.RIGHT;
        }
        if(finalXPosition < initialXPosition){
            // box is move to the left
            movementDirection = Directions.LEFT;
        }
        if(finalYPosition < initialYPosition){
            // box is move to the down
            movementDirection = Directions.QOWN;
        }
        if(finalYPosition > initialYPosition){
            // box is move to the up
            movementDirection = Directions.UP;
        }
    }


    private void moveBoxHorizontally(float newXValue, int direction){
        if(movementDirection == Directions.RIGHT){

        }
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }


    public String getUserText() {
        return userText;
    }


    public void setUserText(String userText) {
        this.userText = userText;
        invalidate();
    }


    public float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

}
