package com.hva.group8.cityguide.Loaders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.hva.group8.cityguide.ActivityItem;
import com.hva.group8.cityguide.ActivityListAdapter;
import com.hva.group8.cityguide.Managers.UserInfo;
import com.hva.group8.cityguide.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lansenou on 19-5-2015.
 */
public class LoadActivityItemFromURL extends AsyncTask<ArrayList<NameValuePair>, List<ActivityItem>, List<ActivityItem>> {

    ProgressDialog dialog;
    boolean hasPrepared = false;
    private ActivityListAdapter adpt;
    private ArrayList<NameValuePair> nameValuePairs;
    private Context context;
    private String url;

    public LoadActivityItemFromURL(ActivityListAdapter adpt, ArrayList<NameValuePair> nameValuePairs, Context context, String url) {
        this.adpt = adpt;
        this.nameValuePairs = nameValuePairs;
        this.context = context;
        this.url = url;
    }

    public void CancelTask(boolean mayInterruptIfRunning) {
        cancel(mayInterruptIfRunning);
        if (!adpt.isEmpty())
            adpt.clear();
    }

    public LoadActivityItemFromURL dialog(Activity activity) {
        super.onPreExecute();
        dialog = new ProgressDialog(activity);
        dialog.setMessage(context.getString(R.string.action_loading));
        dialog.setTitle(context.getString(R.string.action_loading_title));
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.show();
        return this;
    }

    @Override
    protected List<ActivityItem> doInBackground(ArrayList<NameValuePair>... params) {
        try {
            Looper.prepare();
        } catch (Exception e) {
            Log.e("Looper", e.toString());
        }

        String getActivity = DBConnect.Read(nameValuePairs, url);
        Log.e("Async Link", getActivity);

        try {
            JSONArray jArray = new JSONArray(getActivity);
            List<ActivityItem> result = loadList(jArray);
            if (UserInfo.getInstance() != null)
                UserInfo.getInstance().newToast(context, context.getString(R.string.toast_found) + result.size(), Toast.LENGTH_SHORT);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
        Looper.myLooper().quit();
        } catch (Exception e) {
            Log.e("Looper", e.toString());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(List<ActivityItem>... values) {
        super.onProgressUpdate(values);
        adpt.setItemList(values[0]);
    }

    private List<ActivityItem> loadList(JSONArray jArray) {
        List<ActivityItem> result = new ArrayList<>();

        try {
            for (int i = 0; i < jArray.length(); i++) {
                result.add(convertContact(jArray.getJSONObject(i)));
                if (dialog != null && (i == 3 || i == jArray.length() - 1))
                    dialog.dismiss();
                //Update to the ListView every 3 results
                if (i % 3 == 0)
                    publishProgress(result);
            }
            return result;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        List<ActivityItem> list = new ArrayList<>();
        return list;
    }

    @Override
    protected void onPostExecute(List<ActivityItem> result) {
        super.onPostExecute(result);
        if (dialog != null)
            dialog.dismiss();
        adpt.setItemList(result);
        adpt.notifyDataSetChanged();
    }

    private ActivityItem convertContact(JSONObject obj) throws JSONException {
        ActivityItem tempItem = new ActivityItem();
        tempItem.Id = obj.getString("TrcId");
        tempItem.Title = obj.getString("Title");
        tempItem.TitleEN = obj.getString("TitleEN");
        tempItem.ShortDescription = obj.getString("ShortDescription");
        tempItem.ShortDescriptionEN = obj.getString("ShortDescriptionEN");
        tempItem.LongDescription = obj.getString("LongDescription");
        tempItem.LongDescriptionEN = obj.getString("LongDescriptionEN");
        tempItem.Calender = obj.getString("CalendarSummary");
        tempItem.CalenderEN = obj.getString("CalendarSummaryEN");
        tempItem.PictureURL = obj.getString("Media");
        tempItem.URL = obj.getString("Urls");
        tempItem.Latitude = Double.parseDouble(obj.getString("Latitude"));
        tempItem.Longitude = Double.parseDouble(obj.getString("Longitude"));
        tempItem.Distance = UserInfo.getInstance().getDistance(context, tempItem.Longitude, tempItem.Latitude);
        tempItem.TravelTime = UserInfo.getInstance().getTime(tempItem.Distance);
        tempItem.Likes = obj.getInt("RateLike");
        tempItem.Dislikes = obj.getInt("RateDislike");
        return tempItem;
    }
}
