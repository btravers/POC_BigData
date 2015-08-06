package com.zenika.poc.hdp.movie_library.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.poc.hdp.movie_library.AppConfig;
import com.zenika.poc.hdp.movie_library.exception.MovieLibraryException;
import com.zenika.poc.hdp.movie_library.model.Rating;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RatingRepositoryImpl implements RatingRepository {

    private static final Logger logger = LoggerFactory.getLogger(RatingRepositoryImpl.class);

    @Autowired
    private Client client;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public Rating get(String user, String movie) throws MovieLibraryException, IOException {
        logger.info("Getting rating for user " + user + " and movie " + movie);

        SearchResponse response = this.client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.RATING)
                .setQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.termQuery("user", user))
                                .must(QueryBuilders.termQuery("movie", movie))
                ).execute().actionGet();

        if (response.getHits().getHits().length > 1) {
            throw new MovieLibraryException("Expecting less than one rating per (user, movie)");
        }

        if (response.getHits().getHits().length == 1) {
            return this.mapper.readValue(response.getHits().getHits()[0].getSourceAsString(), Rating.class);
        }

        return null;
    }


    @Override
    public void set(Rating rating) throws IOException, MovieLibraryException {
        logger.info("Creating rating " + this.mapper.writeValueAsString(rating));

        String user = rating.getUser();
        String movie = rating.getMovie();

        SearchResponse response = this.client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.RATING)
                .setQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.termQuery("user", user))
                                .must(QueryBuilders.termQuery("movie", movie))
                ).execute().actionGet();

        if (response.getHits().getHits().length == 1) {
            this.client.prepareDelete(AppConfig.INDEX, AppConfig.RATING, response.getHits().getHits()[0].getId())
                    .execute().actionGet();
        } else if (response.getHits().getHits().length != 0) {
            throw new MovieLibraryException("Expecting less than one rating per (user, movie)");
        }

        this.client.prepareIndex(AppConfig.INDEX, AppConfig.RATING)
                .setSource(this.mapper.writeValueAsString(rating))
                .execute().actionGet();
    }

    @Override
    public List<Rating> getByUser(String user) throws IOException {
        logger.info("Get ratings for user " + user);

        SearchResponse response = this.client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.RATING)
                .setSize(Integer.MAX_VALUE)
                .setQuery(
                        QueryBuilders.termQuery("user", user)
                ).execute().actionGet();

        List<Rating> ratings = new ArrayList<>();

        for (SearchHit hit : response.getHits().getHits()) {
            ratings.add(this.mapper.readValue(hit.getSourceAsString(), Rating.class));
        }

        return ratings;
    }
}
