package com.visionarymindszm.examsresults.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visionarymindszm.examsresults.R;
import com.visionarymindszm.examsresults.screens.SearchResultsActivity;
import com.visionarymindszm.examsresults.screens.ViewPaperActivity;
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

public class Home extends Fragment {
    RecyclerView recyclerView;
    public static final String PAST_PAPER_KEY_ID = "pp_id";
    public static final String PAST_PAPER_KEY_NAME = "paper_name";
    public static final String PAST_PAPER_KEY_YEAR = "paper_year";
    public static final String PAST_PAPER_KEY_URL = "paper_url";
    ConstraintLayout home_fragment;
    private List<PastPaperModel> pastPaperModelList;
    PastPaperAdapter.RecyclerViewClickListener listener;
    EditText past_paper_search;
    private String TAG = "HomeFragment";
    public static final String SEARCH_QUERY = "query";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_exam);
        home_fragment = view.findViewById(R.id.home_fragment);
        past_paper_search = view.findViewById(R.id.past_paper_search);
        past_paper_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(past_paper_search.getText().toString()))
                    past_paper_search.setError("Search query is required");
                else{
                    Intent intent = new Intent(getContext(), SearchResultsActivity.class);
                    intent.putExtra(SEARCH_QUERY, past_paper_search.getText().toString());
                    startActivity(intent);
                    past_paper_search.clearFocus();
                }

            }
        });

        // open the PDF
        listener = new PastPaperAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent intent = new Intent(getContext(), ViewPaperActivity.class);
                intent.putExtra(Utils.PDF_URL, pastPaperModelList.get(position).getPaper_url());
                intent.putExtra(Utils.PDF_NAME, pastPaperModelList.get(position).getPaper_name());
                intent.putExtra(Utils.PDF_YEAR, pastPaperModelList.get(position).getPaper_year());
                startActivity(intent);

            }
        };


        populateRecycler();
        return  view;
    }

    private void populateRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.GET_TOP_TEN_PAST_PAPER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "LOADING RECYCLERVIEW");

                        try {
                            JSONObject jsonObject =  new JSONObject(response);

                            if (jsonObject.optString("error").equals("false")){
                                pastPaperModelList = new ArrayList<>();

                                JSONArray resultData =  jsonObject.getJSONArray("top_past_papers");

                                for (int i=0; i < resultData.length(); i++){
                                    JSONObject pastPaperData = resultData.getJSONObject(i);

                                    pastPaperModelList.add(new PastPaperModel(pastPaperData.getString(PAST_PAPER_KEY_NAME),
                                                            pastPaperData.getString(PAST_PAPER_KEY_YEAR),
                                                            pastPaperData.getString(PAST_PAPER_KEY_ID),
                                                             pastPaperData.getString(PAST_PAPER_KEY_URL)
                                                            ));
                                }

                                generateDataList(pastPaperModelList);
                            }

                        }catch (JSONException e){
                            Log.d(TAG,"error", e);
                            Utils.showSnackBar(home_fragment, "Error query the data from the server.", -1);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"error", error);
                        Utils.showSnackBar(home_fragment, "Error query the data from the server.", -1);
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        // request queue
        //  require context ensures that
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        requestQueue.add(stringRequest);
    }

    private void generateDataList(List<PastPaperModel> pastPaperModelList) {
        PastPaperAdapter adapter = new PastPaperAdapter(pastPaperModelList, getContext(), listener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
