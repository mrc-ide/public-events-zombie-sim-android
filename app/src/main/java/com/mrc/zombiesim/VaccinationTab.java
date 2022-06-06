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


public class VaccinationTab extends Fragment {

    public VaccinationTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_vaccination, container, false);
        MainActivity ma = (MainActivity) super.getActivity();
        assert ma != null;

        MainActivity.populate_city_spinner(v.findViewById(R.id.vacc_city), v.getContext());

        // Move the vacc coverage bar

        SeekBar vacc_cov_seek = v.findViewById(R.id.vacc_cov_seek);
        TextView vacc_cov_val = v.findViewById(R.id.vacc_cov_val);

        vacc_cov_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MainActivity ma = (MainActivity) getActivity();
                assert ma != null;
                ma.state_vacc = String.valueOf(vacc_cov_val.getText());
                ma.state_vacc_progress = vacc_cov_seek.getProgress();
                System.out.println("VACC_COV_SEEK CHANGED");
                ma.sendParams(v, "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String p2 = progress + "%";
                vacc_cov_val.setText(p2);
            }
        });

        // Move the vacc distance

        SeekBar vacc_dist_seek = v.findViewById(R.id.vacc_dist_seek);
        TextView vacc_dist_val = v.findViewById(R.id.vacc_dist_val);

        vacc_dist_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MainActivity ma = (MainActivity) getActivity();
                assert ma != null;
                ma.state_vacc_dist_progress = vacc_dist_seek.getProgress();
                ma.state_vacc_dist = String.valueOf(vacc_dist_val.getText());
                System.out.println("VACC_DIST_SEEK CHANGED");
                ma.sendParams(v, "");

                //http://127.0.0.1:8080/?cmd=set&param=vaccpc&value=45
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String p2 = progress + " km";
                vacc_dist_val.setText(p2);
            }
        });

        // Vaccination city

        Spinner vacc_city_spinner = v.findViewById(R.id.vacc_city);
        vacc_city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity ma = (MainActivity) getActivity();
                assert ma != null;
                if (ma.state_vacc_city_index != i) {
                    ma.state_vacc_city_index = i;
                    System.out.println("VACC_CITY_SPINNER CHANGED");
                    ma.sendParams(v, "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }


        });

        // Retrieve settings after a rotate
/*
        if (savedInstanceState != null) {
            ma.state_vacc_progress = savedInstanceState.getInt("vacc");
            ma.state_vacc = savedInstanceState.getString("vacc_text");
            ma.state_vacc_dist_progress = savedInstanceState.getInt("vacc_dist");
            ma.state_vacc_dist = savedInstanceState.getString("vacc_dist_text");
            ma.state_vacc_city_index = savedInstanceState.getInt("vacc_sel");

            vacc_cov_seek.setProgress(ma.state_vacc_progress);
            vacc_cov_val.setText(ma.state_vacc);
            vacc_dist_seek.setProgress(ma.state_vacc_dist_progress);
            vacc_dist_val.setText(ma.state_vacc_dist);
            vacc_city_spinner.setSelection(ma.state_vacc_city_index);
        }
*/
        // Copy init to state

        ma.state_vacc = String.valueOf(vacc_cov_val.getText());
        ma.state_vacc_progress = vacc_cov_seek.getProgress();
        ma.state_vacc_dist = String.valueOf(vacc_dist_val.getText());
        ma.state_vacc_dist_progress = vacc_dist_seek.getProgress();
        ma.state_vacc_city_index = vacc_city_spinner.getSelectedItemPosition();


        return v;
    }
/*
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        MainActivity ma = (MainActivity) super.getActivity();
        assert ma != null;
        outState.putInt("vacc", ma.state_vacc_progress);
        outState.putString("vacc_text", ma.state_vacc);
        outState.putInt("vacc_dist", ma.state_vacc_dist_progress);
        outState.putString("vacc_dist_text", ma.state_vacc_dist);
        outState.putInt("vacc_sel", ma.state_vacc_city_index);
    }
*/
}