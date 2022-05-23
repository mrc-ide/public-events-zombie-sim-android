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
    // It seems we can't rely on the fragment UI to still exist
    // when saving the state. So we'll copy it to our own state.

    String state_vacc;
    int state_vacc_progress;
    String state_vacc_dist;
    int state_vacc_dist_progress;
    int state_vacc_city_index;

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
                MainActivity ma = (MainActivity) getActivity();
                state_vacc = String.valueOf(vacc_cov_val.getText());
                state_vacc_progress = vacc_cov_seek.getProgress();

                new NetTask(v, ma).executeOnExecutor(ma.threadPoolExecutor,
                        ma.serverName + "?cmd=set&param=vaccpc&value=" + vacc_cov_seek.getProgress());

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
                MainActivity ma = (MainActivity) getActivity();
                state_vacc_dist_progress = vacc_dist_seek.getProgress();
                state_vacc_dist = String.valueOf(vacc_dist_val.getText());
                new NetTask(v, ma).executeOnExecutor(ma.threadPoolExecutor,
                        ma.serverName + "?cmd=set&param=vaccrad&value=" + vacc_dist_val.getText());

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
                MainActivity ma = (MainActivity) getActivity();
                state_vacc_city_index = i;
                new NetTask(v, ma).executeOnExecutor(ma.threadPoolExecutor,
                        ma.serverName + "?cmd=set&param=vacccity&value=" + i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }


        });

        // Retrieve settings after a rotate

        if (savedInstanceState != null) {
            state_vacc_progress = savedInstanceState.getInt("vacc");
            state_vacc = savedInstanceState.getString("vacc_text");
            state_vacc_dist_progress = savedInstanceState.getInt("vacc_dist");
            state_vacc_dist = savedInstanceState.getString("vacc_dist_text");
            state_vacc_city_index = savedInstanceState.getInt("vacc_sel");

            vacc_cov_seek.setProgress(state_vacc_progress);
            vacc_cov_val.setText(state_vacc);
            vacc_dist_seek.setProgress(state_vacc_dist_progress);
            vacc_dist_val.setText(state_vacc_dist);
            vacc_city_spinner.setSelection(state_vacc_city_index);
        }

        // Copy init to state

        state_vacc = String.valueOf(vacc_cov_val.getText());
        state_vacc_progress = vacc_cov_seek.getProgress();
        state_vacc_dist = String.valueOf(vacc_dist_val.getText());
        state_vacc_dist_progress = vacc_dist_seek.getProgress();
        state_vacc_city_index = vacc_city_spinner.getSelectedItemPosition();


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        View v = getView();
        outState.putInt("vacc", state_vacc_progress);
        outState.putString("vacc_text", state_vacc);
        outState.putInt("vacc_dist", state_vacc_dist_progress);
        outState.putString("vacc_dist_text", state_vacc_dist);
        outState.putInt("vacc_sel", state_vacc_city_index);
    }

}