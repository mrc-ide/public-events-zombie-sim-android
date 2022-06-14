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
    int ignore_first_item_select;

    public VaccinationTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        boolean disabled = true;
        ignore_first_item_select = 0;
        View v = inflater.inflate(R.layout.fragment_vaccination, container, false);
        MainActivity ma = (MainActivity) super.getActivity();
        assert ma != null;

        MainActivity.populate_city_spinner(v.findViewById(R.id.vacc_city), v.getContext(), "- No Vacc -");

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
                ma.sendParams("");
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
                ma.sendParams("");

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
                if (ignore_first_item_select == 0) {
                    ignore_first_item_select = 1;
                    return;
                }
                MainActivity ma = (MainActivity) getActivity();
                assert ma != null;
                System.out.println("ON SELECTED - i = "+i+", L = "+l+", state = "+ma.state_vacc_city_index);
                if (ma.state_vacc_city_index != i) {
                    ma.state_vacc_city_index = i;
                    System.out.println("VACC_CITY_SPINNER CHANGED to "+i);
                    ma.sendParams("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }


        });

        vacc_cov_seek.post(() -> vacc_cov_seek.setProgress(ma.state_vacc_progress));
        vacc_cov_val.post(() -> vacc_cov_val.setText(ma.state_vacc));
        vacc_dist_seek.post(() -> vacc_dist_seek.setProgress(ma.state_vacc_dist_progress));
        vacc_dist_val.post(() -> vacc_dist_val.setText(ma.state_vacc_dist));
        vacc_city_spinner.post(() -> vacc_city_spinner.setSelection(ma.state_vacc_city_index));

        return v;
    }

}