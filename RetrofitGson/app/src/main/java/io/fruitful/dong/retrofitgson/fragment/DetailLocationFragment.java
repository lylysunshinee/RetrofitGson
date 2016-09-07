package io.fruitful.dong.retrofitgson.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.fruitful.dong.retrofitgson.R;
import io.fruitful.dong.retrofitgson.Utils;
import io.fruitful.dong.retrofitgson.model.Result;

/**
 * Created by Ares on 8/25/2016.
 */
public class DetailLocationFragment extends Fragment {
    private ImageView mAvataDetail;
    private TextView mNameLocaitonDetail, mLocationDetail;
    private RatingBar mRatingBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_location_layout, container, false);
        mAvataDetail = (ImageView) view.findViewById(R.id.imgv_location_detail);
        mNameLocaitonDetail = (TextView) view.findViewById(R.id.txt_NameLocation_detail);
        mLocationDetail = (TextView) view.findViewById(R.id.txt_Location_detai);
        mRatingBar = (RatingBar) view.findViewById(R.id.myRatingBar);

        Bundle bundle = getArguments();
        Result bundleParcelable = bundle.getParcelable(Utils.TAG_LOCATION_DATA);

        if (bundleParcelable != null) {

            mNameLocaitonDetail.setText(bundleParcelable.getName());
            mLocationDetail.setText(bundleParcelable.getVicinity());

            //check null rating

            if (bundleParcelable.getRating() != null) {

                mRatingBar.setRating(bundleParcelable.getRating().floatValue());
            } else {
                mRatingBar.setRating(3);
            }


            // check null photo

            if (bundleParcelable.getPhotos().size() == 0) {

                mAvataDetail.setImageResource(R.drawable.thiennhien);

            } else {

                Picasso.with(getContext())
                        .load(Utils.TAG_PHOTO_API + bundleParcelable.getPhotos().get(0).getPhotoReference() + Utils.TAG_AND_KEY + Utils.TAG_API_KEY)

                        .placeholder(R.drawable.thiennhien)
                        .error(R.drawable.thiennhien)
                        .into(mAvataDetail);

            }

        }


        return view;
    }
}
