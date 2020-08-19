package com.example.newsapp_1;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private View rootView;
    private ProgressBar activityIndicator;
    private TextView txt_secondsleft;
    private TextView weatherCity;
    private TextView weatherTemp;
    private TextView weatherState;
    private TextView weatherSummary;
    private RelativeLayout weatherImage;
    private ArrayList<String> mImageUrl = new ArrayList<>();
    private ArrayList<String> mHeadings = new ArrayList<>();
    private ArrayList<String> mDates = new ArrayList<>();
    private ArrayList<String> mSection = new ArrayList<>();
    private ArrayList<String> marticle = new ArrayList<>();
    private ArrayList<String> murl = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private RequestQueue locationRequest;
    private LocationManager locationManager;
    private String provider;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String city;
    private String state;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //ProgressDialog progressDialog = new ProgressDialog(this);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
//        weatherCity = rootView.findViewById(R.id.weatherCity);
//        weatherImage = rootView.findViewById(R.id.weatherImage);
//        weatherState = rootView.findViewById(R.id.weatherState);
//        weatherSummary = rootView.findViewById(R.id.weatherSummary);
//        weatherTemp = rootView.findViewById(R.id.weatherTemp);
        checkLocationPermission();
        activityIndicator = (ProgressBar) rootView.findViewById(R.id.activityIndicator);
        txt_secondsleft = rootView.findViewById(R.id.txt_secondsleft);
        activityIndicator.setVisibility(View.VISIBLE);
        txt_secondsleft.setVisibility(View.VISIBLE);
        txt_secondsleft.setText("Fetching News");
        mImageUrl.clear();
        mHeadings.clear();
        mDates.clear();
        mSection.clear();
        marticle.clear();
        murl.clear();
        parseJSON();
        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_items);
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
                parseJSON();
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
        return rootView;
    }

    private void parseJSON(){
        mRequestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://newsappclient96.wl.r.appspot.com/guardianhomecall";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.d("aftertry", "aftertry");
                            JSONArray jsonArray= response.getJSONObject("response").getJSONArray("results");
                            Log.d("DATAFRAG",String.valueOf(jsonArray));
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                String id = result.getString("id");
                                String title = result.getString("webTitle");
                                String date = result.getString("webPublicationDate");
                                String url = result.getString("webUrl");
                                String img;
                                try {
                                    img = result.getJSONObject("fields").getString("thumbnail");
                                }
                                catch (JSONException e){
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

                            Log.d("HOMEFRAG", String.valueOf(mImageUrl));

                            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHome);
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
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 400, 1,this);
        }
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }
    @Override
    public void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Log.d("MYNEWSAPP", String.valueOf(location.getLongitude()));
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            city = addresses.get(0).getLocality();
            //Log.d(TAG, "onLocationChanged: " + city);
            state = addresses.get(0).getAdminArea();
            //Log.d(TAG, "onLocationChanged: " + state);
            weatherAPICall(city, state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void weatherAPICall(final String city, final String state) {
        locationRequest = Volley.newRequestQueue(getActivity());
        weatherCity = rootView.findViewById(R.id.weatherCity);
        weatherImage = rootView.findViewById(R.id.weatherImage);
        weatherState = rootView.findViewById(R.id.weatherState);
        weatherSummary = rootView.findViewById(R.id.weatherSummary);
        weatherTemp = rootView.findViewById(R.id.weatherTemp);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city + "&units=metric&appid=b0315a497be60be468436dd9ec766621";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            weatherCity.setText(city);
                            weatherState.setText(state);
                            String tempString = response.getJSONObject("main").getString("temp");
                            Float tempFloat = Float.parseFloat(tempString);
                            int tempInteger = Math.round(tempFloat);
                            String temp = String.valueOf(tempInteger) + "°C";
                            weatherTemp.setText(temp);
                            String summary = response.getJSONArray("weather").getJSONObject(0).getString("main");
                            weatherSummary.setText(summary);
                            if(summary.equals("Clear")){
                                weatherImage.setBackgroundResource(R.drawable.clear_weather);
                            }
                            else if(summary.equals("Clouds")){
                                weatherImage.setBackgroundResource(R.drawable.cloudy_weather);
                            }
                            else if(summary.equals("Snow")){
                                weatherImage.setBackgroundResource(R.drawable.snowy_weather);
                            }
                            else if(summary.equals("Rain/Drizzle")){
                                weatherImage.setBackgroundResource(R.drawable.rainy_weather);
                            }
                            else if(summary.equals("Thunderstorm")){
                                weatherImage.setBackgroundResource(R.drawable.thunder_weather);
                            }
                            else{
                                weatherImage.setBackgroundResource(R.drawable.sunny_weather);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        locationRequest.add(jsonRequest);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("R.string.title_location_permission")
                        .setMessage("R.string.text_location_permission")
                        .setPositiveButton("R.string.ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

}
