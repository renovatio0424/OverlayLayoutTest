package com.example.renov.overlaylayouttest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by renov on 2018-03-16.
 */

public class MyService extends Service {
    WindowManager wm;
    View mView;

    float mTouchX, mTouchY;
    private int mViewX, mViewY;

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                /*ViewGroup.LayoutParams.MATCH_PARENT*/300,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.LEFT | Gravity.TOP;
        mView = inflate.inflate(R.layout.view_in_service, null);
        final TextView textView = (TextView) mView.findViewById(R.id.textView);
        final ImageButton bt =  (ImageButton) mView.findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.setImageResource(R.mipmap.ic_launcher_round);
                textView.setText("on click!!");
            }
        });
        wm.addView(mView, params);

        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        mTouchX = event.getRawX();
                        mTouchY = event.getRawY();
                        mViewX = params.x;
                        mViewY = params.y;

                        break;

                    case MotionEvent.ACTION_UP:
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int x = (int) (event.getRawX() - mTouchX);
                        int y = (int) (event.getRawY() - mTouchY);

                        params.x = mViewX + x;
                        params.y = mViewY + y;

                        wm.updateViewLayout(mView, params);

                        break;
                }

                return true;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(wm != null) {
            if(mView != null) {
                wm.removeView(mView);
                mView = null;
            }
            wm = null;
        }
    }
}
