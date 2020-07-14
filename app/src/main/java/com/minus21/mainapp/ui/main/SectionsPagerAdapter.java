package com.minus21.mainapp.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.minus21.mainapp.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Fragment fragment1, fragment2, fragment3;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;     // MainActivity context
        fragment1 = null;
        fragment2 = null;
        fragment3 = null;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch(position){
            case 0:
                if (fragment1 == null){
                    fragment1 = PlaceholderFragment1.newInstance();
                }
                fragment = fragment1;
                break;
            case 1:
                if (fragment2 == null){
                    fragment2 = PlaceholderFragment2.newInstance();
                }
                fragment = fragment2;
                break;
            case 2:
                if (fragment3 == null){
                    fragment3 = PlaceholderFragment3.newInstance();
                }
                fragment = fragment3;
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}