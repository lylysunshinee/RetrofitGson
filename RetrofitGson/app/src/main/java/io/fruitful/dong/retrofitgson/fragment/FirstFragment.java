package io.fruitful.dong.retrofitgson.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import io.fruitful.dong.retrofitgson.MainActivity;
import io.fruitful.dong.retrofitgson.R;

/**
 * Created by Ares on 8/19/2016.
 */
public class FirstFragment extends Fragment implements View.OnClickListener {
    private Button mGson, mRetrofit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_layout, container, false);
        mGson = (Button) view.findViewById(R.id.btn_gson);
        mRetrofit = (Button) view.findViewById(R.id.btn_retrofit);
        mGson.setOnClickListener(this);
        mRetrofit.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gson:
                onGson();
                break;
            case R.id.btn_retrofit:
                onRetrofit();
                break;

        }
    }

    private void onRetrofit() {
        Fragment mFragment = new RetrofitFragment();
        getActivity()

                .getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left , R.anim.exit_to_right)
                .replace(R.id.frame, mFragment).addToBackStack(null).commit();

    }

    private void onGson() {
        Fragment mFragment = new ReadFileAssetsFragment();
        getActivity()
                .getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right , R.anim.exit_to_left)
                .replace(R.id.frame, mFragment).addToBackStack(null).commit();


    }


}
