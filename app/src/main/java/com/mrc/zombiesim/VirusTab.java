package com.mrc.zombiesim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class VirusTab extends Fragment {
    // It seems we can't rely on the fragment UI to still exist
    // when saving the state. So we'll copy it to our own state.

    String getProgress(int progress, float factor) {
        StringBuilder r0 = new StringBuilder(String.valueOf(Math.round(100 + (progress * factor))));
        while (r0.length() < 3) r0.insert(0, "0");
        r0 = new StringBuilder("" + r0.charAt(0) + "." + r0.substring(1));
        return r0.toString();
    }

    public VirusTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_virus, container, false);
        MainActivity ma = (MainActivity) super.getActivity();
        assert ma != null;

        // Move the R0 seekbar

        SeekBar r0_seek = v.findViewById(R.id.r0_seek);
        TextView r0_val = v.findViewById(R.id.r0_value);

        r0_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ma.state_r0 = String.valueOf(r0_val.getText());
                ma.state_r0_progress = r0_seek.getProgress();
                System.out.println("R0 CHANGED");
                ma.sendParams(v, "");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r0_val.setText(getProgress(progress, 1.5f));
            }
        });

        // Move the T_inf seekbar

        SeekBar tinf_seek = v.findViewById(R.id.tinf_seek);
        TextView tinf_val = v.findViewById(R.id.tinf_value);

        tinf_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ma.state_tinf = String.valueOf(tinf_val.getText());
                ma.state_tinf_progress = tinf_seek.getProgress();
                System.out.println("TINF CHANGED");
                ma.sendParams(v, "");

                //http://127.0.0.1:8080/?cmd=set&param=Tinf&value=2
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tinf_val.setText(getProgress(progress, 2f));
            }
        });

        // Retrieve settings after a rotate
/*
        if (savedInstanceState != null) {
            ma.state_r0 = savedInstanceState.getString("r0_text");
            ma.state_r0_progress = savedInstanceState.getInt("r0");
            ma.state_tinf = savedInstanceState.getString("tinf_text");
            ma.state_tinf_progress = savedInstanceState.getInt("tinf");

            r0_val.setText(ma.state_r0);
            r0_seek.setProgress(ma.state_r0_progress);
            tinf_val.setText(ma.state_tinf);
            tinf_seek.setProgress(ma.state_tinf_progress);
        }
*/
        // Copy init to state

        ma.state_r0_progress = r0_seek.getProgress();
        ma.state_r0 = String.valueOf(r0_val.getText());
        ma.state_tinf_progress = tinf_seek.getProgress();
        ma.state_tinf = String.valueOf(tinf_val.getText());

        return v;
    }
/*
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        MainActivity ma = (MainActivity) super.getActivity();
        assert ma != null;
        outState.putInt("r0", ma.state_r0_progress);
        outState.putString("r0_text", ma.state_r0);
        outState.putInt("tinf", ma.state_tinf_progress);
        outState.putString("tinf_text", ma.state_tinf);
    }
*/
}