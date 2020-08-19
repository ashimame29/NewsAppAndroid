package com.example.newsapp_1;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {

    private RequestQueue mRequestQueue;
    private LineChart mLinechart;
    private View rootView;
    private EditText editText;
    private EditText keyword;
    private ProgressBar activityIndicator;
    private TextView txt_secondsleft;
    private List<Entry> dataVals = new ArrayList<>();
    private String find;
    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_trending, container, false);
        editText = rootView.findViewById(R.id.editText);
        editText.setText("");
        mLinechart = rootView.findViewById(R.id.line_chart);
//        activityIndicator = (ProgressBar) rootView.findViewById(R.id.activityIndicatorTrending);
//        txt_secondsleft = rootView.findViewById(R.id.fetchTrends);
//        activityIndicator.setVisibility(View.VISIBLE);
//        txt_secondsleft.setVisibility(View.VISIBLE);
//        txt_secondsleft.setText("Fetching News");
        editText.setHint("Coronavirus");

        parseJSON("Coronavirus");
        keyword = (EditText) rootView.findViewById(R.id.editText);
        keyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        find = keyword.getText().toString();
                        parseJSON(find);
                        return true;
                    }
                    return false;
                }
            });
        return rootView;
    }

    private void parseJSON(final String key) {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        dataVals.clear();

        String url = "https://newsappclient96.wl.r.appspot.com/trendingapi?keyword=" + key;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONObject("default").getJSONArray("timelineData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                String val = result.getJSONArray("value").getString(0);
                                int finalVal = Integer.parseInt(val);
                                //Log.d("Value", String.valueOf(val));
                                dataVals.add(new Entry(i,finalVal));
                            }
                            Log.d("DATAVAL", String.valueOf(dataVals));
                            LineDataSet lineDataSet = new LineDataSet(dataVals, "Trending Chart for "+ key);
                            List<ILineDataSet> dataSets = new ArrayList<>();

                            lineDataSet.setCircleColor(getActivity().getColor(R.color.colorPrimary));
                            lineDataSet.setColor(getActivity().getColor(R.color.colorPrimary));
                            lineDataSet.setCircleHoleColor(getActivity().getColor(R.color.colorPrimary));

                            dataSets.add(lineDataSet);
                            Legend legend = mLinechart.getLegend();
                            legend.setTextColor(Color.BLACK);
                            legend.setFormSize(17);
                            legend.setTextSize(17);
                            LineData data = new LineData(dataSets);
                            mLinechart.setData(data);
                            mLinechart.getAxisLeft().setDrawGridLines(false);
                            mLinechart.getAxisRight().setDrawGridLines(false);
                            mLinechart.getXAxis().setDrawGridLines(false);
                            mLinechart.getAxisLeft().setDrawAxisLine(false);
                            mLinechart.invalidate();
//                            activityIndicator.setVisibility(View.GONE);
//                            txt_secondsleft.setVisibility(View.GONE);


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
        //Log.d("Linechart", "corona");
        mRequestQueue.add(jsonRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
        editText = rootView.findViewById(R.id.editText);
        editText.setText("");
        editText.setHint("CoronaVirus");
    }
}
