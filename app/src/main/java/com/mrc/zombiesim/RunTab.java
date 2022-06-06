package com.mrc.zombiesim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class RunTab extends Fragment {
    public RunTab() {
        // Required empty public constructor
    }

    protected static void modImage(MainActivity parent, int id_button,
                                   int id_ready, int id_busy, boolean busy) {
        ImageView iv = parent.findViewById(id_button);
        if (iv != null) {
            iv.setImageResource(busy?id_busy:id_ready);
            iv.setEnabled(!busy);
        }
    }

    public static void updateButtons(MainActivity parent) {
        RunTab.modImage(parent, R.id.run_red, R.drawable.run_red, R.drawable.run_grey, parent.state_busy);
        RunTab.modImage(parent, R.id.run_green, R.drawable.run_green, R.drawable.run_grey, parent.state_busy);
        RunTab.modImage(parent, R.id.run_yellow, R.drawable.run_yellow, R.drawable.run_grey, parent.state_busy);
        RunTab.modImage(parent, R.id.run_cyan, R.drawable.run_cyan, R.drawable.run_grey, parent.state_busy);
        RunTab.modImage(parent, R.id.bin_red, R.drawable.bin_red, R.drawable.bin_grey, parent.state_busy);
        RunTab.modImage(parent, R.id.bin_green, R.drawable.bin_green, R.drawable.bin_grey, parent.state_busy);
        RunTab.modImage(parent, R.id.bin_yellow, R.drawable.bin_yellow, R.drawable.bin_grey, parent.state_busy);
        RunTab.modImage(parent, R.id.bin_cyan, R.drawable.bin_cyan, R.drawable.bin_grey, parent.state_busy);

    }

    private void addButtonEvent(View v, int id, String net_msg) {
        ImageView iv = v.findViewById(id);
        iv.setOnClickListener(v1 -> {

            // If we're running a model, prevent running the next one til this
            // one has finished. Disable/grey-out the relevant buttons - then
            // see NetTask; keep polling until a "STOP_WAITING" message is
            // received.

            // For deleting, we want a shorter pause, and only to disable the
            // button just pushed - for a "positive" effect rather than any
            // functional reason...

            MainActivity parent = (MainActivity) getActivity();
            assert parent != null;
            parent.runOnUiThread(() -> {
                if (parent.state_busy) {
                    updateButtons(parent);
                }
                else {
                    parent.state_busy = true;
                    updateButtons(parent);
                    parent.sendParams(v1, net_msg);
                }

            });
        });
    }

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){

            View v = inflater.inflate(R.layout.fragment_run, container, false);
            addButtonEvent(v, R.id.run_red, "R0");
            addButtonEvent(v, R.id.run_green, "R1");
            addButtonEvent(v, R.id.run_yellow, "R2");
            addButtonEvent(v, R.id.run_cyan, "R3");
            addButtonEvent(v, R.id.bin_red, "D0");
            addButtonEvent(v, R.id.bin_green, "D1");
            addButtonEvent(v, R.id.bin_yellow, "D2");
            addButtonEvent(v, R.id.bin_cyan, "D3");
            MainActivity ma = (MainActivity) getActivity();
            assert ma != null;
            ma.runOnUiThread(() -> updateButtons(ma));

            return v;
        }

    }