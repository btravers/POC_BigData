package com.zenika.poc.hdp.movie_library.service;

import com.zenika.poc.hdp.movie_library.exception.MovieLibraryException;
import com.zenika.poc.hdp.movie_library.model.*;
import com.zenika.poc.hdp.movie_library.repository.MovieRepository;
import com.zenika.poc.hdp.movie_library.repository.RatingRepository;
import com.zenika.poc.hdp.movie_library.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieLibraryServiceImpl implements MovieLibraryService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public Movie getMovie(String id) throws IOException {
        return this.movieRepository.get(id);
    }

    @Override
    public List<Movie> searchMovies(String request) throws IOException {
        return this.movieRepository.search(request);
    }

    @Override
    public List<RecommendationWithTitle> getRecommendationsByUser(String user) throws IOException {
        List<Recommendation> recommendations =  this.recommendationRepository.getByUser(user);

        List<RecommendationWithTitle> result = new ArrayList<>();
        for (Recommendation recommendation : recommendations) {
            RecommendationWithTitle tmp = new RecommendationWithTitle();
            tmp.setUser(recommendation.getUser());
            tmp.setMovie(recommendation.getMovie());
            tmp.setMark(recommendation.getMark());

            Movie movie = this.movieRepository.get(tmp.getMovie());
            tmp.setTitle(movie.getTitle());

            result.add(tmp);
        }

        return result;
    }

    @Override
    public Rating getRating(String user, String movie) throws IOException, MovieLibraryException {
        return this.ratingRepository.get(user, movie);
    }

    @Override
    public void setRating(Rating rating) throws IOException, MovieLibraryException {
        this.ratingRepository.set(rating);
    }

    @Override
    public List<RatingWithTitle> getRatingsByUser(String user) throws IOException {
        List<Rating> ratings = this.ratingRepository.getByUser(user);

        List<RatingWithTitle> result = new ArrayList<>();
        for (Rating rating : ratings) {
            RatingWithTitle tmp = new RatingWithTitle();
            tmp.setUser(rating.getUser());
            tmp.setMovie(rating.getMovie());
            tmp.setMark(rating.getMark());

            Movie movie = this.movieRepository.get(tmp.getMovie());
            tmp.setTitle(movie.getTitle());

            result.add(tmp);
        }

        return result;
    }

}
