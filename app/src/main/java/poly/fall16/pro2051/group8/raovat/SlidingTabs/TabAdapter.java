package poly.fall16.pro2051.group8.raovat.SlidingTabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import poly.fall16.pro2051.group8.raovat.Fragments.CategoryFragment;
import poly.fall16.pro2051.group8.raovat.Fragments.MessageFragment;
import poly.fall16.pro2051.group8.raovat.Fragments.NotificationFragment;
import poly.fall16.pro2051.group8.raovat.Fragments.OtherFragment;

/**
 * Created by giang on 5/4/2016.
 */
public class TabAdapter extends FragmentPagerAdapter {
    Context mContext;

    CharSequence Titles[] = {"Danh mục", "Liên lạc", "Thông báo", "Khác"};
    //String[] titles = {"Comporation", "Money"};
    //int [] icons = {R.drawable.comporation_blue, R.drawable.money_blue};
    int heightIcon;

    public TabAdapter(FragmentManager fm, Context c) {
        super(fm);

        mContext = c;
        double scale = c.getResources().getDisplayMetrics().density;
        heightIcon = (int)(24*scale + 0.5f);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment mFragment = null;
        switch (position) {
            case 0:
                mFragment = new CategoryFragment();
                break;
            case 1:
                mFragment = new MessageFragment();
                break;
            case 2:
                mFragment = new NotificationFragment();
                break;
            case 3:
                mFragment = new OtherFragment();
        }

        Bundle b = new Bundle();
        b.putInt("position", position);

        mFragment.setArguments(b);
        return mFragment;
    }



    @Override
    public int getCount() {
        return Titles.length;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//
//        Drawable d = mContext.getResources().getDrawable(icons[position]);
//        d.setBounds(0,0, heightIcon, heightIcon);
//
//        ImageSpan is = new ImageSpan(d);
//        SpannableString sp = new SpannableString(" ");
//        sp.setSpan(is, 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        return (sp);
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}
