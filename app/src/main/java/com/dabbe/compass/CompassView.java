package com.dabbe.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Daniel on 2015-03-25.
 */
public class CompassView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String[] cardinal = {"North", "Northeast", "East", "Southeast", "South", "Southwest", "West", "Northwest"};

    private Path topPath, botPath;
    private SurfaceHolder holder;
    private DrawingThread thread;
    private Paint paint, topPathPaint, botPathPaint, middlePaint;

    private Compass compass;
    private float width, height, arrowHeight, arrowWidth;
    ;

    public CompassView(Context context, Compass compass) {
        super(context);
        this.compass = compass;
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
    }

    private void initGraphical() {
        paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.RED);

        topPathPaint = new Paint();
        topPathPaint.setColor(Color.RED);
        topPathPaint.setStyle(Paint.Style.FILL);

        botPathPaint = new Paint();
        botPathPaint.setColor(Color.WHITE);
        botPathPaint.setStyle(Paint.Style.FILL);

        middlePaint = new Paint();
        middlePaint.setColor(Color.BLACK);
        middlePaint.setStyle(Paint.Style.FILL);

        width = getWidth();
        height = getHeight();
        arrowWidth = width / 15f;
        arrowHeight = height / 3f;

        topPath = new Path();
        topPath.moveTo(width / 2f, height / 2f - arrowHeight);
        topPath.lineTo(width / 2f + arrowWidth, height / 2f);
        topPath.lineTo(width / 2f - arrowWidth, height / 2f);
        topPath.lineTo(width / 2f, height / 2f - arrowHeight);
        topPath.close();

        botPath = new Path();
        botPath.moveTo(width / 2f, height / 2f + arrowHeight);
        botPath.lineTo(width / 2f + arrowWidth, height / 2f);
        botPath.lineTo(width / 2f - arrowWidth, height / 2f);
        botPath.lineTo(width / 2f, height / 2f + arrowHeight);
        botPath.close();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initGraphical();

        thread = new DrawingThread(this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
                compass.stop();
            } catch (InterruptedException e) {
            }
        }
    }

    public void drawCompass(Canvas canvas) {
        float deg = compass.getDegrees();
        canvas.drawColor(Color.BLACK);
        canvas.drawPath(topPath, topPathPaint);
        canvas.drawPath(botPath, botPathPaint);
        canvas.drawCircle(width / 2f, height / 2f, width / 20f, middlePaint);
        canvas.drawText(cardinal[Math.round((deg / 360.0f) * (cardinal.length - 1))], 500, 500, paint);
    }
}
