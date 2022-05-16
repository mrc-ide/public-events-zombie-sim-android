package com.mrc.zombiesim;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetTask extends AsyncTask<String, Void, String> {
    //private Exception exception;

    private static Handler progressCheckHandler = new Handler();
    private static Runnable progressCheckRunner;
    private View view;
    private MainActivity parent;

    public NetTask(View v, MainActivity p) {
        view = v;
        parent = p;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            System.out.println("DO IN BACKGROUND - params[0] = "+params[0]);
            final String result;
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            result = readStream(con.getInputStream());
            if (result.equals("STOP_WAITING")) {
                parent.runOnUiThread(new Runnable() {
                    public void run() {
                        RunTab.modImage(parent, R.id.run_red, R.drawable.run_red, true);
                        RunTab.modImage(parent, R.id.run_green, R.drawable.run_green, true);
                        RunTab.modImage(parent, R.id.run_yellow, R.drawable.run_yellow, true);
                        RunTab.modImage(parent, R.id.run_cyan, R.drawable.run_cyan, true);
                        RunTab.modImage(parent, R.id.bin_red, R.drawable.bin_red, true);
                        RunTab.modImage(parent, R.id.bin_green, R.drawable.bin_green, true);
                        RunTab.modImage(parent, R.id.bin_yellow, R.drawable.bin_yellow, true);
                        RunTab.modImage(parent, R.id.bin_cyan, R.drawable.bin_cyan, true);
                    }
                });

            } else if (result.equals("WAIT")) {
                progressCheckHandler.postDelayed(progressCheckRunner = new Runnable() {
                    public void run() {
                        new NetTask(view, parent).executeOnExecutor(parent.threadPoolExecutor,
                                parent.serverName + "?cmd=BUSY");

                    }
                }, 1000);


            }
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        return null;
    }

}