package com.hva.group8.cityguide;

import java.io.Serializable;

/**
 * Created by Lansenou on 19-5-2015.
 */
public class ActivityItem implements Serializable {
    public String Title;
    public String TitleEN;
    public String ShortDescription;
    public String ShortDescriptionEN;
    public String LongDescription;
    public String LongDescriptionEN;
    public String URL;
    public String PictureURL;
    public String Calender;
    public String CalenderEN;
    public String Id;
    public double Longitude, Latitude;
    public float Distance;
    public String TravelTime;
}
