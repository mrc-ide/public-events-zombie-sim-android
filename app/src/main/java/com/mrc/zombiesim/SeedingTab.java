package com.mrc.zombiesim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class SeedingTab extends Fragment {

    public SeedingTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_seeding, container, false);
        MainActivity ma = (MainActivity) super.getActivity();
        assert ma != null;
        MainActivity.populate_city_spinner(v.findViewById(R.id.seed_city), v.getContext());

        // Move the number of seeds bar

        SeekBar seeds_seek = v.findViewById(R.id.seeds_seek);
        TextView seeds_val = v.findViewById(R.id.seeds_val);

        seeds_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MainActivity ma = (MainActivity) getActivity();
                assert ma != null;
                ma.state_no_seeds = String.valueOf(seeds_val.getText());
                ma.state_no_seeds_progress = seeds_seek.getProgress();
                System.out.println("SEED_SEEK CHANGED");
                ma.sendParams(v, "");
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
                MainActivity ma = (MainActivity) getActivity();
                assert ma != null;
                ma.state_seed_dist_progress = seed_dist_seek.getProgress();
                ma.state_seed_dist = String.valueOf(seed_dist_val.getText());
                System.out.println("SEED_DIST CHANGED");
                ma.sendParams(v, "");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String p2 = progress + " km";
                seed_dist_val.setText(p2);
            }
        });

        // City selector

        Spinner seed_city_spinner = v.findViewById(R.id.seed_city);

        seed_city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity ma = (MainActivity) getActivity();
                assert ma != null;
                if (ma.state_seed_city_index != i) {
                    ma.state_seed_city_index = i;
                    System.out.println("SEED_CITY CHANGED");
                    ma.sendParams(v, "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}


        });

        // Retrieve settings after a rotate
/*
        if (savedInstanceState != null) {
            ma.state_no_seeds_progress = savedInstanceState.getInt("seed");
            ma.state_no_seeds = savedInstanceState.getString("seed_text");
            ma.state_seed_dist_progress = savedInstanceState.getInt("seed_dist");
            ma.state_seed_dist = savedInstanceState.getString("seed_dist_text");
            ma.state_seed_city_index = savedInstanceState.getInt("seed_sel");

            seeds_seek.setProgress(ma.state_no_seeds_progress);
            seeds_val.setText(ma.state_no_seeds);
            seed_dist_seek.setProgress(ma.state_seed_dist_progress);
            seed_dist_val.setText(ma.state_seed_dist);
            seed_city_spinner.setSelection(ma.state_seed_city_index);

        }
*/
        // Copy init to state

        ma.state_no_seeds = String.valueOf(seeds_val.getText());
        ma.state_no_seeds_progress = seeds_seek.getProgress();
        ma.state_seed_dist = String.valueOf(seed_dist_val.getText());
        ma.state_seed_dist_progress = seed_dist_seek.getProgress();
        ma.state_seed_city_index = seed_city_spinner.getSelectedItemPosition();

        return v;
    }
/*
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        MainActivity ma = (MainActivity) super.getActivity();
        assert ma != null;
        outState.putInt("seed", ma.state_no_seeds_progress);
        outState.putString("seed_text", ma.state_no_seeds);
        outState.putInt("seed_dist", ma.state_seed_dist_progress);
        outState.putString("seed_dist_text", ma.state_seed_dist);
        outState.putInt("seed_sel", ma.state_seed_city_index);
    }
*/
}