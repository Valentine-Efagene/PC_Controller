package com.example.valentyne.pc_controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import processing.android.CompatUtils;
import processing.android.PFragment;
import processing.core.PApplet;

public class MouseActivity extends AppCompatActivity {

    private PApplet sketch;
    private final String TAG = "SwitchActivity";
    private String ip;
    private int port;
    private SocketTask socketTask;
    private boolean stillRunning = true;
    private int x;
    private int y;
    private String screenSize = null;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    private String action;


    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    private boolean pressed;

    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    private DisplayMetrics displayMetrics;

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String s) {
        screenSize = s;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        ip = getIntent().getStringExtra("IP");
        port = getIntent().getIntExtra("port", 12324);
        socketTask = new MouseActivity.SocketTask(ip, port, this);
        socketTask.execute((Void) null);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sketch = new MouseSketch(this);
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                onPause();
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        if (sketch != null) {
            sketch.onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
        }
    }

    public boolean getStillRunning() {
        return stillRunning;
    }

    public void onPause() {
        super.onPause();
        stillRunning = false; //This is what actually stops the transmission. Every other thing.
    }

    public class SocketTask extends AsyncTask<Void, Void, Boolean> {

        private final String ip;
        private final int port;
        MouseActivity activity;
        ExecutorService executorService;
        private final String TAG = "SocketTask";

        SocketTask(String ip, int port, MouseActivity activity) {
            this.ip = ip;
            this.port = port;
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            MouseActivity.SocketHandlerThread socketHandlerThread = new MouseActivity.SocketHandlerThread(ip, port, activity);
            executorService = Executors.newCachedThreadPool();
            executorService.execute(socketHandlerThread);
            return true;
        }

        @Override
        protected void onCancelled() {
        }
    }

    public class SocketHandlerThread implements Runnable{
        String data;
        PrintWriter out;
        BufferedReader in;
        String value;
        private final String TAG = "SocketHandler";
        MouseActivity activity;
        char[] buffer;

        public SocketHandlerThread(String ip, int port, MouseActivity activity) {
            this.activity = activity;
            buffer = new char[512];
        }


        @Override
        public void run() {

            try {
                InetAddress serverAddress = InetAddress.getByName(ip);
                Socket socket = new Socket(serverAddress, port);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.w(TAG, "In try.");

                while(activity.getStillRunning()) {
                    int charsRead = 0;
                    data = "";

                    if(activity.getScreenSize() == null) {
                        if( ( in.ready() && (charsRead = in.read(buffer) ) != -1 )) {
                            data += new String(buffer).substring(0, charsRead);
                            activity.setScreenSize(data.toString());
                            Log.w(TAG, "Data after: " + data);
                        }
                    }

                    if(activity.getAction() != null) {
                        Log.w(TAG, "Action = " + activity.getAction());
                        out.println(activity.getAction());
                        Thread.sleep(100);
                        activity.setAction(null);
                    }

                    Log.w(TAG, "Mouse");
                    out.println(x + " " + y);
                    Thread.sleep(100);
                    Log.w(TAG, "Still running.");
                }
            } catch (UnknownHostException e) {
                Log.w(TAG, "Unknown host");
            } catch (IOException e) {
                Log.w(TAG, "IOException");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                out.close();
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
