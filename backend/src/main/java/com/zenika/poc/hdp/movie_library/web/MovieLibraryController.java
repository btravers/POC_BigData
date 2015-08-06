package com.zenika.poc.hdp.movie_library.web;

import com.zenika.poc.hdp.movie_library.exception.MovieLibraryException;
import com.zenika.poc.hdp.movie_library.model.*;
import com.zenika.poc.hdp.movie_library.service.MovieLibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class MovieLibraryController {
    private static final Logger logger = LoggerFactory.getLogger(MovieLibraryController.class);

    @Autowired
    private MovieLibraryService movieLibraryService;


    @RequestMapping(value = "/movie", method = RequestMethod.GET)
    @ResponseBody
    public Movie getMovie(@RequestParam(value = "id", required = true) String id) throws IOException, MovieLibraryException {
        logger.info("GET /movie?id=" + id);
        return this.movieLibraryService.getMovie(id);
    }

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    @ResponseBody
    public List<Movie> searchMovies(@RequestParam(value = "request", required = true) String request) throws IOException {
        logger.info("GET /movies?request=" + request);
        return this.movieLibraryService.searchMovies(request);
    }

    @RequestMapping(value = "/recommendations", method = RequestMethod.GET)
    @ResponseBody
    public List<RecommendationWithTitle> getRecommendationsByUser(@RequestParam(value = "user", required = true) String user) throws IOException, MovieLibraryException {
        logger.info("GET /recommendations?user=" + user);
        return this.movieLibraryService.getRecommendationsByUser(user);
    }

    @RequestMapping(value = "/rating", method = RequestMethod.GET)
    @ResponseBody
    public Rating getRating(@RequestParam(value = "user", required = true) String user,
                            @RequestParam(value = "movie", required = true) String movie) throws IOException, MovieLibraryException {
        logger.info("GET /rating?user=" + user +"&movie=" + movie);
        return this.movieLibraryService.getRating(user, movie);
    }

    @RequestMapping(value = "/rating", method = RequestMethod.POST)
    @ResponseBody
    public void setRating(@Valid @RequestBody Rating rating) throws IOException, MovieLibraryException {
        logger.info("POST /rating?rating=" + rating);
        this.movieLibraryService.setRating(rating);
    }

    @RequestMapping(value = "/ratings", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, RatingWithTitle> getRatingsByUser(@RequestParam(value = "user", required = true) String user) throws IOException, MovieLibraryException {
        logger.info("GET /ratings?user=" + user);
        return this.movieLibraryService.getRatingsByUser(user);
    }
}
