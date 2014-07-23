package com.special.ResideMenuDemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class MenuActivity extends FragmentActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private MenuActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        setUpMenu();
        changeToHomeFragment();
    }

    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
//        resideMenu.setBackground(R.drawable.menu_background);
        ImageView bg = (ImageView) LayoutInflater.from(this).inflate(R.layout.single_image, null);
        resideMenu.setBackGroundView(bg);

        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "Home");
        itemProfile  = new ResideMenuItem(this, R.drawable.icon_profile,  "Profile");
        itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar, "Calendar");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemHome){
            changeToHomeFragment();
        }else if (view == itemProfile){
            changeFragment(new ProfileFragment());
        }else if (view == itemCalendar){
            changeFragment(new CalendarFragment());
        }else if (view == itemSettings){
            changeFragment(new SettingsFragment());
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
//    public ResideMenu getResideMenu(){
//        return resideMenu;
//    }
    // listener pattern alternative begin
    private HomeFragment.OnViewCommonListener mViewCommonListener;
    private void changeToHomeFragment() {
        HomeFragment fragment = new HomeFragment();
        if (null == mViewCommonListener) {
            mViewCommonListener = new HomeFragment.OnViewCommonListener() {
                @Override
                public void onViewIgnored(View view) {
                    resideMenu.addIgnoredView(view);
                }

                @Override
                public void onViewToggle() {
                    resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                }
            };
        }
        fragment.setViewCommonListener(mViewCommonListener);

        changeFragment(fragment);
    }

    // listener pattern alternative end

    @Override
    protected void onResume() {
        super.onResume();
        SettingsFragment.startListen(this, getSettingReceiver());
    }
    @Override
    protected  void onPause() {
        super.onPause();
        if (null != mSettingReceiver) {
            SettingsFragment.stopListen(this, mSettingReceiver);
        }
    }

    private BroadcastReceiver mSettingReceiver;
    private BroadcastReceiver getSettingReceiver() {
        if (null == mSettingReceiver) {
            mSettingReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View bg = SettingsFragment.getTargetView(inflater, intent);
                    if (null == bg) {
                        resideMenu.setBackground(R.drawable.menu_background);
                    } else {
                        resideMenu.setBackGroundView(bg);
                    }
                }
            };
        }
        return mSettingReceiver;
    }
}
