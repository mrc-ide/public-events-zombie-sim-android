package com.mrc.zombiesim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class VirusTab extends Fragment {
    MainActivity parent;

    String getProgress(int progress, float factor) {
        String r0 = String.valueOf(Math.round(100 + (progress * factor)));
        while (r0.length() < 3) r0 = "0" + r0;
        r0 = "" + r0.charAt(0) + "." + r0.substring(1);
        return r0;
    }

    public VirusTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_virus, container, false);

        // Move the R0 seekbar

        SeekBar r0seek = v.findViewById(R.id.r0_seek);
        TextView r0Val = v.findViewById(R.id.r0_value);

        r0seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new NetTask(v, parent).executeOnExecutor(parent.threadPoolExecutor,
                        parent.serverName + "?cmd=set&param=R0&value=" + r0Val.getText());

                //http://127.0.0.1:8080/?cmd=set&param=R0&value=2
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r0Val.setText(getProgress(progress, 1.5f));
            }
        });

        // Move the T_inf seekbar

        SeekBar tinf_seek = v.findViewById(R.id.tinf_seek);
        TextView tinf_val = v.findViewById(R.id.tinf_value);

        tinf_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new NetTask(v, parent).executeOnExecutor(parent.threadPoolExecutor,
                        parent.serverName + "?cmd=set&param=Tinf&value=" + tinf_val.getText());

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


        return v;
    }
}