package com.example.video_test;

public class SearchListInfo {

    String searchHistory;

    public SearchListInfo(){

    }

    public SearchListInfo(String searchHistory) {
        this.searchHistory = searchHistory;
    }

    public String getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(String searchHistory) {
        this.searchHistory = searchHistory;
    }
}
