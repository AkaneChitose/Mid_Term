package com.example.mid_term;

public class DataClass {
    private String dataTitle;
    private String dataDesc;
    private String dataTag;
    private String dataImage;

    public DataClass(String title, String desc, String tag, String imageURL) {
        this.dataTitle = title;
        this.dataDesc = desc;
        this.dataTag = tag;
        this.dataImage = imageURL;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataTag() {
        return dataTag;
    }

    public String getDataImage() {
        return dataImage;
    }

    public DataClass(){

    }
}
