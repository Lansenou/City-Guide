package com.hva.group8.cityguide;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CustomDrawerAdapter mDrawerAdapter;
    private List<DrawerItem> mDrawerItems = new ArrayList<DrawerItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserInfo.getInstance().instantiate(getApplicationContext());

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
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {//Default selected item=0
            SelectItem(0);
        }
    }

    public void SwitchFragment(Fragment fragment, int navBarPos) {
        mDrawerList.setItemChecked(navBarPos, true);
        setTitle(mDrawerItems.get(navBarPos).getItemName());
        SwitchFragment(fragment);
    }

    public void SwitchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //MainFragment.getInstance().getTabPageIndicatorAdapter().replaceHomeFragment(fragment);

        //Add to fragment manager
        // if (!fragment.isAdded())
        //     transaction.add(R.id.content_frame, fragment, fragment.getTag());


        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void SelectItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = MainFragment.newInstance();
                break;
//            case 1:
//                fragment = BlankFragment.newInstance();
//                //Bundle args = new Bundle();
//            \    //args.putString("text", "This is Drawer item1");
//                //fragment.setArguments(args);
//                break;
            default:
                fragment = BlankFragment.newInstance();
                break;
        }
        SwitchFragment(fragment, position);
        mDrawerLayout.closeDrawer(mDrawerList);
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
       return mDrawerToggle.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.START))
            mDrawerLayout.closeDrawer(Gravity.START);
        else {
            getSupportFragmentManager().popBackStack();
            //finish();
            //overridePendingTransition( 0, 0);
            //startActivity(getIntent());
            //overridePendingTransition(0, 0);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SelectItem(position);
        }
    }
}
