/*
 * Copyright (c) 2015.
 * Created by Lansenou on 26-5-15 12:42
 *
 */

package com.hva.group8.cityguide;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Switch;

import com.hva.group8.cityguide.Managers.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SettingsFragment extends SearchActivityFragment {

    public static SettingsFragment instance;

    public static SettingsFragment getInstance() {
        if (instance == null)
            instance = new SettingsFragment();
        return instance;
    }

    public static SettingsFragment newInstance() {
        return (instance = new SettingsFragment());
    }


    String[] langName = {"en", "nl"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        SetupSettings(view);
        parent = HomeFragment.newInstance();
        return view;
    }

    void SetupSettings(View view) {

        Context context = view.getContext();

        //Language
        ArrayAdapter<CharSequence> langAdapter = ArrayAdapter.createFromResource(context, R.array.languages, android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner languages = (Spinner) view.findViewById(R.id.languagespinner);
        languages.setAdapter(langAdapter);

        //Travel
        ArrayAdapter<CharSequence> travelAdapter = ArrayAdapter.createFromResource(context, R.array.travel, android.R.layout.simple_spinner_item);
        travelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner travelmethods = (Spinner) view.findViewById(R.id.travelmethodspinner);
        travelmethods.setAdapter(travelAdapter);

        //Time
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(context, R.array.time, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner timeleft = (Spinner) view.findViewById(R.id.timeleftspinner);
        timeleft.setAdapter(timeAdapter);

        String lang = UserInfo.getInstance().getLanguage();
        int spinnerPosition;
        if (lang.equals("nl"))
            spinnerPosition = langAdapter.getPosition("Dutch");
        else
            spinnerPosition = langAdapter.getPosition("English");
        languages.setSelection(spinnerPosition, false);

        languages.post(new Runnable() {
            public void run() {
                languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        position--;
                        if (position >= langName.length) {
                            setLocale("en");
                            UserInfo.getInstance().setLanguage("en");
                        }
                        setLocale(langName[position]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}

                    public void setLocale(String lang) {
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = new Locale(lang);
                        res.updateConfiguration(conf, dm);
                        UserInfo.getInstance().setLanguage(lang);

                        //End and refresh me
                        ((MainActivity) getActivity()).SwitchFragment(newInstance(), true, 0);
                    }
                });
            }
        });

        Switch notificationswitch = (Switch) view.findViewById(R.id.notificationswitch);
        notificationswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserInfo.getInstance().sendNotifications = isChecked;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
