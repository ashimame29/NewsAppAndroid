package com.example.newsapp_1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BookmarksFragment extends Fragment {

    private View rootView;
    private TextView txtView;
    private LinearLayout layout_to_hide;
    //private RecyclerView recyclerViewBookmarks;
    private RecyclerView.Adapter adapter;
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mHeadings = new ArrayList<>();
    private ArrayList<String> mDates = new ArrayList<>();
    private ArrayList<String> mSection = new ArrayList<>();
    private ArrayList<String> marticle = new ArrayList<>();
    private ArrayList<String> murl = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar activityIndicator;
    private TextView txt_secondsleft;
    private RequestQueue mRequestQueue;
    public BookmarksFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_bookmarks, container, false);
        activityIndicator = (ProgressBar) rootView.findViewById(R.id.activityIndicatorBookmarks);
        txt_secondsleft = rootView.findViewById(R.id.fetchFav);
        activityIndicator.setVisibility(View.VISIBLE);
        txt_secondsleft.setVisibility(View.VISIBLE);
        txt_secondsleft.setText("Fetching News");
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0);

        Gson gson = new Gson();
        Map<String, ?> keys = pref.getAll();
        Log.d("shared pref", String.valueOf(keys));
        txtView = rootView.findViewById(R.id.toggleText);
        marticle.clear();
        mHeadings.clear();
        mImages.clear();
        mDates.clear();
        mSection.clear();
        murl.clear();
        //pref.edit().clear().commit();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                String key = pref.getString(entry.getKey(), "");
                List<String> arrPackageData = gson.fromJson(key, type);
                Log.d("Article", String.valueOf(arrPackageData.get(5)));
                marticle.add(arrPackageData.get(0));
                mHeadings.add(arrPackageData.get(1));
                mImages.add(arrPackageData.get(2));
                mDates.add(arrPackageData.get(3));
                mSection.add(arrPackageData.get(4));
                murl.add(arrPackageData.get(5));
            }
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewBookmarks);
            if(marticle.size() == 0){
                txtView.setText("No Bookmarked Articles");
                activityIndicator.setVisibility(View.GONE);
                txt_secondsleft.setVisibility(View.GONE);
            }
            else {
                adapter = new BookmarkAdapter(getActivity(), mImages, mHeadings, mDates, mSection, marticle, murl);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                txtView.setText("");
                activityIndicator.setVisibility(View.GONE);
                txt_secondsleft.setVisibility(View.GONE);
            }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0);
        Gson gson = new Gson();
        Map<String, ?> keys = pref.getAll();
        Log.d("shared pref", String.valueOf(keys));
        marticle.clear();
        mHeadings.clear();
        mImages.clear();
        mDates.clear();
        mSection.clear();
        murl.clear();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                String key = pref.getString(entry.getKey(), "");
                List<String> arrPackageData = gson.fromJson(key, type);
                Log.d("Article", String.valueOf(arrPackageData.get(1)));
                marticle.add(arrPackageData.get(0));
                mHeadings.add(arrPackageData.get(1));
                mImages.add(arrPackageData.get(2));
                mDates.add(arrPackageData.get(3));
                mSection.add(arrPackageData.get(4));
                murl.add(arrPackageData.get(5));
            }
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewBookmarks);
            if(marticle.size() == 0){
                txtView.setText("No Bookmarked Articles");
                activityIndicator.setVisibility(View.GONE);
                txt_secondsleft.setVisibility(View.GONE);
            }
            else {
                adapter = new BookmarkAdapter(getActivity(), mImages, mHeadings, mDates, mSection, marticle, murl);
                recyclerView.setAdapter(adapter);
                txtView.setText("");
                activityIndicator.setVisibility(View.GONE);
                txt_secondsleft.setVisibility(View.GONE);
            }
            if(adapter!= null)
                adapter.notifyDataSetChanged();
    }
}
