package com.app.jhon.galeriafinal.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.jhon.galeriafinal.Views.FragmentFavoritos;
import com.app.jhon.galeriafinal.Views.FragmentFotos;

/**
 * Created by David on 28/11/2017.
 */

public class AdapterGallery extends FragmentPagerAdapter {

    private static final int PAGE_COUNT =2;
    private static final String TITLE1 = "Fotos";
    private static final String TITLE2 = "Favoritos";

    public AdapterGallery(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment=new FragmentFotos();
                break;
            case 1:
                 fragment= new FragmentFavoritos();

                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return TITLE1;
            case 1: return TITLE2;
        }

        return super.getPageTitle(position);
    }
}
