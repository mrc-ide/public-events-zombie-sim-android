package com.mrc.zombiesim;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class RunTab extends Fragment {
    public RunTab() {
        // Required empty public constructor
    }

    protected static void modImage(MainActivity parent, int id_comp, int id_draw, boolean enab) {
        ImageView iv = parent.findViewById(id_comp);
        iv.setImageResource(id_draw);
        iv.setEnabled(enab);
    }

    private void addButtonEvent(View v, int id, String net_msg) {
        ImageView iv = v.findViewById(id);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If we're running a model, prevent running the next one til this
                // one has finished. Disable/grey-out the relevant buttons - then
                // see NetTask; keep polling until a "STOP_WAITING" message is
                // received.

                // For deleting, we want a shorter pause, and only to disable the
                // button just pushed - for a "positive" effect rather than any
                // functional reason...
                MainActivity parent = (MainActivity) getActivity();
                parent.runOnUiThread(new Runnable() {
                    public void run() {
                        if (net_msg.startsWith("R")) {
                            RunTab.modImage(parent, R.id.run_red, R.drawable.run_grey, false);
                            RunTab.modImage(parent, R.id.run_green, R.drawable.run_grey, false);
                            RunTab.modImage(parent, R.id.run_yellow, R.drawable.run_grey, false);
                            RunTab.modImage(parent, R.id.run_cyan, R.drawable.run_grey, false);
                        }

                        if ((net_msg.startsWith("R")) || (net_msg.equals("D0")))
                            RunTab.modImage(parent, R.id.bin_red, R.drawable.bin_grey, false);
                        if ((net_msg.startsWith("R")) || (net_msg.equals("D1")))
                            RunTab.modImage(parent, R.id.bin_green, R.drawable.bin_grey, false);
                        if ((net_msg.startsWith("R")) || (net_msg.equals("D2")))
                            RunTab.modImage(parent, R.id.bin_yellow, R.drawable.bin_grey, false);
                        if ((net_msg.startsWith("R")) || (net_msg.equals("D3")))
                            RunTab.modImage(parent, R.id.bin_cyan, R.drawable.bin_grey, false);
                    }
                });

                new NetTask(v, parent).executeOnExecutor(parent.threadPoolExecutor,
                                      parent.serverName +"?cmd="+net_msg);

                }
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


            return v;
        }
    }