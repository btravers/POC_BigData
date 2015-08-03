package com.zenika.poc.hdp.movie_library.repository;

import com.zenika.poc.hdp.movie_library.model.Recommendation;

import java.io.IOException;
import java.util.List;

public interface RecommendationRepository {

    List<Recommendation> getByUser(String user) throws IOException;

}
