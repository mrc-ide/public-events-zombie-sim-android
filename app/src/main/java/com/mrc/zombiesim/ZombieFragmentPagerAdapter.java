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
                return new VirusTab();
            case 1:
                return new VaccinationTab();
            case 2:
                return new MobilityTab();
            case 3:
                return new SeedingTab();
            case 4:
                return new RunTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}