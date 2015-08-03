package com.zenika.poc.hdp.movie_library.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.poc.hdp.movie_library.AppConfig;
import com.zenika.poc.hdp.movie_library.model.Recommendation;
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
public class RecommendationRepositoryImpl implements RecommendationRepository {

    @Autowired
    private Client client;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public List<Recommendation> getByUser(String user) throws IOException {
        SearchResponse response = this.client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.RECOMMENDATION)
                .setQuery(
                        QueryBuilders.termQuery("user", user)
                ).execute().actionGet();

        List<Recommendation> recommendations = new ArrayList<>();

        for (SearchHit hit : response.getHits().getHits()) {
            recommendations.add(this.mapper.readValue(hit.getSourceAsString(), Recommendation.class));
        }

        return recommendations;
    }

}
