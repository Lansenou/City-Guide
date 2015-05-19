package com.hva.group8.cityguide;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mustafa on 6-5-2015.
 */

public class CustomCategoryAdapter extends ArrayAdapter<HomeGroupItem> {
    int resource;

    public CustomCategoryAdapter(Context context, int resource, List<HomeGroupItem> items) {
        super(context, resource, items);
        this.resource = resource;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_row, null);
        }

        TextView text = (TextView) convertView.findViewById(R.id.textView);
        text.setText(groupItem.get(groupPosition).getText());

        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        image.setImageResource(groupItem.get(groupPosition).getImageId());
        //((TextView) convertView).setChecked(isExpanded);

        ImageView indicator = (ImageView) convertView.findViewById(R.id.indicator);
        int imageResourceId = isExpanded ? R.drawable.arrow_down : R.drawable.arrow_right;
        if (groupItem.get(groupPosition).getChildList() != null)
            indicator.setImageResource(imageResourceId);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
