package com.hva.group8.cityguide;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mustafa on 6-5-2015.
 */

public class CustomHomeListAdapter extends BaseExpandableListAdapter {

    private List<HomeGroupItem> groupItem, tempChild;
    public LayoutInflater mInflater;
    public Activity activity;
    private int lastExpandedPosition = -1;
    private ExpandableListView listView;

    public CustomHomeListAdapter(List<HomeGroupItem> grList, ExpandableListView listView) {
        groupItem = grList;
        this.listView = listView;
    }

    public void setInflater(LayoutInflater mInflater, Activity act) {
        this.mInflater = mInflater;
        activity = act;
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        tempChild = (List<HomeGroupItem>) groupItem.get(groupPosition).getChildList();

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child_row, null);
        }
        TextView text = (TextView) convertView.findViewById(R.id.textView);
        text.setText(tempChild.get(childPosition).getText());
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        image.setImageResource(tempChild.get(childPosition).getImageId());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, tempChild.get(childPosition).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        tempChild = ((List<HomeGroupItem>) groupItem.get(groupPosition).getChildList());
        return (tempChild == null) ? 0 : tempChild.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return groupItem.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        if (groupPosition == 0) {
            MainFragment.getInstance().getTabPageIndicatorAdapter().replaceHomeFragment(BlankFragment.newInstance());
            MainFragment.getInstance().getPager().setAdapter(MainFragment.getInstance().getTabPageIndicatorAdapter());
        } else {
            if (lastExpandedPosition != -1
                    && groupPosition != lastExpandedPosition) {
                listView.collapseGroup(lastExpandedPosition);
            }
            super.onGroupExpanded(groupPosition);
            lastExpandedPosition = groupPosition;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
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
