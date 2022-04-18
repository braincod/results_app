package com.visionarymindszm.examsresults;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.visionarymindszm.examsresults.utils.PupilSessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PupilSessionManager sessionManager = new PupilSessionManager(getApplicationContext());

        // if the  user is activate
        // if this returns true go to login
        if (sessionManager.checkLogin())
            finish();


        setUpNavigation();

    }

    private void setUpNavigation(){
       BottomNavigationView mBottomNavigationView =findViewById(R.id.bottom_nav);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mBottomNavigationView,
                navHostFragment.getNavController());

    }
}
