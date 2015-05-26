package com.hva.group8.cityguide;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hva.group8.cityguide.Managers.UserInfo;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static MainActivity instance;
    public ActionBarDrawerToggle mDrawerToggle;
    //Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CustomDrawerAdapter mDrawerAdapter;
    private List<DrawerItem> mDrawerItems = new ArrayList<DrawerItem>();
    private ViewPager pager;
    private MainFragment mainFragment;

    public static MainActivity getInstance() {
        if (instance == null)
            instance = new MainActivity();
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Initialize
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserInfo.getInstance().instantiate(getApplicationContext());
        setupNavigationDrawer();

        instance = this;

        int likes = 50;
        int dislikes = 70;
        float rating = 5 * ((float) likes / (likes + dislikes));
        Log.i("Rating", rating + ".");

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.tab_host);
        pager = (ViewPager) mainFragment.getView().findViewById(R.id.pager);
        if (pager == null) {
            Log.i("Pager was", "null!");
        }
    }

    void setupNavigationDrawer() {
        //Hamburger Menu / Back Arrow

        //Nav Drawer
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Add Drawer Item to dataList
        mDrawerItems.add(new DrawerItem("Home", R.drawable.ic_menu_home));
        mDrawerItems.add(new DrawerItem("Route", R.drawable.ic_menu_mapmode));
        mDrawerItems.add(new DrawerItem("Return Watch", R.drawable.ic_menu_revert));
        mDrawerItems.add(new DrawerItem("Settings", R.drawable.ic_settings));
        mDrawerItems.add(new DrawerItem("Account", R.drawable.ic_menu_user));

        //Nav Drawer adapter
        mDrawerAdapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, mDrawerItems);
        mDrawerList.setAdapter(mDrawerAdapter);

        //Nav Drawer Click Listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectItem(position);
            }
        });

        //Navigation Drawer Toggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                //Close
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //Open
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //Toolbar icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    //Switch Fragments
    private void SwitchFragment(Fragment fragment, int navBarPos, int pagerPage) {
        mDrawerList.setItemChecked(navBarPos, true);
        setTitle(mDrawerItems.get(navBarPos).getItemName());
        SwitchFragment(fragment, true, pagerPage);
    }

    public void SwitchFragment(Fragment fragment, boolean showNavDrawer, int pagerPage) {
        //Change with the pager
        pager.setCurrentItem(pagerPage);
        switch (pagerPage) {
            case 0: //Home
                ((TabPageIndicatorAdapter) pager.getAdapter()).ReplaceHomeFragment(fragment);
                break;
            case 1: //Route
                ((TabPageIndicatorAdapter) pager.getAdapter()).ReplaceRouteFragment(fragment);
                break;
            default:
                ((TabPageIndicatorAdapter) pager.getAdapter()).ReplaceHomeFragment(fragment);
                break;
        }
        //mDrawerToggle.setDrawerIndicatorEnabled(showNavDrawer);
        Log.e("Switching Fragment", "To " + fragment.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Called by navigation Drawer
    public void SelectItem(int position) {
        mDrawerLayout.closeDrawer(mDrawerList);
        switch (position) {
            case 0: //Home
                SwitchFragment(HomeFragment.newInstance(), position, 0);
                break;
            case 1: //Route
                SwitchFragment(RouteFragment.newInstance(), position, 1);
                break;
            case 3: //Settings
                SwitchFragment(SettingsFragment.newInstance(), position, 0);
                break;
            default:
                SwitchFragment(BlankFragment.newInstance(), position, 0);
                break;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle.isDrawerIndicatorEnabled() && mDrawerToggle.onOptionsItemSelected(item))
            return true;
        else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START))
            mDrawerLayout.closeDrawer(Gravity.START);
        else {
            Log.i("Pressed:", "Back (Button)");
            if (!mainFragment.onBackPressed())
                getSupportFragmentManager().popBackStack();
        }
    }
}
