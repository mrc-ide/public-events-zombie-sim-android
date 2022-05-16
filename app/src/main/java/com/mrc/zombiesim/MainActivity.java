package com.mrc.zombiesim;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    String serverName = "http://192.168.1.15:8080/";

    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
            keepAliveTime, TimeUnit.SECONDS, workQueue);

    public Updater U = new Updater("https://", "mrcdata.dide.ic.ac.uk",
            "/resources/ZombieSim_version.txt",
            "/resources/ZombieSim.apk",
            internal_version, version, this, true);

    TabLayout tabLayout;
    ZombieViewPager viewPager;

    /************************************************************/
    /* The popup menu - Check for Updates, Set Server, or About */
    /************************************************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.update_check_menu) {
            //U.tryUpdate(false);
            return true;
        }

        if (item.getItemId() == R.id.server_url_menu) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Set Server URL"); //Set Alert dialog title here
            alert.setMessage("Server"); //Message here
            final EditText input = new EditText(MainActivity.this);
            input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
            input.setText(serverName);
            alert.setView(input);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    serverName = input.getEditableText().toString();
                    if (!serverName.toUpperCase().startsWith("HTTP://")) serverName="http://"+serverName;
                    if (!serverName.endsWith("/")) serverName+="/";
                } // End of onClick(DialogInterface dialog, int whichButton)
            }); //End of alert.setPositiveButton

            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) { dialog.cancel(); }
            }); //End of alert.setNegativeButton
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            return true;
        }

        if (item.getItemId() == R.id.about_menu) {
            AlertDialog.Builder ver = new AlertDialog.Builder(MainActivity.this);
            ver.setTitle("Zombie Sim Overlord");
            ver.setMessage("Version: "+version );
            ver.setCancelable(false);
            ver.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            AlertDialog ad = ver.create();
            ad.show();
        }

        return true;
    }


    public static void populate_city_spinner(Spinner spinner, Context c, String first) {
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

        cities.set(0, first);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(c,
                android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Virus"));
        tabLayout.addTab(tabLayout.newTab().setText("Vaccination"));
        tabLayout.addTab(tabLayout.newTab().setText("Seeding"));
        tabLayout.addTab(tabLayout.newTab().setText("Simulation"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ZombieFragmentPagerAdapter adapter;
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
}
