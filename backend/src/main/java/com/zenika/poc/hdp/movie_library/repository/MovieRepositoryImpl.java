package com.zenika.poc.hdp.movie_library.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.poc.hdp.movie_library.AppConfig;
import com.zenika.poc.hdp.movie_library.exception.MovieLibraryException;
import com.zenika.poc.hdp.movie_library.model.Movie;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovieRepositoryImpl implements MovieRepository {

    @Autowired
    private Client client;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public Movie get(String id) throws IOException, MovieLibraryException {
        SearchResponse response = this.client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.MOVIE)
                .setQuery(QueryBuilders.termQuery("id", id))
                .execute().actionGet();

        if (response.getHits().getHits().length != 1) {
            throw new MovieLibraryException("Should contain exactly one movie with id: " + id);
        }

        return this.mapper.readValue(response.getHits().getHits()[0].getSourceAsString(), Movie.class);
    }

    @Override
    public List<Movie> search(String request) throws IOException {
        SearchResponse response = this.client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.MOVIE)
                .setQuery(
                        QueryBuilders.matchQuery("title", request)
                ).execute().actionGet();

        List<Movie> movies = new ArrayList<>();

        for (SearchHit hit : response.getHits().getHits()) {
            movies.add(this.mapper.readValue(hit.getSourceAsString(), Movie.class));
        }

        return movies;
    }
}
