package com.visionarymindszm.examsresults.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.visionarymindszm.examsresults.screens.ViewPapersCategoryActivity;
import com.visionarymindszm.examsresults.utils.PaperCategoryAdapter;
import com.visionarymindszm.examsresults.utils.PaperCategoryModel;
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
    public static final String CATE_PAPER_KEY_NAME = "paperName";
    public static final String PAPER_KEY_COUNT = "paperCount";
    public static final String PAST_PAPER_KEY_URL = "paper_url";
    ConstraintLayout home_fragment;
    private List<PaperCategoryModel> pastPaperModelList;
    PaperCategoryAdapter.RecyclerViewClickListener listener;
    EditText past_paper_search;
    private final String TAG = "HomeFragment";
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
        listener = (view1, position) -> {
            Intent intent = new Intent(getContext(), ViewPapersCategoryActivity.class);
            intent.putExtra(Utils.PAPER_CATEGORY_EXTRA, pastPaperModelList.get(position).getPaperCategoryName());
            startActivity(intent);

        };


        populateRecycler();
        return  view;
    }

    private void populateRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.PAPER_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "LOADING RECYCLERVIEW");

                        try {
                            JSONObject jsonObject =  new JSONObject(response);

                            if (jsonObject.optString("error").equals("false")){
                                pastPaperModelList = new ArrayList<>();

                                JSONArray resultData =  jsonObject.getJSONArray("paperCategory");

                                for (int i=0; i < resultData.length(); i++){
                                    JSONObject pastPaperData = resultData.getJSONObject(i);

                                    pastPaperModelList.add(new PaperCategoryModel(pastPaperData.getString(CATE_PAPER_KEY_NAME),
                                                            pastPaperData.getInt(PAPER_KEY_COUNT)
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

    private void generateDataList(List<PaperCategoryModel> pastPaperModelList) {
        PaperCategoryAdapter adapter = new PaperCategoryAdapter(pastPaperModelList, getContext(), listener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
