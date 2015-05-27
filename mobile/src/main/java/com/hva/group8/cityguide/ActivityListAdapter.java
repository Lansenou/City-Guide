package com.hva.group8.cityguide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hva.group8.cityguide.Managers.RouteManager;
import com.hva.group8.cityguide.Managers.UILManager;
import com.hva.group8.cityguide.Managers.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Lansenou on 19-5-2015.
 */
public class ActivityListAdapter extends ArrayAdapter<ActivityItem> {
    //Our Imageloader
    ImageLoader imageLoader;

    //Constructor
    ActivityListAdapter(Context context, int resource, List<ActivityItem> objects) {
        super(context, resource, objects);
        imageLoader = UILManager.instance().getImageLoader(getContext());
    }

    //Reset the list
    public void setItemList(List<ActivityItem> objects) {
        if (objects == null)        //Double Check that objects isn't empty
            return;
        clear();                    //Clear List
        addAll(objects);            //Add items
        notifyDataSetChanged();     //Notify Ourself
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_layout, parent, false);
        }

        // Get item from the adapter
        final ActivityItem item = getItem(position);

        // Lookup and populate view with Title
        TextView tvTitle = (TextView) convertView.findViewById(R.id.activityTekst1);
        String title = UserInfo.getInstance().getLanguage().equals("nl") ? item.Title : item.TitleEN;
        tvTitle.setSelected(true);
        /*if (title.length() > 20) {
            title.substring(0, 20);
            title += "...";
        }*/
        tvTitle.setText(title);

        // Lookup and populate view with Distance
        TextView distance = (TextView) convertView.findViewById(R.id.distanceText);
        DecimalFormat df = new DecimalFormat("#.0");
        float tempDist = Float.parseFloat(df.format(item.Distance / 1000).replace(",", "."));
        distance.setText(String.valueOf(tempDist) + " km");

        // Lookup and populate view with Time
        TextView estTime = (TextView) convertView.findViewById(R.id.EstTimeTxt);
        estTime.setText(item.TravelTime);

        // Lookup and populate view with ImageButton
        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteManager.getInstance().Add(item, getContext());
            }
        });

        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        float rating = 5 * ((float) item.Likes / ((float) item.Likes + (float) item.Dislikes));
        if (item.Likes == 0 && item.Dislikes == 0)
            rating = 2.5f;
        ratingBar.setRating(rating);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(convertView.getResources().getColor(R.color.star_full), PorterDuff.Mode.SRC_ATOP); //FULL
        //stars.getDrawable(1).setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //Lines
        stars.getDrawable(0).setColorFilter(convertView.getResources().getColor(R.color.transparent), PorterDuff.Mode.SRC_ATOP); //EMPTY

        //Stuff needed to load the picture and show/remove the progressbar
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.loadingPanel);
        ImageView ivPicture = (ImageView) convertView.findViewById(R.id.ivPicture);
        String[] newStr = item.PictureURL.split(",");

        //Display Configuration
        // ImageLoader will bind the url with the picture
        imageLoader.displayImage(newStr[0], ivPicture, UILManager.instance().getRoundImage(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
