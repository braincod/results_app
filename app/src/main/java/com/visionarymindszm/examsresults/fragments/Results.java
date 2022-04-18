package com.visionarymindszm.examsresults.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.visionarymindszm.examsresults.MainActivity;
import com.visionarymindszm.examsresults.R;
import com.visionarymindszm.examsresults.utils.HoldVariables;
import com.visionarymindszm.examsresults.utils.PupilSessionManager;
import com.visionarymindszm.examsresults.utils.RequestHandler;
import com.visionarymindszm.examsresults.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Results extends Fragment {
    CardView results_card;
    TextView subject_results,grade_results;
    Button view_results_button;
    final String TAG = "RESULT_FRAGMENT";
    ProgressBar progressBarResults;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_fragment, container, false);
        Log.d(TAG,"check results from the on create view");
        results_card = view.findViewById(R.id.results_card);
        subject_results = view.findViewById(R.id.subject_results);
        grade_results = view.findViewById(R.id.grade_results);
        view_results_button = view.findViewById(R.id.view_results_button);
        results_card.setVisibility(View.GONE);
        progressBarResults = view.findViewById(R.id.progressBarResults);
        view_results_button.setOnClickListener(view1 -> {
          Log.d(TAG,"check results has been called ");
            check_results();
        });
        return  view;
    }

    /**
     * Check the results from the server function
     */
    private void check_results() {
        Log.d(TAG,"check results inside inthe method ");
        progressBarResults.setVisibility(View.VISIBLE);
        final PupilSessionManager sessionManager = new PupilSessionManager(requireContext());
        HashMap<String, String> pupils = sessionManager.getUserDetails();

        final String pupil_id = pupils.get(PupilSessionManager.KEY_PUPIL_ID);
        final String pupil_intake = pupils.get(PupilSessionManager.KEY_PUPIL_INTAKE);


        // Get the response from the server

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Utils.GET_RESULTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"check results from the server  ");
                        progressBarResults.setVisibility(View.GONE);
                        try {
                            Log.d(TAG,"check results from the try  ");
                            JSONObject jsonObject = new JSONObject(response);
                            System.out.println(jsonObject.getString("error"));
                            results_card.setVisibility(View.VISIBLE);
                            if (!jsonObject.getBoolean("error")){
                                Log.d(TAG,"check results from the if statement ");

                                JSONArray dataArray = jsonObject.getJSONArray("message");

                                for(int i = 0; i < dataArray.length(); i++){
                                    // replace this with shared preference
                                    JSONObject dataObject = dataArray.getJSONObject(i);

                                    subject_results.append(dataObject.getString("subject_name")+"\n");
                                    grade_results.append(dataObject.getString("subject_grade")+"\n");
                                }


                            }else{
                                Log.d(TAG,"check results from the else statement ");
                                    subject_results.setText("Your results haven't been uploaded yet");
                            }
                        } catch (JSONException e) {
                            Log.d(TAG,"check results from the catch  ");
                            Log.d(TAG, "Error", e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"check results from error response ");
                        error.printStackTrace();
                        Log.d(TAG, "Error on response", error);
                        System.out.println(error.getCause().toString());
                        System.out.println(error.getMessage());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()  {
                Log.d(TAG,"check results from the hmap ");
                Map<String, String> params = new HashMap<>();
                params.put("pupil_id", pupil_id);
                params.put("intake", pupil_intake);
                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addRequestQueue(stringRequest);
    }
}
