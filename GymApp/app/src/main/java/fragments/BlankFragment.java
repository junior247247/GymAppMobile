package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.makingfight.gymapp.R;
import com.google.android.material.tabs.TabLayout;

import adapters.ViewPagerAdapter;


public class BlankFragment extends Fragment {
    View view;
    TabLayout tabLayout;
    ViewPager viewPager;

    public BlankFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_blank, container, false);

          tabLayout=view.findViewById(R.id.tabLoutAtletes);
         viewPager=view.findViewById(R.id.viewPagerAtletas);
       tabLayout.addTab(tabLayout.newTab().setText("Peso"));
       tabLayout.addTab(tabLayout.newTab().setText("Nombre"));
       tabLayout.addTab(tabLayout.newTab().setText("Ranking"));
        tabLayout.addTab(tabLayout.newTab().setText("Gimnasios"));
       viewPager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager()));
       viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {


              viewPager.setCurrentItem(tab.getPosition());
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });




        return view;
    }
}