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


public class VaccinationTab extends Fragment {
    MainActivity parent;

    public VaccinationTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_vaccination, container, false);
        MainActivity.populate_city_spinner(v.findViewById(R.id.vacc_city), v.getContext(),
                "- Nowhere -");

        // Move the vacc coverage bar

        SeekBar vacc_cov_seek = v.findViewById(R.id.vacc_cov_seek);
        TextView vacc_cov_val = v.findViewById(R.id.vacc_cov_val);

        vacc_cov_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new NetTask(v, parent).executeOnExecutor(parent.threadPoolExecutor,
                        parent.serverName + "?cmd=set&param=vaccpc&value=" + vacc_cov_seek.getProgress());

                //http://127.0.0.1:8080/?cmd=set&param=vaccpc&value=45
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vacc_cov_val.setText(progress + " %");
            }
        });

        // Move the vacc distance

        SeekBar vacc_dist_seek = v.findViewById(R.id.vacc_dist_seek);
        TextView vacc_dist_val = v.findViewById(R.id.vacc_dist_val);

        vacc_dist_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new NetTask(v, parent).executeOnExecutor(parent.threadPoolExecutor,
                        parent.serverName + "?cmd=set&param=vaccrad&value=" + vacc_dist_val.getText());

                //http://127.0.0.1:8080/?cmd=set&param=vaccpc&value=45
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vacc_dist_val.setText(progress + " km");
            }
        });

        // Vaccination city

        Spinner vacc_city_spinner = v.findViewById(R.id.vacc_city);
        vacc_city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new NetTask(v, parent).executeOnExecutor(parent.threadPoolExecutor,
                        parent.serverName + "?cmd=set&param=vacccity&value=" + i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }


        });

        return v;
    }
}