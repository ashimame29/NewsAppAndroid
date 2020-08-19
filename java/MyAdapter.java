package com.example.newsapp_1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mHeadings = new ArrayList<>();
    private ArrayList<String> mDates = new ArrayList<>();
    private ArrayList<String> mSections = new ArrayList<>();
    private ArrayList<String> marticle = new ArrayList<>();
    private ArrayList<String> murl = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Context mcontext;

    public MyAdapter(Context context, ArrayList<String> images, ArrayList<String> headings, ArrayList<String> dates, ArrayList<String> section, ArrayList<String> id, ArrayList<String> url){
        mImages = images;
        mHeadings = headings;
        mDates = dates;
        mSections = section;
        mcontext= context;
        marticle = id;
        murl = url;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new ViewHolder(v);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)

    private String getTime(String webTime) {

        ZonedDateTime publishTime = Instant.parse(webTime).atZone(ZoneId.of("America/Los_Angeles"));
        //Log.d(TAG, "getTime: LA publish time: "+ publishTime);
        ZonedDateTime currentTime = LocalDateTime.now().atZone( ZoneId.of( "America/Los_Angeles" ) );
        //Log.d(TAG, "getTime: LA current time: "+ currentTime);
        //Log.d(TAG, "getTime: Time diff: " + Duration.between( publishTime , currentTime ).toMillis() / 1000);

        long secondsDiff = Duration.between( publishTime , currentTime ).toMillis() / 1000;
        long minutesDiff = secondsDiff / 60;
        long hoursDiff = minutesDiff / 60;
        if(hoursDiff > 0){
            return(hoursDiff + "h ago");
        }
        else if (minutesDiff > 0) {
            return(minutesDiff + "m ago");
        }
        else {
            return(secondsDiff + "s ago");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.with(mcontext).load(mImages.get(position)).into(holder.homeImage);
        holder.homeHeading.setText(mHeadings.get(position));
        String finalDate = getTime(mDates.get(position));
        holder.homeDate.setText(finalDate);
        holder.homeSection.setText(mSections.get(position));
        SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        boolean valBook = pref.contains(marticle.get(position));
        if(valBook == true){
            holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_black_24dp);
        }
        else{
            holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return mHeadings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView homeImage;
        TextView homeHeading;
        TextView homeDate;
        TextView homeSection;
        ImageView bookmarkIcon;
        ImageView twitter;
        ImageView fav;
        RelativeLayout relative_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIconHome);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), Details.class);
                    i.putExtra("articleIdt", marticle.get(getAdapterPosition()));
                    v.getContext().startActivity(i);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.dialog_box);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                    Log.d("Textdialog", String.valueOf(mHeadings.get(getAdapterPosition())));
                    text.setText(mHeadings.get(getAdapterPosition()));
                    ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
                    Picasso.with(mcontext).load(mImages.get(getAdapterPosition())).into(image);
                    dialog.show();
                    twitter = (ImageView) dialog.findViewById(R.id.start);
                    twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tweetUrl = "https://twitter.com/intent/tweet?text=Check+out+this+Link: \n" + murl.get(getAdapterPosition()) + "&hashtags=CSCI571NewsSearch" ;
                            Uri uri = Uri.parse(tweetUrl);
                            mcontext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        }
                    });
                    fav = (ImageView) dialog.findViewById(R.id.cancel);
                    SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    if(pref.contains(marticle.get(getAdapterPosition())) == true) {
                        fav.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    }
                    else {
                        fav.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    }
                    fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            if(pref.contains(marticle.get(getAdapterPosition())) == true){
                                fav.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                                bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                                editor.remove(marticle.get(getAdapterPosition()));
                                editor.commit();
                                //Log.d("Bookmarks", String.valueOf(pref));
                                CharSequence text = "'"+ mHeadings.get(getAdapterPosition()) + "' was removed from Bookmarks";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(mcontext, text, duration);
                                toast.show();
                            }
                            else{
                                fav.setImageResource(R.drawable.ic_bookmark_black_24dp);
                                bookmarkIcon.setImageResource(R.drawable.ic_bookmark_black_24dp);
                                Gson gson = new Gson();
                                ArrayList<String> articleData = new ArrayList<>();
                                articleData.add(marticle.get(getAdapterPosition()));
                                articleData.add(mHeadings.get(getAdapterPosition()));
                                articleData.add(mImages.get(getAdapterPosition()));
                                articleData.add(mDates.get(getAdapterPosition()));
                                articleData.add(mSections.get(getAdapterPosition()));
                                articleData.add(murl.get(getAdapterPosition()));
                                String json = gson.toJson(articleData);
                                editor.putString(marticle.get(getAdapterPosition()), json);
                                CharSequence text = "'" + mHeadings.get(getAdapterPosition()) + "' was added to Bookmarks";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(mcontext, text, duration);
                                toast.show();
                                editor.commit();
                            }
                        }
                    });
                    return true;
                }
            });


            bookmarkIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    if(pref.contains(marticle.get(getAdapterPosition())) == true){
                        bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                        editor.remove(marticle.get(getAdapterPosition()));
                        editor.commit();
                        //Log.d("Bookmarks", String.valueOf(pref));
                        CharSequence text = "'"+ mHeadings.get(getAdapterPosition()) + "' was removed from Bookmarks";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(mcontext, text, duration);
                        toast.show();
                    }
                    else {
                        bookmarkIcon.setImageResource(R.drawable.ic_bookmark_black_24dp);
                        Gson gson = new Gson();
                        ArrayList<String> articleData = new ArrayList<>();
                        articleData.add(marticle.get(getAdapterPosition()));
                        articleData.add(mHeadings.get(getAdapterPosition()));
                        articleData.add(mImages.get(getAdapterPosition()));
                        articleData.add(mDates.get(getAdapterPosition()));
                        articleData.add(mSections.get(getAdapterPosition()));
                        articleData.add(murl.get(getAdapterPosition()));
                        String json = gson.toJson(articleData);
                        editor.putString(marticle.get(getAdapterPosition()), json);
                        CharSequence text = "'" + mHeadings.get(getAdapterPosition()) + "' was added to Bookmarks";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(mcontext, text, duration);
                        toast.show();
                        editor.commit();
                    }
                }
            });

            homeImage = itemView.findViewById(R.id.homeImage);
            homeHeading = itemView.findViewById(R.id.homeHeading);
            homeDate = itemView.findViewById(R.id.homeDate);
            homeSection = itemView.findViewById(R.id.homeSection);
            relative_layout = itemView.findViewById(R.id.relative_layout);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIconHome);
        }
    }
}
