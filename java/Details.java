package com.example.newsapp_1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Details extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private TextView title;
    private TextView section;
    private TextView date;
    private TextView details;
    private TextView URL;
    private TextView detailbar;
    ImageView bookmarkIcon;
    private ImageView image;
    private ProgressBar activityIndicator;
    private TextView txt_secondsleft;
    ImageView twitterIcon;
    private String link = "";
    Toolbar detailedToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i = getIntent();
        final String articleID = i.getStringExtra("articleIdt");
        detailedToolbar = (Toolbar) findViewById(R.id.detailed_bar);
        image = (ImageView) findViewById(R.id.detailedImg);
        date = (TextView) findViewById(R.id.detailedDate);
        section = (TextView) findViewById(R.id.detailedSection);
        bookmarkIcon =(ImageView) findViewById(R.id.bookmark_icon_details);
        title = (TextView) findViewById(R.id.detailedTitle);
        details = (TextView) findViewById(R.id.detailedContent);

        URL = (TextView) findViewById(R.id.detailedLink) ;
        URL.setMovementMethod(LinkMovementMethod.getInstance());

        twitterIcon = findViewById(R.id.twitter_icon);
        activityIndicator = (ProgressBar) findViewById(R.id.activityIndicatorDetails);
        txt_secondsleft = findViewById(R.id.fetchDetails);
        activityIndicator.setVisibility(View.VISIBLE);
        txt_secondsleft.setVisibility(View.VISIBLE);
        txt_secondsleft.setText("Fetching News");
        mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://newsappserver96.wl.r.appspot.com/guardiandetailed?id=" + articleID;
        Log.d("afterurl", String.valueOf(url));
        SharedPreferences pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        if(pref.contains(articleID) == true){
            bookmarkIcon.setImageResource(R.drawable.ic_bookmark_black_24dp);
        }
        else{
            bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
        }
        final String[] heading = new String[1];
        final String[] dateNews = new String[1];
        final String[] sectionNews = new String[1];
        final String[] img = new String[1];
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            heading[0] = response.getJSONObject("response").getJSONObject("content").getString("webTitle");
                            detailedToolbar.setTitle(heading[0]);
                            title.setText(heading[0]);
                            dateNews[0] = response.getJSONObject("response").getJSONObject("content").getString("webPublicationDate");
                            Instant instant = Instant.parse(dateNews[0]);
                            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("America/Los_Angeles"));
                            date.setText(DateTimeFormatter.ofPattern("dd MMM yyyy").format(zonedDateTime));
//                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
//                            String dateString = sdf.format(zonedDateTime);
//                            date.setText(dateString);
                            sectionNews[0] = response.getJSONObject("response").getJSONObject("content").getString("sectionName");
                            section.setText(sectionNews[0]);
                            link = response.getJSONObject("response").getJSONObject("content").getString("webUrl");
                            Log.d("Links", String.valueOf(link));
                            URL.setText(Html.fromHtml("<a href='"+link+"'> View Full Article </a>"));
                            URL.setMovementMethod(LinkMovementMethod.getInstance());
                            String finalDesc = "";
                            JSONArray desc = response.getJSONObject("response").getJSONObject("content").getJSONObject("blocks").getJSONArray("body");
                            for (int i = 0; i <desc.length() ; i++) {
                                JSONObject result = desc.getJSONObject(i);
                                finalDesc += result.getString("bodyHtml");
                            }
                            details.setText(Html.fromHtml(finalDesc));
                            Log.d("Hello", "hello");

                            try {
                                img[0] = response.getJSONObject("response").getJSONObject("content").getJSONObject("blocks").getJSONObject("main").getJSONArray("elements").getJSONObject(0).getJSONArray("assets").getJSONObject(0).getString("file");

                            }
                            catch (JSONException e) {
                                img[0] = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                            }
                            Log.d("Hello", String.valueOf(img[0]));
                            Picasso.with(Details.this).load(img[0]).into(image);
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

        detailedToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        detailedToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        twitterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetUrl = "https://twitter.com/intent/tweet?text=Check+out+this+Link: \n" + link + "&hashtags=CSCI571NewsSearch" ;
                Uri uri = Uri.parse(tweetUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        bookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                if(pref.contains(articleID) == true){
                    //fav.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    editor.remove(articleID);
                    editor.commit();
                    //Log.d("Bookmarks", String.valueOf(pref));
                    CharSequence text = "'"+ heading[0] + "' was removed from Bookmarks";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
                else{
                    //fav.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    bookmarkIcon.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    Gson gson = new Gson();
                    ArrayList<String> articleData = new ArrayList<>();
                    articleData.add(articleID);
                    articleData.add(heading[0]);
                    articleData.add(img[0]);
                    articleData.add(dateNews[0]);
                    articleData.add(sectionNews[0]);
                    articleData.add(link);
                    String json = gson.toJson(articleData);
                    editor.putString(articleID, json);
                    CharSequence text = "'" + heading[0] + "' was added to Bookmarks";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                    editor.commit();
                }
            }
        });

    }

}
