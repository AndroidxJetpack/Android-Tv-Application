package com.ncgtelevision.net.search_screen.search_model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Genre {

    @SerializedName("genre_link")
    private String mGenreLink;
    @SerializedName("genre_title")
    private String mGenreTitle;

    public String getGenreLink() {
        return mGenreLink;
    }

    public void setGenreLink(String genreLink) {
        mGenreLink = genreLink;
    }

    public String getGenreTitle() {
        return mGenreTitle;
    }

    public void setGenreTitle(String genreTitle) {
        mGenreTitle = genreTitle;
    }

}
