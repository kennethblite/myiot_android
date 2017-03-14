package com.example.colin.myapplication;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity
        implements Recommendationrpt.OnRecommendReport ,
        Account.OnAccountListener,
        Tracking.OnTrackingListener,
        Timeline.OnTimelineListener{

    private String[] Pages;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create a new Fragment to be placed in the activity layout
        Timeline firstFragment = Timeline.newInstance();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        //firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.replace_all, firstFragment).commit();

        Pages = new String[]{"Recommendations", "Tracking", "Account"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, Pages));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mTitle);
            }
        };


        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        Toast.makeText(this, Pages[position], Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setNewFragment(Pages[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void setNewFragment(String newfragname){
        Fragment newfrag;
        switch(newfragname){
            case "Recommendations":{
                newfrag = Timeline.newInstance();
                break;
            }
            case "Tracking":{
                newfrag = Tracking.newInstance();
                break;
            }
            case "Account":{
                newfrag = Account.newInstance();
                break;
            }
            default:{
                newfrag = Recommendationrpt.newInstance("shouldn;t", "exist", "wtf");
                break;
            }
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.replace_all, newfrag).commit();
    }


    @Override
    public void onDeletePushed(){
        Toast.makeText(this, "Mainactivity registered delete", Toast.LENGTH_SHORT).show();
        Timeline firstFragment = Timeline.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.replace_all, firstFragment).commit();

    }
    @Override
    public void onArrowPushed() {
        Toast.makeText(this, "Mainactivity registered Arrow", Toast.LENGTH_SHORT).show();
        Timeline firstFragment = Timeline.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.replace_all, firstFragment).commit();



    }

    @Override
    public void onRecommendation(Recommendation d){
        Fragment newfrag = Recommendationrpt.newInstance(d);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.replace_all, newfrag).commit();
    }

    @Override
    public void onLogout(){
        User.Clear();
        finish();
    }

private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        selectItem(position);
    }
}
}
