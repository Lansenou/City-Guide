package com.hva.group8.cityguide;

import java.util.List;

/**
 * Created by Mustafa on 12-5-2015.
 */

public class HomeGroupItem {

    String text;
    int imageId;
    List<HomeGroupItem> childList;

    public HomeGroupItem(int imageId, String text, List<HomeGroupItem> childList) {
        super();
        this.text = text;
        this.imageId = imageId;
        this.childList = childList;
    }

    public HomeGroupItem(int imageId, String text) {
        super();
        this.text = text;
        this.imageId = imageId;
        this.childList = null;
    }


    public String getText() {
        return text;
    }

    public int getImageId() {
        return imageId;
    }

    public List<HomeGroupItem> getChildList() {
        return childList;
    }



}
