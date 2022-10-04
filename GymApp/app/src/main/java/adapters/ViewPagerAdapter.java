package adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import fragments.ClasificacionesFragment;
import fragments.DesdelaAFragment;
import fragments.GimnasiosFragment;
import fragments.RankingFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return  new ClasificacionesFragment();
            case 1:return  new DesdelaAFragment();
            case 2:return  new RankingFragment();
            case 3:return new GimnasiosFragment();
            default:return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
