package com.system.newsapp;

public class News {

    private String mArticleSection;
    private String mArticleTitle;
    private String mArticleAuthor;
    private String mArticleDate;
    private String mUrl;

    public News(String mArticleSection, String mArticleTitle, String mArticleAuthor, String mArticleDate, String mUrl) {
        this.mArticleSection = mArticleSection;
        this.mArticleTitle = mArticleTitle;
        this.mArticleAuthor = mArticleAuthor;
        this.mArticleDate = mArticleDate;
        this.mUrl = mUrl;
    }

    public String getmArticleTitle() {
        return mArticleTitle;
    }

    public String getmArticleAuthor() {
        return mArticleAuthor;
    }

    public String getmArticleDate() {
        return mArticleDate;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmArticleSection() {
        return mArticleSection;
    }
}
