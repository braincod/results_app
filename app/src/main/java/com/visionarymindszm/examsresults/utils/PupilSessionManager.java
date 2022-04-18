package com.visionarymindszm.examsresults.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.visionarymindszm.examsresults.R;
import com.visionarymindszm.examsresults.screens.LoginActivity;

import java.util.HashMap;

/**
 * User session Manager class handles the logic details for a given user
 * This is to allow users not be prompt with login screen each time they open
 * the app
 */
public class PupilSessionManager {
    // shared preference
    SharedPreferences sharedPreferences;

    // Editor reference
    Editor editor;

    // activity context or fragment
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = String.valueOf(R.string.app_name);

    // All Shared Preferences Keys
    private static final String IS_PUPIL_LOGIN = "IsPupilLoggedIn";

    public static final String KEY_PUPIL_NAME = "pupilName";
    public static final String KEY_PUPIL_INTAKE = "pupilIntake";
    public static final String KEY_PUPIL_SCHOOL = "pupilSchool";
    public static final String KEY_PUPIL_ID = "pupilID";

    public PupilSessionManager(Context _context) {
        this._context = _context;
        sharedPreferences = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    public void createPupilSession(String pupilID, String pupilName, String pupilSchool, String pupilIntake){
        editor.putBoolean(IS_PUPIL_LOGIN, true);

        editor.putString(KEY_PUPIL_ID, pupilID);
        editor.putString(KEY_PUPIL_SCHOOL, pupilSchool);
        editor.putString(KEY_PUPIL_NAME, pupilName);
        editor.putString(KEY_PUPIL_INTAKE, pupilIntake);

        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isPupilLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        HashMap<String, String> pupil = new HashMap<>();

        pupil.put(KEY_PUPIL_NAME, sharedPreferences.getString(KEY_PUPIL_NAME, null));
        pupil.put(KEY_PUPIL_ID, sharedPreferences.getString(KEY_PUPIL_ID, null));
        pupil.put(KEY_PUPIL_INTAKE, sharedPreferences.getString(KEY_PUPIL_INTAKE, null));
        pupil.put(KEY_PUPIL_SCHOOL, sharedPreferences.getString(KEY_PUPIL_SCHOOL, null));

        return pupil;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    private boolean isPupilLoggedIn() {
        return sharedPreferences.getBoolean(IS_PUPIL_LOGIN, false);
    }


}
