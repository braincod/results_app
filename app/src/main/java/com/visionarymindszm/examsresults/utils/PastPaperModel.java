package com.visionarymindszm.examsresults.utils;

public class PastPaperModel {

    private String paper_name;
    private String paper_year;
    private String pp_id;
    private String paper_url;

    public PastPaperModel(String paper_name, String paper_year, String pp_id, String paper_url) {
        this.paper_name = paper_name;
        this.paper_year = paper_year;
        this.pp_id = pp_id;
        this.paper_url = paper_url;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public String getPaper_year() {
        return paper_year;
    }

    public String getPp_id() {
        return pp_id;
    }

    public String getPaper_url() {
        return paper_url;
    }
}
