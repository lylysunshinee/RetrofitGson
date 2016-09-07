package io.fruitful.dong.retrofitgson.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import io.fruitful.dong.retrofitgson.Utils;
import io.fruitful.dong.retrofitgson.fragment.DetailLocationFragment;
import io.fruitful.dong.retrofitgson.model.Result;

/**
 * Created by Ares on 8/25/2016.
 */
public class ViewPagerAdapter  extends FragmentStatePagerAdapter{
    private ArrayList<Result> mResults;

    public ViewPagerAdapter(FragmentManager fm,ArrayList<Result> mResults) {
        super(fm);
        this.mResults=mResults;
    }

    @Override
    public Fragment getItem(int position) {

        DetailLocationFragment fragment = new DetailLocationFragment();
        Result mResult = mResults.get(position);
        Bundle data = new Bundle();
        data.putParcelable(Utils.TAG_LOCATION_DATA, mResult);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public int getCount() {
        return mResults.size();
    }
}
