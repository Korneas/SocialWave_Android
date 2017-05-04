package com.example.camilomontoya.socialwave_android;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


public class HomeActivity extends AppCompatActivity {

    BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar.setTabTitleTypeface("fonts/OpenSans-Light.ttf");
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    Home_Frag f = new Home_Frag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,f).commit();
                } else if (tabId == R.id.tab_post) {
                    Post_Frag f = new Post_Frag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,f).commit();
                } else if (tabId == R.id.tab_notify) {
                    Notify_Frag f = new Notify_Frag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,f).commit();
                } else if (tabId == R.id.tab_profile) {
                    Perfil_Frag f = new Perfil_Frag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,f).commit();
                }
            }
        });
    }
}
