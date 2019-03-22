package com.example.valentyne.pc_controller;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The reception desk of the app.
 */
public class ConnectActivity extends AppCompatActivity{

    private ConnectTask connectTask = null;
    private final String TAG = "Connect Activity";
    final private int REQUEST_INTERNET = 123;

    // UI references.
    private EditText editText_IP;
    private EditText editText_port;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent("com.example.valentyne.pc_controller.AboutActivity");
                startActivity(intent);
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        editText_IP = (EditText) findViewById(R.id.editText_IP);

        editText_port = (EditText) findViewById(R.id.editText_port);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_INTERNET) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    private void attemptLogin() {
        boolean cancel = false;
        View focusView = null;

        if (connectTask != null) {
            return;
        }

        // Reset errors.
        editText_IP.setError(null);
        editText_port.setError(null);

        // Store values at the time of the login attempt.
        String IP = editText_IP.getText().toString();
        String string_port = editText_port.getText().toString();
        int port = 0;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(string_port)) {
            editText_port.setError(getString(R.string.error_field_required));
            focusView = editText_port;
            cancel = true;
        }else{
            try{
                port = Integer.parseInt(editText_port.getText().toString());
            }catch (NumberFormatException ex) {
                editText_port.setError(getString(R.string.error_invalid_password));
                focusView = editText_port;
                cancel = true;
            }
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(IP)) {
            editText_IP.setError(getString(R.string.error_field_required));
            focusView = editText_IP;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                        REQUEST_INTERNET);
            } else{
                Log.w(TAG, "We have permission.");
                connectTask = new ConnectTask(IP, port);
                connectTask.execute((Void) null);
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * This task tests the network, and allows the user to progress if the network is ok.
     *
     */
    public class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        private final String ip;
        private final int port;
        private ExecutorService executorService;
        private boolean proceed;

        public boolean isProceed() {
            return proceed;
        }

        public void setProceed(boolean proceed) {
            this.proceed = proceed;
        }

        ConnectTask(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // Test the network by creating and then closing a test socket
            ConnectActivity.SocketHandlerThread socketHandlerThread = new ConnectActivity.SocketHandlerThread(ip, port, this);
            executorService = Executors.newCachedThreadPool();
            executorService.execute(socketHandlerThread);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return proceed;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            connectTask = null;
            showProgress(false);

            if (success) {
                Intent i = new Intent("com.example.valentyne.pc_controller.ModeSelectionActivity");
                i.putExtra("error", 0);
                i.putExtra("IP", ip);
                i.putExtra("port", port);
                startActivity(i);
                finish();
            } else {
                editText_port.setError(getString(R.string.error_could_not_connect));
                editText_port.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            connectTask = null;
            showProgress(false);
        }
    }

    public class SocketHandlerThread implements Runnable{
        private final String TAG = "SocketHandler";
        private String ip;
        private int port;
        Socket socket;

        ConnectTask connectTask;

        public SocketHandlerThread(String ip, int port, ConnectTask connectTask) {
            this.ip = ip;
            this.port = port;
            this.connectTask = connectTask;
        }


        @Override
        public void run() {
            try {
                InetAddress serverAddress = InetAddress.getByName(ip);
                socket = new Socket(serverAddress, port);
                connectTask.setProceed(true);
            } catch (UnknownHostException e) {
                Toast.makeText(ConnectActivity.this, "Unknown host." + ip, Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Unknown host");
            } catch (IOException e) {
                Log.w(TAG, "IOException");
            }finally {
                if(socket != null) {
                    try {
                        socket.getOutputStream().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

