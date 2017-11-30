package com.app.jhon.galeriafinal.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.jhon.galeriafinal.Adapters.AdapterViewPager;
import com.app.jhon.galeriafinal.R;

/**
 * Created by David on 28/11/2017.
 */

public class FragmentTabs extends Fragment {

    private TabLayout tabs ;
    private ViewPager viewPager;
    private AdapterViewPager adapterPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabs = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);

        adapterPager = new AdapterViewPager(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(adapterPager);

        tabs.setupWithViewPager(viewPager,true);
    }
}
