package com.visionarymindszm.examsresults.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.visionarymindszm.examsresults.MainActivity;
import com.visionarymindszm.examsresults.R;
import com.visionarymindszm.examsresults.utils.HoldVariables;
import com.visionarymindszm.examsresults.utils.RequestHandler;
import com.visionarymindszm.examsresults.utils.PupilSessionManager;
import com.visionarymindszm.examsresults.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText pupil_id, pupil_password;
    ProgressBar progressBarLogin;
    Button loginButton;
    ConstraintLayout login_layout;
    private String TAG = getClass().getName();
    PupilSessionManager pupilSessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pupil_id = findViewById(R.id.pupil_id);
        pupil_password = findViewById(R.id.pupil_password);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        progressBarLogin.setVisibility(View.INVISIBLE);
        loginButton = findViewById(R.id.loginButton);
        login_layout = findViewById(R.id.login_layout);
        pupilSessionManager = new PupilSessionManager(getApplicationContext());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {
        if (TextUtils.isEmpty(pupil_id.getText().toString())){
            pupil_id.setError("ID is required");
        }else if(TextUtils.isEmpty(pupil_password.getText().toString())){
            pupil_password.setError("Password is required");
        }else{
            loginButton.setEnabled(false);
            progressBarLogin.setVisibility(View.VISIBLE);

            String pupilID = pupil_id.getText().toString();
            String pupilPassword = pupil_password.getText().toString();

            onLogin(pupilID, pupilPassword, getApplicationContext());

        }
    }

    /**
     *
     * @param pupil_id 'integer type'
     * @param password 'String'
     * @param context Pass the current application context to the method
     */
    public void onLogin(final String pupil_id, final String password, final Context context){

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Utils.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            System.out.println(jsonObject.getString("error"));
                            if (!jsonObject.getBoolean("error")){

                                JSONArray dataArray = jsonObject.getJSONArray("message");

                                for(int i = 0; i < dataArray.length(); i++){
                                    // replace this with shared preference
                                    JSONObject dataObject = dataArray.getJSONObject(i);

                                    //
                                    pupilSessionManager.createPupilSession(pupil_id,
                                            dataObject.getString("pupil_name"),
                                            dataObject.getString("pupil_school"),
                                            dataObject.getString("pupil_intake"));

                                }

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }else{
                                loginButton.setEnabled(true);
                                progressBarLogin.setVisibility(View.INVISIBLE);
                                Utils.showSnackBar(login_layout, "Wrong password or Username, try again", -1);
                            }
                        } catch (JSONException e) {
                            loginButton.setEnabled(true);
                            progressBarLogin.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "Error", e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loginButton.setEnabled(true);
                        progressBarLogin.setVisibility(View.INVISIBLE);
                        error.printStackTrace();
                        Log.d(TAG, "Error on response", error);
                        Utils.showSnackBar(login_layout, "Ensure you have connection", 0);

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("pupil_id", pupil_id);
                params.put("password", password);
                return params;
            }
        };

        RequestHandler.getInstance(context).addRequestQueue(stringRequest);
    }
}