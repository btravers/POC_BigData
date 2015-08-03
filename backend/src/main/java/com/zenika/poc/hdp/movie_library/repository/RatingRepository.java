package com.zenika.poc.hdp.movie_library.repository;

import com.zenika.poc.hdp.movie_library.exception.MovieLibraryException;
import com.zenika.poc.hdp.movie_library.model.Rating;

import java.io.IOException;
import java.util.List;

public interface RatingRepository {

    Rating get(String user, String movie) throws MovieLibraryException, IOException;

    void set(Rating rating) throws IOException, MovieLibraryException;

    List<Rating> getByUser(String user) throws IOException;

}
