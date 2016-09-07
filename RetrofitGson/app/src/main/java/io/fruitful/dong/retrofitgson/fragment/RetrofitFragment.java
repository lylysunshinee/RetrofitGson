package io.fruitful.dong.retrofitgson.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.fruitful.dong.retrofitgson.MainActivity;
import io.fruitful.dong.retrofitgson.R;
import io.fruitful.dong.retrofitgson.rest.RestAPI;
import io.fruitful.dong.retrofitgson.Utils;
import io.fruitful.dong.retrofitgson.adapter.RecyclerLocationAdapter;
import io.fruitful.dong.retrofitgson.model.Place;
import io.fruitful.dong.retrofitgson.model.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ares on 8/22/2016.
 */
public class RetrofitFragment extends Fragment {

    private ArrayList<Result> mResultList;
    private RecyclerView mRecyclerView;
    private RecyclerLocationAdapter recyclerLocationAdapter;
    private LinearLayoutManager layoutManager;
    private ProgressDialog mDialog;
    private int totalItemCount, lastVisibleItem;
    private boolean isLoading;
    private String nextPageToken = null;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabReset;
    private TextView tvError;
    private String type = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_getlocation_layout, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressDialog);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        fabReset = (FloatingActionButton) view.findViewById(R.id.fabReset);
        tvError = (TextView) view.findViewById(R.id.tvError);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        fabReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        setHasOptionsMenu(true);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_local);


        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        /**
         * Load more Data when last Scroll
         *
         * **/

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//                    Log.e("zzz", totalItemCount + " - " + lastVisibleItem);
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 5)) {
                        Log.e("zzz", "loading...");
                        if (nextPageToken != null) {
                            loadMoreData();
                        }
                    }

                }
            }
        });



        /**
         * check Data click back
         * */

        if (mResultList == null) {
            getData();
        } else {
            showData();
        }


        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.atm:
                type = "atm";
                getData();
                break;
            case R.id.food:
                type = "food";
                getData();
                break;
            case R.id.cafe:
                type = "cafe";
                getData();
                break;
            case R.id.bar:
                type = "bar";
                getData();
                break;
            case R.id.hotel:
                type = "hotel";
                getData();
                break;

        }
        return super.onOptionsItemSelected(item);
    }




    private void loadMoreData() {

        mResultList.add(null);
        recyclerLocationAdapter.notifyItemInserted(mResultList.size() - 1);
        isLoading = true;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.TAG_BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestAPI service = retrofit.create(RestAPI.class);

        Call<Place> call = service.getNextResultCall(nextPageToken, Utils.TAG_API_KEY);

        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {

                if (response.body().getStatus().equals("OK")) {
                    nextPageToken = response.body().getNextPageToken();
                    isLoading = false;

                    mResultList.remove(mResultList.size() - 1);
                    mResultList.addAll(response.body().getResults());
                    recyclerLocationAdapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Log.e("hehe2", t.getMessage());
                isLoading = false;
                if (mDialog.isShowing())
                    mDialog.dismiss();
            }
        });
    }

    private void getData() {

        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Loading ...");
        mDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.TAG_BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestAPI service = retrofit.create(RestAPI.class);

        Call<Place> call = service.getResultCall(Utils.TAG_MyDinh_Lat + "," + Utils.TAG_MyDinh_Lng, "distance", type, Utils.TAG_API_KEY);

        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                try {
                    fabReset.setVisibility(View.GONE);
                    tvError.setVisibility(View.GONE);
                    Log.e("hihi", response.body().getResults().size() + "");
                    nextPageToken = response.body().getNextPageToken();
                    if (mDialog.isShowing())
                        mDialog.dismiss();
                    mResultList = response.body().getResults();

                    showData();

                } catch (Exception e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                fabReset.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.VISIBLE);
                Log.e("hehe2", t.getMessage());
//                Toast.makeText(getContext(), "Không có kết nối internet", Toast.LENGTH_LONG).show();
                if (mDialog.isShowing())
                    mDialog.dismiss();
            }
        });
    }

    private void showData() {
        recyclerLocationAdapter = new RecyclerLocationAdapter(mResultList, getContext());
        mRecyclerView.setAdapter(recyclerLocationAdapter);

        recyclerLocationAdapter.setOnItemClickListener(new RecyclerLocationAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {


                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Utils.TAG_DATA, mResultList);
                bundle.putInt(Utils.TAG_POSITON, position);
                Log.e("positon", position + "");


                // RePalce fragment
                Fragment mFragment = new DetailLocationPager();
                mFragment.setArguments(bundle);
                getActivity()
                        .getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.frame, mFragment).addToBackStack(null).commit();


            }
        });
    }


}
