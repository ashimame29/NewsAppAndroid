package com.example.newsapp_1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Headlines extends Fragment {

    private TabLayout tabLayout;
    private TabItem headlineWorld, headlineBusiness, headlinePolitics, headlineSports, headlineTechnology, headlineScience;
    private RequestQueue mRequestQueue;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ProgressBar activityIndicator;
    private TextView txt_secondsleft;
    private ArrayList<String> mImageUrl = new ArrayList<>();
    private ArrayList<String> mHeadings = new ArrayList<>();
    private ArrayList<String> mDates = new ArrayList<>();
    private ArrayList<String> mSection = new ArrayList<>();
    private ArrayList<String> marticle = new ArrayList<>();
    private ArrayList<String> murl = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View rootView;

    public Headlines() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_headlines, container, false);
        tabLayout = (TabLayout) rootView.findViewById(R.id.headlineTablayout);
        activityIndicator = (ProgressBar) rootView.findViewById(R.id.activityIndicatorHeadline);
        txt_secondsleft = rootView.findViewById(R.id.fetchnews);
        activityIndicator.setVisibility(View.VISIBLE);
        txt_secondsleft.setVisibility(View.VISIBLE);
        txt_secondsleft.setText("Fetching News");
        mImageUrl.clear();
        mHeadings.clear();
        mDates.clear();
        mSection.clear();
        marticle.clear();
        murl.clear();
        parseJsonWorld();
        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_headlines);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mImageUrl.clear();
                mHeadings.clear();
                mDates.clear();
                mSection.clear();
                marticle.clear();
                murl.clear();
                activityIndicator.setVisibility(View.VISIBLE);
                txt_secondsleft.setVisibility(View.VISIBLE);
                txt_secondsleft.setText("Fetching News");
                parseJsonWorld();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1500);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mImageUrl.clear();
                mHeadings.clear();
                mDates.clear();
                mSection.clear();
                marticle.clear();
                murl.clear();

                if(tab.getPosition()== 0){
                    mImageUrl.clear();
                    mHeadings.clear();
                    mDates.clear();
                    mSection.clear();
                    marticle.clear();
                    murl.clear();

                    recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                    adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection,marticle,murl);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    activityIndicator.setVisibility(View.VISIBLE);
                    txt_secondsleft.setVisibility(View.VISIBLE);
                    txt_secondsleft.setText("Fetching News");
                    parseJsonWorld();
                    mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_headlines);
                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            mImageUrl.clear();
                            mHeadings.clear();
                            mDates.clear();
                            mSection.clear();
                            marticle.clear();
                            murl.clear();
                            parseJsonWorld();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mSwipeRefreshLayout.isRefreshing()) {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }, 1500);
                        }
                    });
                }
                else if(tab.getPosition() == 1){
                    mImageUrl.clear();
                    mHeadings.clear();
                    mDates.clear();
                    mSection.clear();
                    marticle.clear();
                    murl.clear();

                    recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                    adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection,marticle,murl);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    activityIndicator.setVisibility(View.VISIBLE);
                    txt_secondsleft.setVisibility(View.VISIBLE);
                    txt_secondsleft.setText("Fetching News");
                    parseJsonBusiness();
                    mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_headlines);
                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            mImageUrl.clear();
                            mHeadings.clear();
                            mDates.clear();
                            mSection.clear();
                            marticle.clear();
                            murl.clear();

                            parseJsonBusiness();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(mSwipeRefreshLayout.isRefreshing()) {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }, 1500);
                        }
                    });
                }
                else if(tab.getPosition() == 2){
                    mImageUrl.clear();
                    mHeadings.clear();
                    mDates.clear();
                    mSection.clear();
                    marticle.clear();
                    murl.clear();

                    recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                    adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection,marticle,murl);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    activityIndicator.setVisibility(View.VISIBLE);
                    txt_secondsleft.setVisibility(View.VISIBLE);
                    txt_secondsleft.setText("Fetching News");
                    parseJsonPolitics();
                    mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_headlines);
                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            mImageUrl.clear();
                            mHeadings.clear();
                            mDates.clear();
                            mSection.clear();
                            marticle.clear();
                            murl.clear();
                            parseJsonPolitics();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(mSwipeRefreshLayout.isRefreshing()) {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }, 1500);
                        }
                    });
                }
                else if(tab.getPosition() == 3){
                    mImageUrl.clear();
                    mHeadings.clear();
                    mDates.clear();
                    mSection.clear();
                    marticle.clear();
                    murl.clear();

                    recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                    adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection,marticle,murl);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    activityIndicator.setVisibility(View.VISIBLE);
                    txt_secondsleft.setVisibility(View.VISIBLE);
                    txt_secondsleft.setText("Fetching News");
                    parseJsonSports();
                    mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_headlines);
                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            mImageUrl.clear();
                            mHeadings.clear();
                            mDates.clear();
                            mSection.clear();
                            marticle.clear();
                            murl.clear();
                            parseJsonSports();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(mSwipeRefreshLayout.isRefreshing()) {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }, 1500);
                        }
                    });
                }
                else if(tab.getPosition() == 4){
                    mImageUrl.clear();
                    mHeadings.clear();
                    mDates.clear();
                    mSection.clear();
                    marticle.clear();
                    murl.clear();

                    recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                    adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection,marticle,murl);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    activityIndicator.setVisibility(View.VISIBLE);
                    txt_secondsleft.setVisibility(View.VISIBLE);
                    txt_secondsleft.setText("Fetching News");
                    parseJsonTechnology();
                    mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_headlines);
                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            mImageUrl.clear();
                            mHeadings.clear();
                            mDates.clear();
                            mSection.clear();
                            marticle.clear();
                            murl.clear();
                            parseJsonTechnology();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(mSwipeRefreshLayout.isRefreshing()) {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }, 1500);
                        }
                    });
                }
                else if(tab.getPosition() == 5){
                    mImageUrl.clear();
                    mHeadings.clear();
                    mDates.clear();
                    mSection.clear();
                    marticle.clear();
                    murl.clear();

                    recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                    adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection,marticle,murl);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    activityIndicator.setVisibility(View.VISIBLE);
                    txt_secondsleft.setVisibility(View.VISIBLE);
                    txt_secondsleft.setText("Fetching News");
                    parseJsonScience();
                    mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_headlines);
                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            mImageUrl.clear();
                            mHeadings.clear();
                            mDates.clear();
                            mSection.clear();
                            marticle.clear();
                            murl.clear();
                            parseJsonScience();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(mSwipeRefreshLayout.isRefreshing()) {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }, 1500);
                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return rootView;
    }

    private void parseJsonScience() {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        //Log.d("Hello", "hello");
        String url = "https://newsappserver96.wl.r.appspot.com/guardiansection?section=science";
        //Log.d("afterurl", "url");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.d("aftertry", "aftertry");
                            JSONArray jsonArray= response.getJSONObject("response").getJSONArray("results");
                            //Log.d("DATAFRAG",String.valueOf(jsonArray));
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                String id = result.getString("id");
                                String title = result.getString("webTitle");
                                String date = result.getString("webPublicationDate");
                                String url = result.getString("webUrl");
                                String img;

                                try {
                                    img = result.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements").getJSONObject(0).getJSONArray("assets").getJSONObject(0).getString("file");
                                            //.getJSONArray("assets").getString("thumbnail");
                                }
                                catch (JSONException e) {
                                    img = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                }

                            String section = result.getString("sectionName");

                                mImageUrl.add(img);
                                mHeadings.add(title);
                                mDates.add(date);
                                mSection.add(section);
                                marticle.add(id);
                                murl.add(url);
                            }
                            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                            adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection,marticle,murl);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            activityIndicator.setVisibility(View.GONE);
                            txt_secondsleft.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(jsonRequest);
    }

    private void parseJsonTechnology() {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        //Log.d("Hello", "hello");
        String url = "https://newsappserver96.wl.r.appspot.com/guardiansection?section=technology";
        //Log.d("afterurl", "url");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.d("aftertry", "aftertry");
                            JSONArray jsonArray= response.getJSONObject("response").getJSONArray("results");
                            //Log.d("DATAFRAG",String.valueOf(jsonArray));
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                String id = result.getString("id");
                                String title = result.getString("webTitle");
                                String date = result.getString("webPublicationDate");
                                String url = result.getString("webUrl");
                                String img;

                                try {
                                    img = result.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements").getJSONObject(0).getJSONArray("assets").getJSONObject(0).getString("file");
                                    //.getJSONArray("assets").getString("thumbnail");
                                }
                                catch (JSONException e) {
                                    img = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                }

                                String section = result.getString("sectionName");

                                mImageUrl.add(img);
                                mHeadings.add(title);
                                mDates.add(date);
                                mSection.add(section);
                                marticle.add(id);
                                murl.add(url);
                            }
                            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                            adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection,marticle, murl);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            activityIndicator.setVisibility(View.GONE);
                            txt_secondsleft.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(jsonRequest);
    }

    private void parseJsonSports() {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        //Log.d("Hello", "hello");
        String url = "https://newsappserver96.wl.r.appspot.com/guardiansection?section=sport";
        //Log.d("afterurl", "url");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.d("aftertry", "aftertry");
                            JSONArray jsonArray= response.getJSONObject("response").getJSONArray("results");
                            //Log.d("DATAFRAG",String.valueOf(jsonArray));
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                String id = result.getString("id");
                                String title = result.getString("webTitle");
                                String date = result.getString("webPublicationDate");
                                String url = result.getString("webUrl");
                                String img;

                                try {
                                    img = result.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements").getJSONObject(0).getJSONArray("assets").getJSONObject(0).getString("file");
                                    //.getJSONArray("assets").getString("thumbnail");
                                }
                                catch (JSONException e) {
                                    img = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                }

                                String section = result.getString("sectionName");

                                mImageUrl.add(img);
                                mHeadings.add(title);
                                mDates.add(date);
                                mSection.add(section);
                                marticle.add(id);
                                murl.add(url);
                            }
                            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                            adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection, marticle, murl);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            activityIndicator.setVisibility(View.GONE);
                            txt_secondsleft.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(jsonRequest);
    }

    private void parseJsonPolitics() {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        //Log.d("Hello", "hello");
        String url = "https://newsappserver96.wl.r.appspot.com/guardiansection?section=politics";
        //Log.d("afterurl", "url");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.d("aftertry", "aftertry");
                            JSONArray jsonArray= response.getJSONObject("response").getJSONArray("results");
                            //Log.d("DATAFRAG",String.valueOf(jsonArray));
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                String id = result.getString("id");
                                String title = result.getString("webTitle");
                                String date = result.getString("webPublicationDate");
                                String url = result.getString("webUrl");
                                String img;

                                try {
                                    img = result.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements").getJSONObject(0).getJSONArray("assets").getJSONObject(0).getString("file");
                                    //.getJSONArray("assets").getString("thumbnail");
                                }
                                catch (JSONException e) {
                                    img = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                }

                                String section = result.getString("sectionName");

                                mImageUrl.add(img);
                                mHeadings.add(title);
                                mDates.add(date);
                                mSection.add(section);
                                marticle.add(id);
                                murl.add(url);
                            }
                            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                            adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection, marticle, murl);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            activityIndicator.setVisibility(View.GONE);
                            txt_secondsleft.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(jsonRequest);
    }

    private void parseJsonBusiness() {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        //Log.d("Hello", "hello");
        String url = "https://newsappserver96.wl.r.appspot.com/guardiansection?section=business";
        //Log.d("afterurl", "url");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.d("aftertry", "aftertry");
                            JSONArray jsonArray= response.getJSONObject("response").getJSONArray("results");
                            //Log.d("DATAFRAG",String.valueOf(jsonArray));
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                String id = result.getString("id");
                                String title = result.getString("webTitle");
                                String date = result.getString("webPublicationDate");
                                String url = result.getString("webUrl");
                                String img;

                                try {
                                    img = result.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements").getJSONObject(0).getJSONArray("assets").getJSONObject(0).getString("file");
                                    //.getJSONArray("assets").getString("thumbnail");
                                }
                                catch (JSONException e) {
                                    img = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                }

                                String section = result.getString("sectionName");

                                mImageUrl.add(img);
                                mHeadings.add(title);
                                mDates.add(date);
                                mSection.add(section);
                                marticle.add(id);
                                murl.add(url);
                            }
                            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                            adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection,marticle, murl);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            activityIndicator.setVisibility(View.GONE);
                            txt_secondsleft.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(jsonRequest);
    }

    private void parseJsonWorld() {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        //Log.d("Hello", "hello");
        String url = "https://newsappserver96.wl.r.appspot.com/guardiansection?section=world";
        //Log.d("afterurl", "url");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.d("aftertry", "aftertry");
                            JSONArray jsonArray= response.getJSONObject("response").getJSONArray("results");
                            //Log.d("DATAFRAG",String.valueOf(jsonArray));
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                String id = result.getString("id");
                                String title = result.getString("webTitle");
                                String date = result.getString("webPublicationDate");
                                String url = result.getString("webUrl");
                                String img;

                                try {
                                    img = result.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements").getJSONObject(0).getJSONArray("assets").getJSONObject(0).getString("file");
                                    //.getJSONArray("assets").getString("thumbnail");
                                }
                                catch (JSONException e) {
                                    img = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                }

                                String section = result.getString("sectionName");

                                mImageUrl.add(img);
                                mHeadings.add(title);
                                mDates.add(date);
                                mSection.add(section);
                                marticle.add(id);
                                murl.add(url);
                            }
                            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHeadlines);
                            adapter = new MyAdapter(getActivity(), mImageUrl, mHeadings, mDates, mSection,marticle, murl);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            activityIndicator.setVisibility(View.GONE);
                            txt_secondsleft.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(jsonRequest);
    }
    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }
}
