package com.example.newsapp_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private View rootView;

    private ArrayList<String> mImageUrl = new ArrayList<>();
    private ArrayList<String> mHeadings = new ArrayList<>();
    private ArrayList<String> mDates = new ArrayList<>();
    private ArrayList<String> mSection = new ArrayList<>();
    private ArrayList<String> marticle = new ArrayList<>();
    private ArrayList<String> murl = new ArrayList<>();
    private RequestQueue mRequestQueue;
    Context mcontext;
    private ProgressBar activityIndicator;
    private TextView txt_secondsleft;
    Toolbar searchToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        mcontext = this;
        searchToolbar = (Toolbar) findViewById(R.id.search_bar);
        Intent i = getIntent();
        final String query = i.getStringExtra("query");
        activityIndicator = (ProgressBar) findViewById(R.id.activityIndicatorSearch);
        txt_secondsleft = findViewById(R.id.fetchSearch);
        activityIndicator.setVisibility(View.VISIBLE);
        txt_secondsleft.setVisibility(View.VISIBLE);
        txt_secondsleft.setText("Fetching News");
        doMySearch(query);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_search);
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
                doMySearch(query);
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
        //handleIntent(getIntent());

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Intent i = getIntent();
        final String query = i.getStringExtra("query");
        doMySearch(query);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_search);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doMySearch(query);
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
        //handleIntent(intent);
    }
//    private void handleIntent(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            doMySearch(query);
//            //use the query to search your data somehow
//        }
//    }

    private void doMySearch(String query) {
        mRequestQueue = Volley.newRequestQueue(this);
        //Log.d("Hello", "hello");
        String url = "https://newsappserver96.wl.r.appspot.com/guardiansearch?q=" + query;
        searchToolbar.setTitle("Search Results for " + query);
        //Log.d("Search URL", String.valueOf(url));
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
                                String url = result.getString("webUrl");
                                String date = result.getString("webPublicationDate");
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
                            recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSearch);
                            adapter = new MyAdapter(mcontext, mImageUrl, mHeadings, mDates, mSection,marticle, murl);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
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

        searchToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        searchToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }
}
