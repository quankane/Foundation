package com.group2.restaurantorderingwebapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankingAnalysisResponse {
    private int rankingCount;
    private double rankingAvg;
    private double rank5;
    private double rank4;
    private double rank3;
    private double rank2;
    private double rank1;
}
