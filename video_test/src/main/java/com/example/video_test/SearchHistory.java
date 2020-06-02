package com.example.video_test;

import org.litepal.crud.LitePalSupport;

public class SearchHistory extends LitePalSupport {
    private int id;
    private String account;
    private String search;
    public SearchHistory(){

    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
