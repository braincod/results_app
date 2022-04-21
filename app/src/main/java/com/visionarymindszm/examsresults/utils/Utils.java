package com.visionarymindszm.examsresults.utils;

import android.view.View;
import com.google.android.material.snackbar.Snackbar;

/**
 * Utils is a class that ho
 */

public class Utils {
    // use this for physical device, ensure you on the same network
    // check for the current IP address for the computer (ifconfig or ip addr linux and ipconfig windows)
//    public static final String  ROOT_URL = "http://192.168.139.169/exam_api/";
    public static final String  ROOT_URL = "http://10.0.2.2/api_exam/"; // use this for the emulator
    public static final String LOGIN_URL = ROOT_URL+"?apicall=login";
    public static final String GET_TOP_TEN_PAST_PAPER = ROOT_URL+"?apicall=topTen";
    public static final String PAPER_CATEGORY = ROOT_URL+"?apicall=paperCategory";
    public static final String PAPER_BASED_CATEGORY = ROOT_URL+"?apicall=paperBasedOnCategory";
    public static final String SEARCH_PAPER = ROOT_URL +"?apicall=searchPastPaper";
    public static final String GET_RESULTS = ROOT_URL + "?apicall=check_results";
    // Extra
    public static final String PDF_URL = "pdf_url";
    public static final String PDF_NAME = "pdf_name";
    public static final String PDF_YEAR = "pdf_year";
    public static final String PAPER_CATEGORY_EXTRA = "paperCategory";

    /**
     *
     * @param view_name: Layout to render the snackBack on
     * @param message: The message to be displayed
     * @param length: Length of the message => {-2, -1, 0} valid parameters
     */
    public static void showSnackBar(View view_name, String message, int length){
        if (length == -1) {
            Snackbar.make(view_name, message, Snackbar.LENGTH_SHORT).show();
        }else if (length == 0){
            Snackbar.make(view_name, message, Snackbar.LENGTH_LONG).show();
        }else if (length == -2){
            Snackbar.make(view_name, message, Snackbar.LENGTH_INDEFINITE).show();
        }
    }
}
