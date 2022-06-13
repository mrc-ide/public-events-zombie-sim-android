package com.mrc.zombiesim;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "zombie_sim";
    public static final String version = "1.0";
    public static int internal_version = 1;

    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;

    String serverName;

    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(maximumPoolSize);

    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
            keepAliveTime, TimeUnit.SECONDS, workQueue);
/*
    public Updater U = new Updater("https://", "mrcdata.dide.ic.ac.uk",
            "/resources/ZombieSim_version.txt",
            "/resources/ZombieSim.apk",
            internal_version, version, this, true);
*/
    TabLayout tabLayout;
    ZombieViewPager viewPager;
    ZombieFragmentPagerAdapter adapter;

    //////////////////////////////////////////////////////////////
    // Send all the state to the java client
    //////////////////////////////////////////////////////////////

    String state_r0;
    int state_r0_progress;
    String state_tinf;
    int state_tinf_progress;

    String state_vacc;
    int state_vacc_progress;
    String state_vacc_dist;
    int state_vacc_dist_progress;
    int state_vacc_city_index;

    String state_no_seeds;
    int state_no_seeds_progress;
    String state_seed_dist;
    int state_seed_dist_progress;
    int state_seed_city_index;

    int state_mobility = 2;

    boolean state_busy = false;

    public void sendParams(View v, String run_msg) {
        if (run_msg.equals("")) run_msg = "0";

        String params = "R0;Tinf;vaccpc;vaccrad;vacccity;seeds;seedrad;seedcity;mobility;net_msg";
        String vals = state_r0 + ";" + state_tinf + ";" +
                      state_vacc_progress + ";" + state_vacc_dist_progress + ";" + state_vacc_city_index + ";" +
                      state_no_seeds + ";" + state_seed_dist_progress + ";" + state_seed_city_index + ";" +
                      state_mobility + ";" + run_msg;

        new NetTask(v, this).executeOnExecutor(threadPoolExecutor,
                serverName + "?cmd=set&param=" + params + "&value=" + vals);
    }

    //////////////////////////////////////////////////////////////
    // The popup menu - Check for Updates, Set Server, or About //
    //////////////////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
/*
        if (item.getItemId() == R.id.update_check_menu) {
            //U.tryUpdate(false);
            return true;
        }
*/
        if (item.getItemId() == R.id.server_url_menu) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Set Server URL"); //Set Alert dialog title here
            alert.setMessage("Server"); //Message here
            final EditText input = new EditText(MainActivity.this);
            input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
            input.setText(serverName);
            alert.setView(input);
            // End of onClick(DialogInterface dialog, int whichButton)
            alert.setPositiveButton("OK", (dialog, whichButton) -> {
                serverName = input.getEditableText().toString();
                if (!serverName.toUpperCase().startsWith("HTTP://")) serverName="http://"+serverName;
                if (!serverName.endsWith("/")) serverName+="/";
            }); //End of alert.setPositiveButton

            alert.setNegativeButton("CANCEL", (dialog, whichButton) -> dialog.cancel()); //End of alert.setNegativeButton
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            return true;
        }

        if (item.getItemId() == R.id.about_menu) {
            AlertDialog.Builder ver = new AlertDialog.Builder(MainActivity.this);
            ver.setTitle("Zombie Sim Overlord");
            ver.setMessage("Version: "+version );
            ver.setCancelable(false);
            ver.setPositiveButton("OK", (dialog, whichButton) -> dialog.cancel());
            AlertDialog ad = ver.create();
            ad.show();
        }

        return true;
    }


    public static void populate_city_spinner(Spinner spinner, Context c) {
        ArrayList<String> cities = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(c.getAssets().open("cities.csv")));
            String s = br.readLine();
            while (s != null) {
                cities.add(s.split(",")[0]);
                s = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(c,
                android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.zombie_app_yellow);
        actionBar.setTitle(Html.fromHtml("&nbsp;&nbsp;&nbsp;<font color='#003e74'>Zombie Spatial Epidemic Simulator</font>"));

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffdd00"));
        actionBar.setBackgroundDrawable(colorDrawable);

        // Load preferences

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        serverName = settings.getString("server", "http://192.168.1.15:8080/");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText(Html.fromHtml("<font color='#ffffff'>Virus</font>")));
        tabLayout.addTab(tabLayout.newTab().setText(Html.fromHtml("<font color='#ffffff'>Vaccination</font>")));
        tabLayout.addTab(tabLayout.newTab().setText(Html.fromHtml("<font color='#ffffff'>Mobility</font>")));
        tabLayout.addTab(tabLayout.newTab().setText(Html.fromHtml("<font color='#ffffff'>Seeding</font>")));
        tabLayout.addTab(tabLayout.newTab().setText(Html.fromHtml("<font color='#ffffff'>Simulation</font>")));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new ZombieFragmentPagerAdapter(this, getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Save preferences on stopping
    @Override
    protected void onStop() {
        super.onStop();
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("server", serverName);
        // Commit the edits!
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
