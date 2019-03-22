package com.example.valentyne.pc_controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ModeSelectionActivity extends AppCompatActivity {
    ListView listView;

    String[] modes;
    String ip;
    int port;
    private final String TAG = "ModeSelectionActivity";
    private int error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ip = (String)getIntent().getStringExtra("IP");
        port = (int)getIntent().getIntExtra("port", 12324);
        error = (int)getIntent().getIntExtra("error", 0);
        setContentView(R.layout.activity_mode_selection);
        listView = (ListView) findViewById(R.id.listView);
        modes = getResources().getStringArray(R.array.modes_array);

        if(error == 1) {
            Toast.makeText(ModeSelectionActivity.this,
                    "This sensor is not supported on your device!", Toast.LENGTH_LONG).show();
        }

        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, modes));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.w(TAG, "You selected " + modes[i]);

                if(modes[i] == modes[0]) {
                    Intent intent = new Intent("com.example.valentyne.pc_controller.MouseActivity");
                    intent.putExtra("IP", ip);
                    intent.putExtra("port", port);
                    startActivity(intent);
                    Log.w(TAG, "You picked text input");
                }else if (modes[i] == modes[1]) {
                    Intent intent = new Intent("com.example.valentyne.pc_controller.KeyboardActivity");
                    intent.putExtra("IP", ip);
                    intent.putExtra("port", port);
                    startActivity(intent);
                    Log.w(TAG, "You picked switch");
                }
            }
        });
    }
}
