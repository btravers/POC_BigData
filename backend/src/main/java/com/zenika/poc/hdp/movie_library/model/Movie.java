package com.zenika.poc.hdp.movie_library.model;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Movie {

    @NotNull
    private String id;
    @NotNull
    private String title;
    @NotNull
    private List<String> genres;
    @NotNull
    private Double mark;
    @NotNull
    private int nb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = Math.round(mark * 10) / 10.0;
    }
}
