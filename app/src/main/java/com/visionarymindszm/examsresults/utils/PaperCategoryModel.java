package com.visionarymindszm.examsresults.utils;

public class PaperCategoryModel {

    private final String paperCategoryName;
    private final int paperCount;


    public PaperCategoryModel(String paperCategoryName, int paperCount) {
        this.paperCategoryName = paperCategoryName;
        this.paperCount = paperCount;

    }



    public String getPaperCategoryName() {
        return paperCategoryName;
    }

    public int getPaperCount() {
        return paperCount;
    }
}
