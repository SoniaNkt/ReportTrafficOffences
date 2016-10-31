package localhost.traffic_offences.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by Sonia on 12/10/2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int pos) {
        Log.d("", "get Item!");
        switch (pos) {
            case 1:
                return ReportFragment.newInstance("FirstFragment, Instance 1");
            case 2: return MapFragment.newInstance("SecondFragment, Instance 1");
//                case 2: return ThirdFragment.newInstance("ThirdFragment, Instance 1");
//                case 3: return ThirdFragment.newInstance("ThirdFragment, Instance 2");
//                case 4: return ThirdFragment.newInstance("ThirdFragment, Instance 3");
            default:
                return ReportFragment.newInstance("ThirdFragment, Default");
        }

    }

//    @Override
//    public int getItemPosition(Object object) {
//        return super.getItemPosition(object);
//    }

    @Override
    public int getCount() {
        return 1;
    }
}
