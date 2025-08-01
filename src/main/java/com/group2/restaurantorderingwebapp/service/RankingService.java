package com.group2.restaurantorderingwebapp.service;

import com.group2.restaurantorderingwebapp.dto.request.RankingRequest;
import com.group2.restaurantorderingwebapp.dto.response.PageCustom;
import com.group2.restaurantorderingwebapp.dto.response.RankingAnalysisResponse;
import com.group2.restaurantorderingwebapp.dto.response.RankingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RankingService {
    RankingResponse addRanking (RankingRequest rankingRequest);

    RankingResponse updateRanking(Long id, RankingRequest rankingRequest);

    String deleteRanking(Long id);

    PageCustom<RankingResponse> getAllRanking(Pageable pageable);


    RankingResponse getRankingById(Long id);

    PageCustom<RankingResponse> getRankingByDishId(Long dishId,Pageable pageable);

    RankingAnalysisResponse getRankingAnalysis(Long dishId);

    PageCustom<RankingResponse> getRankingByUserId(Long dishId, Long userId, Pageable pageable);

    PageCustom<RankingResponse> getRankingByStar(Long dishId, int rankingStars, Pageable pageable);
}
