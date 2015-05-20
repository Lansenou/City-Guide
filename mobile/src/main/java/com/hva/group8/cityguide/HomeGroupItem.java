package com.hva.group8.cityguide;

import java.util.List;

/**
 * Created by Mustafa on 12-5-2015.
 */

public class HomeGroupItem {

    public String Title;
    public String Query;
    public int ImageID;
    public List<HomeGroupItem> ChildList;

    public HomeGroupItem(int ImageID, String Title) {
        this.Title = Title;
        this.ImageID = ImageID;
    }

    public HomeGroupItem(int ImageID, String Title, String Query) {
        this.Title = Title;
        this.ImageID = ImageID;
        this.Query = Query;
    }

    public HomeGroupItem(int ImageID, String Title, List<HomeGroupItem> ChildList) {
        this.Title = Title;
        this.ImageID = ImageID;
        this.ChildList = ChildList;
    }

    public HomeGroupItem(String Title, int ImageID) {
        this.Title = Title;
        this.ImageID = ImageID;
        this.ChildList = null;
    }
}
