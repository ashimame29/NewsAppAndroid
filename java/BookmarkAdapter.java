package com.example.newsapp_1;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mHeadings = new ArrayList<>();
    private ArrayList<String> mDates = new ArrayList<>();
    private ArrayList<String> mSections = new ArrayList<>();
    private ArrayList<String> marticle = new ArrayList<>();
    private ArrayList<String> murl = new ArrayList<>();
    //SimpleDateFormat sdf = new SimpleDateFormat("MMM-d");
    //SharedPreferences sharedPreferences;
    //SharedPreferences.Editor editor;
    private Context mcontext;

    public BookmarkAdapter(Context context, ArrayList<String> images, ArrayList<String> headings, ArrayList<String> dates, ArrayList<String> section, ArrayList<String> id, ArrayList<String> url){
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
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmarks, parent, false);
        return new BookmarkAdapter.ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.ViewHolder holder, int position) {
        Picasso.with(mcontext).load(mImages.get(position)).into(holder.bookImage);
        holder.bookHeading.setText(mHeadings.get(position));
        Instant instant = Instant.parse(mDates.get(position));
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("America/Los_Angeles"));
        holder.bookDate.setText(DateTimeFormatter.ofPattern("dd MMM").format(zonedDateTime));
        if(mSections.get(position).length()>14) {
            String new_string = mSections.get(position).substring(0, 13) + "...";
            holder.bookSection.setText(new_string);
        }
        else{
            holder.bookSection.setText(mSections.get(position));
        }
        //holder.bookSection.setText(mSections.get(position));
        //holder.articleID.setText(marticle.get(position));
    }


    @Override
    public int getItemCount() {
        return mHeadings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView bookImage;
        TextView bookHeading;
        TextView bookDate;
        TextView bookSection;
        ImageView bookIcon;
        ImageView twitter;
        TextView txtView;
        ImageView fav;
        RelativeLayout relative_layout;
        //SharedPreferences sharedPreferences = mcontext.getSharedPreferences("id", MODE_PRIVATE);
        //TextView articleID;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            bookIcon = itemView.findViewById(R.id.bookIcon);
            txtView = ((Activity) mcontext).findViewById(R.id.toggleText);
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
                    twitter=(ImageView) dialog.findViewById(R.id.start);
                    twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tweetUrl = "https://twitter.com/intent/tweet?text=Check+out+this+Link: \n" + murl.get(getAdapterPosition()) + "&hashtags=CSCI571NewsSearch" ;
                            Uri uri = Uri.parse(tweetUrl);
                            mcontext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        }
                    });
                    fav = (ImageView) dialog.findViewById(R.id.cancel);
                    fav.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.remove(marticle.get(getAdapterPosition()));
                            CharSequence text = "'" + mHeadings.get(getAdapterPosition()) + "' was removed from Bookmarks";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(mcontext, text, duration);
                            toast.show();
                            marticle.remove(marticle.get(getAdapterPosition()));
                            mHeadings.remove(mHeadings.get(getAdapterPosition()));
                            mSections.remove(mSections.get(getAdapterPosition()));
                            mDates.remove(mDates.get(getAdapterPosition()));
                            mImages.remove(mImages.get(getAdapterPosition()));
                            murl.remove(murl.get(getAdapterPosition()));
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), getItemCount());
                            Log.d("position", String.valueOf(getAdapterPosition()));
                            editor.commit();
                            dialog.dismiss();
                            if(marticle.size() == 0){
                                txtView.setText("No Bookmarked Articles");
                            }
                            else{
                                txtView.setText("");
                            }
                        }
                    });

                    return true;
                }

            });

            bookIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //layout_to_hide = itemView.findViewById(R.id.layout_to_hide);
                    SharedPreferences pref = mcontext.getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove(marticle.get(getAdapterPosition()));
                    //Log.d("Bookmarks", String.valueOf(pref));
                    CharSequence text = "'"+ mHeadings.get(getAdapterPosition()) + "' was removed from Bookmarks";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(mcontext, text, duration);
                    toast.show();
                    marticle.remove(marticle.get(getAdapterPosition()));
                    mHeadings.remove(mHeadings.get(getAdapterPosition()));
                    mSections.remove(mSections.get(getAdapterPosition()));
                    mDates.remove(mDates.get(getAdapterPosition()));
                    mImages.remove(mImages.get(getAdapterPosition()));
                    murl.remove(murl.get(getAdapterPosition()));
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), getItemCount());
                    //Log.d("position", String.valueOf(getAdapterPosition()));
                    editor.commit();
                    if(marticle.size() == 0){
                        txtView.setText("No Bookmarked Articles");
                    }
                    else{
                        txtView.setText("");
                    }

                }
            });

            bookImage = itemView.findViewById(R.id.bookImage);
            bookHeading = itemView.findViewById(R.id.bookHeading);
            bookDate = itemView.findViewById(R.id.bookDate);
            bookSection = itemView.findViewById(R.id.bookSection);
            relative_layout = itemView.findViewById(R.id.relative_layout_card);
            //articleID=itemView.findViewById(R.id.articleID);
        }

    }

}


