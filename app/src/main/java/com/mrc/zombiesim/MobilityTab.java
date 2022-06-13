package com.mrc.zombiesim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class MobilityTab extends Fragment {

    public MobilityTab() {
        // Required empty public constructor
    }

    private void addMobilityRadio(RadioButton rb, int id, boolean checked) {
        MainActivity ma = (MainActivity) super.getActivity();
        rb.setOnClickListener(v1 -> {
            assert ma != null;
            ma.state_mobility = id;
            ma.sendParams("");
        });
        rb.post(() -> rb.setChecked(checked));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mobility, container, false);
        MainActivity ma = (MainActivity) super.getActivity();
        assert ma != null;

        RadioButton rb_slow = v.findViewById(R.id.mobility_slow);
        RadioButton rb_med = v.findViewById(R.id.mobility_med);
        RadioButton rb_fast = v.findViewById(R.id.mobility_fast);
        RadioButton rb_fly = v.findViewById(R.id.mobility_fly);

        addMobilityRadio(rb_slow, 1, ma.state_mobility == 1);
        addMobilityRadio(rb_med, 2, ma.state_mobility == 2);
        addMobilityRadio(rb_fast, 3, ma.state_mobility == 3);
        addMobilityRadio(rb_fly, 4, ma.state_mobility == 4);

        return v;
    }
/*
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        MainActivity ma = (MainActivity) super.getActivity();
        assert ma != null;
        outState.putInt("mobility", ma.state_mobility);
    }
*/
}