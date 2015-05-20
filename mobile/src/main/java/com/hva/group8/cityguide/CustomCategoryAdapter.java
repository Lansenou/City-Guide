package com.hva.group8.cityguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lansenou on 6-5-2015.
 */

public class CustomCategoryAdapter extends ArrayAdapter<HomeGroupItem> {
    int resource;

    public CustomCategoryAdapter(Context context, int resource, List<HomeGroupItem> items) {
        super(context, resource, items);
        this.resource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);

        //Get item
        HomeGroupItem item = getItem(position);

        //Picture
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        imageView.setImageResource(item.ImageID);

        //Title
        TextView themeText = (TextView) convertView.findViewById(R.id.textView);
        themeText.setText(item.Title);

        return convertView;
    }

}
