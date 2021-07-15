package com.ncgtelevision.net.search_screen.search_model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Result implements Serializable {

    @SerializedName("actors")
    private List<Object> mActors;
    @SerializedName("directors")
    private List<Object> mDirectors;
    @SerializedName("genres")
    private List<Genre> mGenres;
    @SerializedName("tags")
    private List<Object> mTags;
    @SerializedName("videos")
    private List<Video> mVideos;

    public List<Object> getActors() {
        return mActors;
    }

    public void setActors(List<Object> actors) {
        mActors = actors;
    }

    public List<Object> getDirectors() {
        return mDirectors;
    }

    public void setDirectors(List<Object> directors) {
        mDirectors = directors;
    }

    public List<Genre> getGenres() {
        return mGenres;
    }

    public void setGenres(List<Genre> genres) {
        mGenres = genres;
    }

    public List<Object> getTags() {
        return mTags;
    }

    public void setTags(List<Object> tags) {
        mTags = tags;
    }

    public List<Video> getVideos() {
        return mVideos;
    }

    public void setVideos(List<Video> videos) {
        mVideos = videos;
    }

}
