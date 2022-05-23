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

    // It seems we can't rely on the fragment UI to still exist
    // when saving the state. So we'll copy it to our own state.

    String state_no_seeds;
    int state_no_seeds_progress;
    String state_seed_dist;
    int state_seed_dist_progress;
    int state_seed_city_index;

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
                MainActivity ma = (MainActivity) getActivity();
                state_no_seeds = String.valueOf(seeds_val.getText());
                state_no_seeds_progress = seeds_seek.getProgress();
                new NetTask(v, ma).executeOnExecutor(ma.threadPoolExecutor,
                        ma.serverName + "?cmd=set&param=seeds&value=" + state_no_seeds);
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
                state_seed_dist_progress = seed_dist_seek.getProgress();
                state_seed_dist = String.valueOf(seed_dist_val.getText());

                new NetTask(v, ma).executeOnExecutor(ma.threadPoolExecutor,
                        ma.serverName + "?cmd=set&param=seedrad&value=" +
                                state_seed_dist_progress);

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
                MainActivity ma = (MainActivity) getActivity();
                state_seed_city_index = i;
                new NetTask(v, ma).executeOnExecutor(ma.threadPoolExecutor,
                        ma.serverName + "?cmd=set&param=seedcity&value=" + i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}


        });

        // Retrieve settings after a rotate

        if (savedInstanceState != null) {
            state_no_seeds_progress = savedInstanceState.getInt("seed");
            state_no_seeds = savedInstanceState.getString("seed_text");
            state_seed_dist_progress = savedInstanceState.getInt("seed_dist");
            state_seed_dist = savedInstanceState.getString("seed_dist_text");
            state_seed_city_index = savedInstanceState.getInt("seed_sel");

            seeds_seek.setProgress(state_no_seeds_progress);
            seeds_val.setText(state_no_seeds);
            seed_dist_seek.setProgress(state_seed_dist_progress);
            seed_dist_val.setText(state_seed_dist);
            seed_city_spinner.setSelection(state_seed_city_index);

        }

        // Copy init to state

        state_no_seeds = String.valueOf(seeds_val.getText());
        state_no_seeds_progress = seeds_seek.getProgress();
        state_seed_dist = String.valueOf(seed_dist_val.getText());
        state_seed_dist_progress = seed_dist_seek.getProgress();
        state_seed_city_index = seed_city_spinner.getSelectedItemPosition();

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        View v = getView();
        outState.putInt("seed", state_no_seeds_progress);
        outState.putString("seed_text", state_no_seeds);
        outState.putInt("seed_dist", state_seed_dist_progress);
        outState.putString("seed_dist_text", state_seed_dist);
        outState.putInt("seed_sel", state_seed_city_index);
    }

}