/*
 * Copyright (c) 2015.
 * Created by Lansenou on 27-5-15 10:13
 *
 */

package com.hva.group8.cityguide;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.hva.group8.cityguide.Managers.UserInfo;

import java.util.ArrayList;
import java.util.List;


public class SmartwatchFragment extends SearchActivityFragment {

    public static SmartwatchFragment instance;

    public static SmartwatchFragment getInstance() {
        if (instance == null)
            instance = new SmartwatchFragment();
        return instance;
    }

    public static SmartwatchFragment newInstance() {
        return (instance = new SmartwatchFragment());
    }

    //Prevent from reloading every time the user click on a different tab
    private boolean fragmentResume=false;
    private boolean fragmentVisible=false;
    private boolean fragmentOnCreated=false;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_watch, container, false);
        return view;
    }

    void dialog() {
        //Cancel current toast
        Toast toast = UserInfo.getInstance().toast;
        if (toast != null)
            toast.cancel();

        //Show alert dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        alert.setTitle(R.string.action_send_title);
        alert.setMessage(R.string.action_send);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        if (visible && isResumed()){   // only at fragment screen is resumed
            fragmentResume=true;
            fragmentVisible=false;
            fragmentOnCreated=true;

            //Show dialog
            dialog();
        }else  if (visible){        // only at fragment onCreated
            fragmentResume=false;
            fragmentVisible=true;
            fragmentOnCreated=true;
        }
        else if(!visible && fragmentOnCreated){// only when you go out of fragment screen
            fragmentVisible=false;
            fragmentResume=false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
