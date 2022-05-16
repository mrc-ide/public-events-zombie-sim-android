package com.mrc.zombiesim;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

class ZombieFragmentPagerAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public ZombieFragmentPagerAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                VirusTab virusTab = new VirusTab();
                virusTab.parent = (MainActivity) context;
                return virusTab;
            case 1:
                VaccinationTab vaccTab = new VaccinationTab();
                vaccTab.parent = (MainActivity) context;
                return vaccTab;
            case 2:
                SeedingTab seedTab = new SeedingTab();
                seedTab.parent = (MainActivity) context;
                return seedTab;
            case 3:
                RunTab runTab = new RunTab();
                runTab.parent = (MainActivity) context;
                return runTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}