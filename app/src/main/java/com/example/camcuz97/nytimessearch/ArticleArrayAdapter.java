package com.example.camcuz97.nytimessearch;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by camcuz97 on 6/20/16.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article>{

    public ArticleArrayAdapter(Context context, List<Article> articles){
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for current position
        Article article = getItem(position);
        //check to see if existing view is being reused
        //not using recycled view --> inflate the layout
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }
        //find the image view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        //clear out recycled image from convertview from last time
        imageView.setImageResource(0);

        //set textview to be the headline
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadline());

        //populate thumbnail image
        //remote download image in the background
        String thumbnail = article.getThumbNail();
        if(!TextUtils.isEmpty(thumbnail)){
            //if thumbnail isn't empty, download thumbnail
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }
        else{
            //if it is empty, set to default image
            imageView.setImageResource(R.drawable.ic_no_image);
        }
        return convertView;
    }
}
