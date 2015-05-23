/*
 * Copyright (c) 2015.
 * Created by Lansenou on 23/05/15
 *
 */

package com.hva.group8.cityguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hva.group8.cityguide.Loaders.CustomPagerAdapter;
import com.hva.group8.cityguide.Managers.RouteManager;
import com.hva.group8.cityguide.Managers.UserInfo;

import java.text.DecimalFormat;


public class ViewActivityFragment extends CustomFragment {

    public static ViewActivityFragment instance;

    public static ViewActivityFragment getInstance() {
        if (instance == null)
            instance = new ViewActivityFragment();
        return instance;
    }

    public static ViewActivityFragment newInstance() {
        return (instance = new ViewActivityFragment());
    }


    ActivityItem myItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_activity, container, false);

        //Get all the views
        TextView tvDescription = (TextView) view.findViewById(R.id.infoTxt);
        TextView tvOpeningstijd = (TextView) view.findViewById(R.id.OpeningstijdTxt);
        ViewPager vpPicture = (ViewPager) view.findViewById(R.id.banner);
        TextView tvURL = (TextView) view.findViewById(R.id.urlTxt);
        TextView tvTitle = (TextView) view.findViewById(R.id.titelTxt);
        TextView distance = (TextView) view.findViewById(R.id.distanceText);
        TextView estTime = (TextView) view.findViewById(R.id.EstTimeTxt);

        // Lookup and populate view with Time
        //Set all the text values
        tvOpeningstijd.setText(UserInfo.getInstance().getLanguage().equals("nl") ? myItem.Calender : myItem.CalenderEN);
        tvTitle.setText(UserInfo.getInstance().getLanguage().equals("nl") ? myItem.Title : myItem.TitleEN);

        DecimalFormat df = new DecimalFormat("#.00");
        estTime.setText(myItem.TravelTime);

        float tempDist = Float.parseFloat(df.format(myItem.Distance / 1000).replace(",", "."));
        distance.setText(String.valueOf(tempDist) + " km");

        //Check if LongDesc is empty
        String text = UserInfo.getInstance().getLanguage().equals("nl") ? myItem.LongDescription : myItem.LongDescriptionEN;
        if (text.isEmpty())
            text = UserInfo.getInstance().getLanguage().equals("nl") ? myItem.ShortDescription : myItem.ShortDescriptionEN;
        tvDescription.setText(Html.fromHtml(text));

        //Check if URL is empty
        String url = myItem.URL.split(",")[0];
        if (!url.isEmpty()) {
            url = "<a href=\"" + url + "\">" + getText(R.string.action_website) + "</a>";
            tvURL.setText(Html.fromHtml(url));
            tvURL.setMovementMethod(LinkMovementMethod.getInstance());
        } else
            tvURL.setText("");

        //Load picture
        String[] newUrl = myItem.PictureURL.split(",");
        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getActivity().getApplicationContext(), newUrl);
        vpPicture.setAdapter(mCustomPagerAdapter);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            //Add item to list
            RouteManager.getInstance().Add(myItem, getActivity().getApplicationContext());
        } else if (id == android.R.id.home) {
            //Previous Fragment
            ((MainActivity) getActivity()).SwitchFragment(ActivityListFragment.newInstance(), false, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_view_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
