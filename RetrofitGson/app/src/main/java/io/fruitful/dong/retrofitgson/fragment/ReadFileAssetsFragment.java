package io.fruitful.dong.retrofitgson.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.fruitful.dong.retrofitgson.MainActivity;
import io.fruitful.dong.retrofitgson.R;
import io.fruitful.dong.retrofitgson.adapter.RecyclerAdapter;
import io.fruitful.dong.retrofitgson.model.User;

/**
 * Created by Ares on 8/22/2016.
 */
public class ReadFileAssetsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private List<User> itemList = new ArrayList<>();
    private Context mContext;
    private LinearLayoutManager layoutManager;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_readassets_layout, null, false);
        mContext = getActivity().getApplicationContext();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        LoadJsonData loadJsonData = new LoadJsonData();
        loadJsonData.execute("reviewjson.txt");
        return view;
    }

    private void showListUsers(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("Message");
            for (int i = 0; i < jsonArray.length(); i++) {

                String obj = jsonArray.getString(i);
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                User contact = gson.fromJson(obj, User.class);
                itemList.add(contact);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerAdapter = new RecyclerAdapter(mContext, itemList);
        mRecyclerView.setAdapter(mRecyclerAdapter);

    }

    private String onReadData(String fileName) {

        BufferedReader reader = null;
        StringBuffer strBuffer = new StringBuffer();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getActivity().getAssets().open(fileName)));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                strBuffer.append(mLine + "\n");
                //process line
            }
        } catch (IOException e) {
            //log the exception
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return strBuffer.toString();
    }

    class LoadJsonData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... fileNames) {

            return onReadData(fileNames[0]);
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
            showListUsers(jsonData);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getContext());
//            progressDialog.setMessage("Loading...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
        }
    }


}
