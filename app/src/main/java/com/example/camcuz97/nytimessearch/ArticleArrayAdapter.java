package com.example.camcuz97.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.camcuz97.nytimessearch.activities.ArticleActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by camcuz97 on 6/20/16.
 */

public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder>{

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvTitle;
        public ImageView ivImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            //Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            int position = getLayoutPosition();
            Article article = mArticles.get(position);
            Intent i = new Intent(v.getContext(), ArticleActivity.class);
            i.putExtra("article", article);
            v.getContext().startActivity(i);

        }
    }

    private ArrayList<Article> mArticles;

    public ArticleArrayAdapter(ArrayList<Article> articles){
        mArticles = articles;
    }



    @Override
    public ArticleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleArrayAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = mArticles.get(position);
        // Set item views based on the data model
        TextView textView = viewHolder.tvTitle;
        textView.setText(article.getHeadline());
        ImageView imageView = viewHolder.ivImage;
        imageView.setImageResource(0);
        String thumbnail = article.getThumbNail();
        if(!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(imageView.getContext()).load(thumbnail).into(imageView);
        } else{
            imageView.setImageResource(R.drawable.ic_no_image);
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

//    public ArticleArrayAdapter(Context context, List<Article> articles){
//        super(context, android.R.layout.simple_list_item_1, articles);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        //get the data item for current position
//        Article article = getItem(position);
//        //check to see if existing view is being reused
//        //not using recycled view --> inflate the layout
//        if(convertView == null){
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
//        }
//        //find the image view
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
//        //clear out recycled image from convertview from last time
//        imageView.setImageResource(0);
//
//        //set textview to be the headline
//        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//        tvTitle.setText(article.getHeadline());
//
//        //populate thumbnail image
//        //remote download image in the background
//        String thumbnail = article.getThumbNail();
//        if(!TextUtils.isEmpty(thumbnail)){
//            //if thumbnail isn't empty, download thumbnail
//            Picasso.with(getContext()).load(thumbnail).into(imageView);
//        }
//        else{
//            //if it is empty, set to default image
//            imageView.setImageResource(R.drawable.ic_no_image);
//        }
//        return convertView;
//    }
}
