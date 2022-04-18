package com.visionarymindszm.examsresults.utils;

public class PaperCategoryModel {

    private String paper_name;
    private int paper_count;
    private String pp_id;
    private String paper_url;

    public PaperCategoryModel(String paper_name, int paper_count, String pp_id, String paper_url) {
        this.paper_name = paper_name;
        this.paper_count = paper_count;
        this.pp_id = pp_id;
        this.paper_url = paper_url;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public int getPaper_year() {
        return paper_count;
    }

    public String getPp_id() {
        return pp_id;
    }

    public String getPaper_url() {
        return paper_url;
    }
}
