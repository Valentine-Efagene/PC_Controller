package com.example.valentyne.pc_controller;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyboardActivity extends AppCompatActivity {
    //UI references
    private Button btn_0;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_a;
    private Button btn_b;
    private Button btn_c;
    private Button btn_d;
    private Button btn_e;
    private Button btn_f;
    private Button btn_g;
    private Button btn_h;
    private Button btn_i;
    private Button btn_j;
    private Button btn_k;
    private Button btn_l;
    private Button btn_m;
    private Button btn_n;
    private Button btn_o;
    private Button btn_p;
    private Button btn_q;
    private Button btn_r;
    private Button btn_s;
    private Button btn_t;
    private Button btn_u;
    private Button btn_v;
    private Button btn_w;
    private Button btn_x;
    private Button btn_y;
    private Button btn_z;

    private Button btn_caps;

    boolean caps = false;

    private String message = null;

    private final String TAG = "SwitchActivity";
    private String ip;
    private int port;
    private SocketTask socketTask;
    private boolean stillRunning = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        ip = (String)getIntent().getStringExtra("IP");
        port = (int)getIntent().getIntExtra("port", 12324);
        socketTask = new SocketTask(ip, port, this);
        socketTask.execute((Void) null);

        btn_caps = (Button) findViewById(R.id.btn_caps);
        btn_caps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caps = !caps;

                if(caps) {
                    btn_caps.setBackgroundColor(Color.GRAY);
                }else{
                    btn_caps.setBackgroundColor(Color.WHITE);
                }
            }
        });

        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "~48";
            }
        });

        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "~49";
            }
        });

        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "~50";
            }
        });

        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "~51";
            }
        });

        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "~52";
            }
        });

        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "~53";
            }
        });

        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "~54";
            }
        });

        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "~55";
            }
        });

        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "~56";
            }
        });

        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "~57";
            }
        });

        btn_a = (Button) findViewById(R.id.btn_a);
        btn_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~65" : "~97";
            }
        });

        btn_b = (Button) findViewById(R.id.btn_b);
        btn_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~66" : "~98";
            }
        });

        btn_c = (Button) findViewById(R.id.btn_c);
        btn_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~67" : "~99";
            }
        });

        btn_d = (Button) findViewById(R.id.btn_d);
        btn_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~68" : "~100";
            }
        });

        btn_e = (Button) findViewById(R.id.btn_e);
        btn_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~69" : "~101";
            }
        });

        btn_f = (Button) findViewById(R.id.btn_f);
        btn_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~70" : "~102";
            }
        });

        btn_g = (Button) findViewById(R.id.btn_g);
        btn_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~71" : "~103";
            }
        });

        btn_h = (Button) findViewById(R.id.btn_h);
        btn_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~72" : "~104";
            }
        });

        btn_i = (Button) findViewById(R.id.btn_i);
        btn_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~73" : "~105";
            }
        });

        btn_j = (Button) findViewById(R.id.btn_j);
        btn_j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~74" : "~106";
            }
        });

        btn_k = (Button) findViewById(R.id.btn_k);
        btn_k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~75" : "~107";
            }
        });

        btn_l = (Button) findViewById(R.id.btn_l);
        btn_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~76" : "~108";
            }
        });

        btn_m = (Button) findViewById(R.id.btn_m);
        btn_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~77" : "~109";
            }
        });

        btn_n = (Button) findViewById(R.id.btn_n);
        btn_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~78" : "~110";
            }
        });

        btn_o = (Button) findViewById(R.id.btn_o);
        btn_o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~79" : "~111";
            }
        });

        btn_p = (Button) findViewById(R.id.btn_p);
        btn_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~80" : "~112";
            }
        });

        btn_q = (Button) findViewById(R.id.btn_q);
        btn_q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~81" : "~113";
            }
        });

        btn_r = (Button) findViewById(R.id.btn_r);
        btn_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~82" : "~114";
            }
        });

        btn_s = (Button) findViewById(R.id.btn_s);
        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~83" : "~115";
            }
        });

        btn_t = (Button) findViewById(R.id.btn_t);
        btn_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~84" : "~116";
            }
        });

        btn_u = (Button) findViewById(R.id.btn_u);
        btn_u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~85" : "~117";
            }
        });

        btn_v = (Button) findViewById(R.id.btn_v);
        btn_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~86" : "~118";
            }
        });

        btn_w = (Button) findViewById(R.id.btn_w);
        btn_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~87" : "~119";
            }
        });

        btn_x = (Button) findViewById(R.id.btn_x);
        btn_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~88" : "~120";
            }
        });

        btn_y = (Button) findViewById(R.id.btn_y);
        btn_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~89" : "~121";
            }
        });

        btn_z = (Button) findViewById(R.id.btn_z);
        btn_z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = caps ? "~90" : "~122";
            }
        });
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
        KeyboardActivity activity;
        ExecutorService executorService;
        private final String TAG = "SocketTask";

        SocketTask(String ip, int port, KeyboardActivity activity) {
            this.ip = ip;
            this.port = port;
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SocketHandlerThread socketHandlerThread = new SocketHandlerThread(ip, port, activity);
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
        Switch aSwitch;
        private final String TAG = "SocketHandler";
        KeyboardActivity activity;

        public SocketHandlerThread(String ip, int port, KeyboardActivity activity) {
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
                    if(message != null) {
                        out.println(message);
                        message = null;
                    }

                    Log.w(TAG, "Still running.");
                }
            } catch (UnknownHostException e) {
                Log.w(TAG, "Unknown host");
            } catch (IOException e) {
                Log.w(TAG, "IOException");
            }finally{
                out.close();
            }
        }
    }
}
