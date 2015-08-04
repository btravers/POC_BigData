package com.zenika.poc.hdp.movie_library.model;

import javax.validation.constraints.NotNull;

public class Recommendation {

    @NotNull
    private String user;
    @NotNull
    private String movie;
    @NotNull
    private Double mark;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

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
        this.mark = Math.round(mark * 10) / 10.0;
    }

}
