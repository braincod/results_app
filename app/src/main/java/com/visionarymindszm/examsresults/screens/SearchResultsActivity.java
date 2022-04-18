package com.visionarymindszm.examsresults.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visionarymindszm.examsresults.R;
import com.visionarymindszm.examsresults.utils.PastPaperAdapter;
import com.visionarymindszm.examsresults.utils.PastPaperModel;
import com.visionarymindszm.examsresults.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.visionarymindszm.examsresults.fragments.Home.SEARCH_QUERY;

public class SearchResultsActivity extends AppCompatActivity {
    TextView search_results, found_number;
    RecyclerView result_search_recycler;
    ConstraintLayout search_layout;
    public static final String PAST_PAPER_KEY_ID = "pp_id";
    public static final String PAST_PAPER_KEY_NAME = "paper_name";
    public static final String PAST_PAPER_KEY_YEAR = "paper_year";
    public static final String PAST_PAPER_KEY_URL = "paper_url";
    private List<PastPaperModel> pastPaperModelList;
    PastPaperAdapter.RecyclerViewClickListener listener;
    private String TAG = "SearchResultsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_results = findViewById(R.id.search_results);
        String extra = "You searched for "+getIntent().getStringExtra(SEARCH_QUERY);
        search_results.setText(extra);
        found_number = findViewById(R.id.found_number);
        result_search_recycler = findViewById(R.id.result_search_recycler);
        search_layout = findViewById(R.id.search_layout);
        found_number.setText("Loading  data");
        populateRecycler(getIntent().getStringExtra(SEARCH_QUERY));

        listener = new PastPaperAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), ViewPaperActivity.class);
                intent.putExtra(Utils.PDF_URL, pastPaperModelList.get(position).getPaper_url());
                intent.putExtra(Utils.PDF_NAME, pastPaperModelList.get(position).getPaper_name());
                intent.putExtra(Utils.PDF_YEAR, pastPaperModelList.get(position).getPaper_year());
                startActivity(intent);

            }
        };
    }

    private void populateRecycler(final String searchQuery) {
        result_search_recycler.setLayoutManager(new LinearLayoutManager(this));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEARCH_PAPER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "LOADING RECYCLERVIEW");

                        try {
                            JSONObject jsonObject =  new JSONObject(response);

                            if (jsonObject.optString("error").equals("false")){
                                pastPaperModelList = new ArrayList<>();

                                JSONArray resultData =  jsonObject.getJSONArray("search_results");
                                found_number.setText("Matches found "+resultData.length());

                                for (int i=0; i < resultData.length(); i++){
                                    JSONObject pastPaperData = resultData.getJSONObject(i);

                                    pastPaperModelList.add(new PastPaperModel(pastPaperData.getString(PAST_PAPER_KEY_NAME),
                                            pastPaperData.getString(PAST_PAPER_KEY_YEAR),
                                            pastPaperData.getString(PAST_PAPER_KEY_ID),
                                            pastPaperData.getString(PAST_PAPER_KEY_URL)
                                    ));
                                }

                                generateDataList(pastPaperModelList);
                            } else {
                                Log.d(TAG, ""+jsonObject);
                                found_number.setText("Matches found 0");
                            }

                        }catch (JSONException e){

                            Log.d(TAG,"error", e);
                            Utils.showSnackBar(search_layout, "Error query the data from the server.", -1);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"error", error);
                        Utils.showSnackBar(search_layout, "Error query the data from the server.", -1);
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("searchQuery", searchQuery);
                return params;
            }
        };
        // request queue
        //  require context ensures that
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    private void generateDataList(List<PastPaperModel> pastPaperModelList) {
        PastPaperAdapter adapter = new PastPaperAdapter(pastPaperModelList, this, listener);
        adapter.notifyDataSetChanged();
        result_search_recycler.setAdapter(adapter);
    }

}