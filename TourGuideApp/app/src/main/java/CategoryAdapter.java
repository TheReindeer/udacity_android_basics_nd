package com.example.android.tourguideapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by john on 26.10.2016.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new AttractionsFragment();
        } else if (position == 1){
            return new HotelsFragment();
        } else if (position == 2){
            return new RestaurantsFragment();
        } else {
            return new FoodFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return mContext.getString(R.string.category_attractions);
        } else if (position == 1){
            return mContext.getString(R.string.category_hotels);
        } else if (position == 2){
            return mContext.getString(R.string.category_restaurants);
        } else {
            return mContext.getString(R.string.category_food);
        }
    }
}
