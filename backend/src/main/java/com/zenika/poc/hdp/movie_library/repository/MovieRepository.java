package com.zenika.poc.hdp.movie_library.repository;

import com.zenika.poc.hdp.movie_library.model.Movie;

import java.io.IOException;
import java.util.List;

public interface MovieRepository {

    Movie get(String id) throws IOException;

    List<Movie> search(String request) throws IOException;

}
