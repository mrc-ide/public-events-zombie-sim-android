package com.mrc.zombiesim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class SeedingTab extends Fragment {
    MainActivity parent;

    public SeedingTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_seeding, container, false);
        MainActivity.populate_city_spinner(v.findViewById(R.id.seed_city), v.getContext(),
                "- Random -");

        // Move the number of seeds bar

        SeekBar seeds_seek = v.findViewById(R.id.seeds_seek);
        TextView seeds_val = v.findViewById(R.id.seeds_val);

        seeds_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new NetTask(v, parent).executeOnExecutor(parent.threadPoolExecutor,
                        parent.serverName + "?cmd=set&param=seeds&value=" + seeds_val.getText());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seeds_val.setText(String.valueOf(progress + 1));
            }
        });

        // Move the seed distance

        SeekBar seed_dist_seek = v.findViewById(R.id.seed_dist_seek);
        TextView seed_dist_val = v.findViewById(R.id.seed_dist_val);

        seed_dist_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new NetTask(v, parent).executeOnExecutor(parent.threadPoolExecutor,
                        parent.serverName + "?cmd=set&param=seedrad&value=" +
                                seed_dist_seek.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seed_dist_val.setText(String.valueOf(progress)+" km");
            }
        });

        // City selector

       Spinner seed_city_spinner = v.findViewById(R.id.seed_city);

        seed_city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new NetTask(v, parent).executeOnExecutor(parent.threadPoolExecutor,
                        parent.serverName + "?cmd=set&param=seedcity&value=" + i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}


        });


        return v;
    }
}