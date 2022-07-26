package com.example.study_lab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TouchView extends View {
    private float penX;
    private float penY;
    private Path path = new Path();
    private Paint drawingPaint = new Paint(Paint.DITHER_FLAG);
    private Bitmap drawingBitmap;
    private Canvas drawingCanvas;
    private Paint pen = new Paint();

    public TouchView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);

        drawingPaint.setAntiAlias(true);
        drawingPaint.setStrokeWidth(10f);
        drawingPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    public Bitmap initBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable drawable = getBackground();

        if (drawable!=null){
            drawable.draw(canvas);
        }
        else{
            canvas.drawColor(Color.WHITE);
        }
        draw(canvas);

        return bitmap;
    }

    public Bitmap exportToBitmap(int width, int height){
        Bitmap rawBitmap = initBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(rawBitmap, width, height, false);
        rawBitmap.recycle();
        return scaledBitmap;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawBitmap(drawingBitmap, 0f, 0f, drawingPaint);
        canvas.drawPath(path, pen);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        drawingBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawingCanvas = new Canvas(drawingBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) return false;

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                onTouchDown(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp();
                invalidate();
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    private void onTouchDown(float x, float y){
        path.reset();
        path.moveTo(x, y);
        penX = x;
        penY = y;
    }

    private void onTouchMove(float x, float y){
        float dx = Math.abs(x-penX);
        float dy = Math.abs(y-penY);

        if (dx >= 4f || dy >= 4f){
            path.quadTo(penX, penY, (x+penX)/2, (y+penY)/2);
            penX = x;
            penY = y;
        }
    }

    private void onTouchUp(){
        path.lineTo(penX, penY);
        drawingCanvas.drawPath(path, pen);
        path.reset();
    }

}
