package com.visionarymindszm.examsresults.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

public class ViewPapersCategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static final String PAST_PAPER_KEY_ID = "pp_id";
    public static final String PAST_PAPER_KEY_NAME = "paper_name";
    public static final String PAST_PAPER_KEY_YEAR = "paper_year";
    public static final String PAST_PAPER_KEY_URL = "paper_url";
    private String paperCategory;
    private ConstraintLayout viewPaperCategoryLayout;
    private List<PastPaperModel> pastPaperModelList;
    private PastPaperAdapter adapter;
    PastPaperAdapter.RecyclerViewClickListener listener;
    private final String TAG = "ViewScreen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_papers_category);
        paperCategory = getIntent().getStringExtra(Utils.PAPER_CATEGORY_EXTRA);
        Log.d(TAG, paperCategory+" "+getIntent().getStringExtra(Utils.PAPER_CATEGORY_EXTRA));
        recyclerView = findViewById(R.id.recyclerPaper);

        viewPaperCategoryLayout = findViewById(R.id.viewPaperCategoryLayout);
//        past_paper_search = findViewById(R.id.past_paper_search);
//        past_paper_search.setOnClickListener(view -> {
//            if (TextUtils.isEmpty(past_paper_search.getText().toString()))
//                past_paper_search.setError("Search query is required");
//            else{
//                Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
//                intent.putExtra(SEARCH_QUERY, past_paper_search.getText().toString());
//                startActivity(intent);
//                past_paper_search.clearFocus();
//            }
//
//        });

        // open the PDF
        listener = (view, position) -> {
            Intent intent = new Intent(getApplicationContext(), ViewPaperActivity.class);
            intent.putExtra(Utils.PDF_URL, pastPaperModelList.get(position).getPaper_url());
            intent.putExtra(Utils.PDF_NAME, pastPaperModelList.get(position).getPaper_name());
            intent.putExtra(Utils.PDF_YEAR, pastPaperModelList.get(position).getPaper_year());
            startActivity(intent);

        };


        populateRecycler();
    }

    private void populateRecycler() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.PAPER_BASED_CATEGORY,
                response -> {
                    Log.d(TAG, "LOADING RECYCLERVIEW");

                    try {
                        JSONObject jsonObject =  new JSONObject(response);

                        if (jsonObject.optString("error").equals("false")){
                            pastPaperModelList = new ArrayList<>();

                            JSONArray resultData =  jsonObject.getJSONArray("pastPapers");

                            for (int i=0; i < resultData.length(); i++){
                                JSONObject pastPaperData = resultData.getJSONObject(i);

                                pastPaperModelList.add(new PastPaperModel(pastPaperData.getString(PAST_PAPER_KEY_NAME),
                                        pastPaperData.getString(PAST_PAPER_KEY_YEAR),
                                        pastPaperData.getString(PAST_PAPER_KEY_ID),
                                        pastPaperData.getString(PAST_PAPER_KEY_URL)));
                            }

                            generateDataList(pastPaperModelList);
                        }else{
                            Log.d(TAG," Failed to pull data."+ jsonObject.toString());
                            Utils.showSnackBar(viewPaperCategoryLayout, "Failed to pull data", -1);

                        }

                    }catch (JSONException e){
                        Log.d(TAG,"error", e);
                        Utils.showSnackBar(viewPaperCategoryLayout, "Error query the data from the server.", -1);
                    }
                },
                error -> {
                    Log.d(TAG,"error", error);
                    Utils.showSnackBar(viewPaperCategoryLayout, "Error query the data from the server.", -1);
                }

        ){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("paperName", paperCategory);
                return params;
            }
        };
        // request queue
        //  require context ensures that
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(stringRequest);
    }

    private void generateDataList(List<PastPaperModel> pastPaperModelList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.setHasFixedSize(true);
        adapter = new PastPaperAdapter(pastPaperModelList, getApplicationContext(), listener);

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}