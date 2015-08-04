package com.zenika.poc.hdp.movie_library.model;

import javax.validation.constraints.NotNull;

public class RatingWithTitle {

    @NotNull
    private String user;
    @NotNull
    private String movie;
    @NotNull
    private String title;
    @NotNull
    private Double mark;

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
