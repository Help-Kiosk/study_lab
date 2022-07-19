package com.example.study_lab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TouchView extends View {
    private float x,y;

    private Path path = new Path();
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint(Paint.DITHER_FLAG);

    public TouchView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public Bitmap exportToBitmap(int width, int height){
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);


    }







}
