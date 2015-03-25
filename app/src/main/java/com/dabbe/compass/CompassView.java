package com.dabbe.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Daniel on 2015-03-25.
 */
public class CompassView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private DrawingThread thread;
    private Paint paint;
    private Compass compass;

    public CompassView(Context context, Compass compass) {
        super(context);
        init();
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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
            } catch (InterruptedException e) {}
        }
    }

    public void drawCompass(Canvas canvas){
        canvas.drawColor(Color.BLACK);
        canvas.drawCircle(500, 500, 100, paint);
    }
}
