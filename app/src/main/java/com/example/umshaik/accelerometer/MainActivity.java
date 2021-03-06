package com.example.umshaik.accelerometer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    //global variables

    SurfaceView cameraView, transparentView;

    SurfaceHolder holder, holderTransparent;

    Camera camera;

    private float RectLeft, RectTop, RectRight, RectBottom;

    int deviceHeight, deviceWidth;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);


        holder = cameraView.getHolder();

        holder.addCallback((SurfaceHolder.Callback) this);

        //holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraView.setSecure(true);


        // Create second surface with another holder (holderTransparent)

        transparentView = (SurfaceView) findViewById(R.id.transparent_view);


        holderTransparent = transparentView.getHolder();


        holderTransparent.addCallback((SurfaceHolder.Callback) this);

        holderTransparent.setFormat(PixelFormat.TRANSLUCENT);

        transparentView.setZOrderMediaOverlay(true);

        //getting the device heigth and width

        deviceWidth = getScreenWidth();

        deviceHeight = getScreenHeight();


    }

    public static int getScreenWidth() {

        return Resources.getSystem().getDisplayMetrics().widthPixels;

    }


    public static int getScreenHeight() {

        return Resources.getSystem().getDisplayMetrics().heightPixels;

    }


    private void Draw()

    {

        Canvas canvas = holderTransparent.lockCanvas(null);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(Color.GREEN);

        paint.setStrokeWidth(3);

        RectLeft = deviceHeight / 2;

        RectTop = deviceHeight / 2;

        RectRight = deviceWidth / 2;

        RectBottom = deviceWidth / 2;

        //Rect rec = new Rect((int) RectLeft, (int) RectTop, (int) RectRight, (int) RectBottom);
        //Rect rec = new Rect(10,10,200,200);

        /*Point centerOfCanvas = new Point(deviceWidth / 2, deviceHeight / 2);
        int rectW = 200;
        int rectH = 200;
        int left = centerOfCanvas.x - (rectW / 2);
        int top = centerOfCanvas.y - (rectH / 2);
        int right = centerOfCanvas.x + (rectW / 2);
        int bottom = centerOfCanvas.y + (rectH / 2);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, paint);*/
        Point centerOfCanvas = new Point(deviceWidth / 2, deviceHeight / 2);
        Bitmap bmp  = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);//ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
        canvas.drawBitmap(bmp, centerOfCanvas.x, centerOfCanvas.y - 50, null);
        bmp.recycle();
        bmp = null;

        holderTransparent.unlockCanvasAndPost(canvas);


    }


    @Override

    public void surfaceCreated(SurfaceHolder holder) {

        try {

            synchronized (holder)

            {
                Draw();
            }   //call a draw method

            camera = Camera.open(); //open a camera

        } catch (Exception e) {

            Log.i("Exception", e.toString());

            return;

        }

        Camera.Parameters param;

        /*param = camera.getParameters();


        param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);*/

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        if (display.getRotation() == Surface.ROTATION_0)

        {

            camera.setDisplayOrientation(90);

        }


        //camera.setParameters(param);


        try {

            camera.setPreviewDisplay(holder);

            camera.startPreview();

        } catch (Exception e) {


            return;

        }

    }

    @Override

    protected void onDestroy() {

        super.onDestroy();

    }

    @Override

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


        refreshCamera(); //call method for refress camera

    }

    public void refreshCamera() {

        if (holder.getSurface() == null) {

            return;

        }


        try {

            camera.stopPreview();

        } catch (Exception e) {


        }


        try {


            camera.setPreviewDisplay(holder);

            camera.startPreview();

        } catch (Exception e) {

        }

    }

    @Override

    public void surfaceDestroyed(SurfaceHolder holder) {

        camera.release(); //for release a camera

    }


}