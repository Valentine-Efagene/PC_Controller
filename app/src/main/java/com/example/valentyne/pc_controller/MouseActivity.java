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

import java.io.IOException;
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

    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    private DisplayMetrics displayMetrics;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int y;

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

        sketch = new MouseSketch(this);
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);

        ip = (String)getIntent().getStringExtra("IP");
        port = (int)getIntent().getIntExtra("port", 12324);
        socketTask = new MouseActivity.SocketTask(ip, port, this);
        socketTask.execute((Void) null);
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
        PrintWriter out;
        String value;
        private final String TAG = "SocketHandler";
        MouseActivity activity;

        public SocketHandlerThread(String ip, int port, MouseActivity activity) {
            this.out = out;
            this.value = value;
            this.activity = activity;
        }


        @Override
        public void run() {
            try {
                InetAddress serverAddress = InetAddress.getByName(ip);
                Socket socket = new Socket(serverAddress, port);
                out = new PrintWriter(socket.getOutputStream(), true);
                Log.w(TAG, "In try.");

                while(activity.getStillRunning()) {
                    Log.w(TAG, "In loop");
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
            }
        }
    }
}
