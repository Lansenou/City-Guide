package com.hva.group8.cityguide.Managers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hva.group8.cityguide.ActivityItem;
import com.hva.group8.cityguide.MainActivity;
import com.hva.group8.cityguide.R;
import com.hva.group8.cityguide.RouteFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lansenou on 6-5-2015.
 */
public class RouteManager {
    private static RouteManager instance;
    public List<ActivityItem> routeList = new ArrayList<>();

    public static RouteManager getInstance() {
        if (instance == null)
            instance = new RouteManager();
        return instance;
    }

    public void Add(ActivityItem item, Context context) {
        if (routeList.size() >= 8) {
            UserInfo.getInstance().newToast(context, context.getString(R.string.toast_full), Toast.LENGTH_SHORT);
            return;
        }
        routeList.add(item);

        //Update User
        UserInfo.getInstance().newToast(context, context.getString(R.string.toast_added) + (UserInfo.getInstance().getLanguage().equals("nl") ? item.Title : item.TitleEN), Toast.LENGTH_SHORT);

        if (routeList.size() == 1) {
            UserInfo.getInstance().newToast(context, context.getString(R.string.toast_added) + (UserInfo.getInstance().getLanguage().equals("nl") ? item.Title : item.TitleEN), Toast.LENGTH_SHORT);
            MainActivity.getInstance().SwitchFragment(RouteFragment.newInstance(), true, 1);
        }

        //Update route
        //RouteActivity.getInstance().UpdateList();
    }

    public void LoopThroughList() {
        Log.i("RouteManager", "List has " + routeList.size() + " items.");
    }

    public void Clear() {
        routeList.clear();

        //Update Route List
        //RouteActivity.getInstance().UpdateList();
    }

    public void Remove(int removeItemAt, Context context) {
        routeList.remove(removeItemAt);
        UserInfo.getInstance().newToast(context, context.getString(R.string.toast_removed), Toast.LENGTH_SHORT);
        //Update Route List
        //RouteActivity.getInstance().UpdateList();
    }

}

