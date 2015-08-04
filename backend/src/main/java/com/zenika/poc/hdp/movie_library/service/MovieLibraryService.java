package com.zenika.poc.hdp.movie_library.service;

import com.zenika.poc.hdp.movie_library.exception.MovieLibraryException;
import com.zenika.poc.hdp.movie_library.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MovieLibraryService {

    Movie getMovie(String id) throws IOException, MovieLibraryException;

    List<Movie> searchMovies(String request) throws IOException;

    List<RecommendationWithTitle> getRecommendationsByUser(String user) throws IOException, MovieLibraryException;

    Rating getRating(String user, String movie) throws IOException, MovieLibraryException;

    void setRating(Rating rating) throws IOException, MovieLibraryException;

    Map<String, RatingWithTitle> getRatingsByUser(String user) throws IOException, MovieLibraryException;
}
