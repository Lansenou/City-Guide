package com.hva.group8.cityguide.Loaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hva.group8.cityguide.Managers.UILManager;
import com.hva.group8.cityguide.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by Lansenou on 29-4-2015.
 */
public class CustomPagerAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    String[] newUrl;
    ImageLoader imageLoader;

    public CustomPagerAdapter(Context context, String[] newUrl) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.newUrl = newUrl;
        imageLoader = UILManager.instance().getImageLoader(context);
    }


    @Override
    public int getCount() {
        return newUrl.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //Get item View
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        //Banner
        ImageView imageView = (ImageView) itemView.findViewById(R.id.banner);
        final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.loadingPanel);

        //Load image in the loader
        imageLoader.displayImage(newUrl[position] + "?w", imageView, UILManager.getNormalImage(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }
        });

        //Add view to the container
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}