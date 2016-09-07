package io.fruitful.dong.retrofitgson.adapter;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import io.fruitful.dong.retrofitgson.R;
import io.fruitful.dong.retrofitgson.Utils;

import io.fruitful.dong.retrofitgson.model.Result;

/**
 * Created by Ares on 8/23/2016.
 */
public class RecyclerLocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context mContext;
    private ArrayList<Result> resultList;
    private ClickListener clickListener;

    public RecyclerLocationAdapter(ArrayList<Result> resultList, Context mContext) {
        this.resultList = resultList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_card_retroft_localtion_layout, parent, false);
            final RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = recyclerViewHolder.getAdapterPosition();
                    clickListener.onItemClick(position, view);
                }
            });

            return recyclerViewHolder;
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_item_layout, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof RecyclerViewHolder) {

            RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
            viewHolder.mNameLocation.setText((position + 1) + "- " + resultList.get(position).getName());
            viewHolder.mDiaChi.setText(resultList.get(position).getVicinity());

            double distance = getDistanceFromLatLonInKm(Utils.TAG_MyDinh_Lat, Utils.TAG_MyDinh_Lng,
                    resultList.get(position).getGeometry().getLocation().getLat(),
                    resultList.get(position).getGeometry().getLocation().getLng());
//        Log.e("zzz",resultList.get(position).getGeometry().getLocation().getLat() + " - " + resultList.get(position).getGeometry().getLocation().getLng());
            viewHolder.mKhoangCach.setText(formatDistance(distance));



            if (resultList.get(position).getPhotos().size() == 0) {

                viewHolder.mIcon.setImageResource(R.drawable.thiennhien);

            } else {
                Picasso.with(mContext)
                        .load(Utils.TAG_PHOTO_API + resultList.get(position).getPhotos().get(0).getPhotoReference() + Utils.TAG_AND_KEY + Utils.TAG_API_KEY)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(viewHolder.mIcon);
            }


        } else if (holder instanceof LoadingViewHolder) {

            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return resultList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameLocation, mDiaChi, mKhoangCach;
        public ImageView mIcon;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mNameLocation = (TextView) itemView.findViewById(R.id.textViewName);
            mDiaChi = (TextView) itemView.findViewById(R.id.txt_vitri);
            mKhoangCach = (TextView) itemView.findViewById(R.id.txt_khangcach);
            mIcon = (ImageView) itemView.findViewById(R.id.imgv_location);

            mIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }


    /**
     * interface
     */

    public interface ClickListener {

        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0008; // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km
        return d;
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public String formatDistance(double distance) {
        String str;
        if (distance >= 1) {
            str = new DecimalFormat("#.#").format(distance) + " Km";
        } else {
            str = String.format("%.00f", distance * 1000) + " m";
        }
        return str;
    }


}
