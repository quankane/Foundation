package com.group2.restaurantorderingwebapp.service.impl;

import com.group2.restaurantorderingwebapp.dto.request.RankingRequest;
import com.group2.restaurantorderingwebapp.dto.response.PageCustom;
import com.group2.restaurantorderingwebapp.dto.response.RankingAnalysisResponse;
import com.group2.restaurantorderingwebapp.dto.response.RankingResponse;
import com.group2.restaurantorderingwebapp.dto.response.UserResponse;
import com.group2.restaurantorderingwebapp.entity.Dish;
import com.group2.restaurantorderingwebapp.entity.Ranking;
import com.group2.restaurantorderingwebapp.entity.User;
import com.group2.restaurantorderingwebapp.exception.ResourceNotFoundException;
import com.group2.restaurantorderingwebapp.repository.DishRepository;
import com.group2.restaurantorderingwebapp.repository.RankingRepository;
import com.group2.restaurantorderingwebapp.repository.UserRepository;
import com.group2.restaurantorderingwebapp.service.RankingService;
import com.group2.restaurantorderingwebapp.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final RedisService redisService;
    private final String KEY = "ranking";


    @Override
    public RankingResponse addRanking(RankingRequest rankingRequest) {
        Ranking ranking = modelMapper.map(rankingRequest, Ranking.class);
        User user = userRepository.findById(rankingRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", rankingRequest.getUserId()));
        Dish dish = dishRepository.findById(rankingRequest.getDishID()).orElseThrow(() -> new ResourceNotFoundException("Dish", "id", rankingRequest.getDishID()));
        ranking.setDish(dish);
        ranking.setUser(user);
        rankingRepository.save(ranking);
        RankingResponse response = modelMapper.map(ranking, RankingResponse.class);
        response.setUser(modelMapper.map(user, UserResponse.class));
        redisService.deleteAll(KEY);
//        redisService.deleteAll("order");
        return response;
    }


    @Override
    public RankingResponse updateRanking(Long rankingId, RankingRequest rankingRequest) {
        Ranking ranking = rankingRepository.findById(rankingId).orElseThrow(() -> new ResourceNotFoundException("Ranking", "id", rankingId));
        ranking.setComment(rankingRequest.getComment());
        ranking.setRankingStars(rankingRequest.getRankingStars());
        rankingRepository.save(ranking);
        RankingResponse response = modelMapper.map(ranking, RankingResponse.class);
        response.setUser(modelMapper.map(ranking.getUser(), UserResponse.class));
        redisService.deleteAll(KEY);
        return response;
    }

    @Override
    public String deleteRanking(Long rankingId) {
        Ranking ranking = rankingRepository.findById(rankingId).orElseThrow(() -> new ResourceNotFoundException("Ranking", "id", rankingId));
        rankingRepository.delete(ranking);
        redisService.deleteAll(KEY);
        return "Ranking deleted successfully";
    }

    @Override
    public PageCustom<RankingResponse> getAllRanking(Pageable pageable) {
        String field = "allRanking" + pageable.toString();
        var json = redisService.getHash(KEY, field);
        if (json == null) {

            Page<Ranking> page = rankingRepository.findAll(pageable);
            PageCustom<RankingResponse> pageCustom = PageCustom.<RankingResponse>builder()
                    .pageNo(page.getNumber() + 1)
                    .pageSize(page.getSize())
                    .totalPages(page.getTotalPages())
                    .pageContent(page.getContent().stream().map(result -> modelMapper.map(result, RankingResponse.class)).toList())
                    .build();
            redisService.setHashRedis(KEY, field, redisService.convertToJson(pageCustom));
            return pageCustom;
        }
        return (PageCustom<RankingResponse>) redisService.convertToObject((String) json, PageCustom.class);
    }

    @Override
    public RankingResponse getRankingById(Long rankingId) {
        String field = "rankingById:" + rankingId;
        var json = redisService.getHash(KEY, field);
        if (json == null) {

            Ranking ranking = rankingRepository.findById(rankingId).orElseThrow(() -> new ResourceNotFoundException("Ranking", "id", rankingId));
            RankingResponse response = modelMapper.map(ranking, RankingResponse.class);
            response.setUser(modelMapper.map(ranking.getUser(), UserResponse.class));
            redisService.setHashRedis(KEY, field, redisService.convertToJson(response));
            return response;
        }
        return redisService.convertToObject((String) json, RankingResponse.class);
    }

    @Override
    public PageCustom<RankingResponse> getRankingByDishId(Long dishId, Pageable pageable) {
        String field = "rankingByDishId:" + dishId + pageable.toString();
        var json = redisService.getHash(KEY, field);
        if (json == null) {

            Page<Ranking> ranking = rankingRepository.findAllByDishId(dishId, pageable);
            PageCustom<RankingResponse> pageCustom = PageCustom.<RankingResponse>builder()
                    .pageNo(ranking.getNumber() + 1)
                    .pageSize(ranking.getSize())
                    .totalPages(ranking.getTotalPages())
                    .pageContent(ranking.getContent().stream().map(result -> modelMapper.map(result, RankingResponse.class)).toList())
                    .build();
            redisService.setHashRedis(KEY, field, redisService.convertToJson(pageCustom));
            return pageCustom;
        }
        return (PageCustom<RankingResponse>) redisService.convertToObject((String) json, PageCustom.class);
    }

    @Override
    public RankingAnalysisResponse getRankingAnalysis(Long dishId) {
        String field = "rankingAnalysis:" + dishId;
        var json = redisService.getHash(KEY, field);
        if (json == null) {
            Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new ResourceNotFoundException("Dish", "id", dishId));
            List<Ranking> rankings = rankingRepository.findAllByDish(dish);

            double rankingAvg = 0;
            double rank5 = 0;
            double rank4 = 0;
            double rank3 = 0;
            double rank2 = 0;
            double rank1 = 0;
            for (Ranking ranking : rankings) {
                rankingAvg += ranking.getRankingStars();
                if (ranking.getRankingStars() == 5) {
                    rank5++;
                }
                if (ranking.getRankingStars() == 4) {
                    rank4++;
                }
                if (ranking.getRankingStars() == 3) {
                    rank3++;
                }
                if (ranking.getRankingStars() == 2) {
                    rank2++;
                }
                if (ranking.getRankingStars() == 1) {
                    rank1++;
                }
            }


            RankingAnalysisResponse response = new RankingAnalysisResponse();
            if (rankings.size()!=0){
                response.setRankingAvg(rankingAvg / rankings.size());
                response.setRank5((rank5 / rankings.size()) * 100);
                response.setRank4((rank4 / rankings.size()) * 100);
                response.setRank3((rank3 / rankings.size()) * 100);
                response.setRank2((rank2 / rankings.size()) * 100);
                response.setRank1((rank1 / rankings.size()) * 100);
            }
            response.setRankingCount(rankings.size());
            redisService.setHashRedis(KEY, field, redisService.convertToJson(response), 60000L);
            return response;
        }
        return redisService.convertToObject((String) json, RankingAnalysisResponse.class);
    }

    @Override
    public PageCustom<RankingResponse> getRankingByUserId(Long dishId, Long userId, Pageable pageable) {
        String field = "rankingByUserId:" + userId + pageable.toString();
        var json = redisService.getHash(KEY, field);
        if (json == null) {

            Page<Ranking> ranking = rankingRepository.findAllByDishIdUserId(dishId, userId, pageable);
            PageCustom<RankingResponse> pageCustom = PageCustom.<RankingResponse>builder()
                    .pageNo(ranking.getNumber() + 1)
                    .pageSize(ranking.getSize())
                    .totalPages(ranking.getTotalPages())
                    .pageContent(ranking.getContent().stream().map(result -> modelMapper.map(result, RankingResponse.class)).toList())
                    .build();
            redisService.setHashRedis(KEY, field, redisService.convertToJson(pageCustom));
            return pageCustom;
        }
        return (PageCustom<RankingResponse>) redisService.convertToObject((String) json, PageCustom.class);
    }


    @Override
    public PageCustom<RankingResponse> getRankingByStar(Long dishId, int rankingStars, Pageable pageable) {
        String field = "rankingByRankingStar:" + rankingStars + pageable.toString();
        var json = redisService.getHash(KEY, field);
        if (json == null) {

            Page<Ranking> ranking = rankingRepository.findAllByDishIdRankingStars(dishId, rankingStars, pageable);
            PageCustom<RankingResponse> pageCustom = PageCustom.<RankingResponse>builder()
                    .pageNo(ranking.getNumber() + 1)
                    .pageSize(ranking.getSize())
                    .totalPages(ranking.getTotalPages())
                    .pageContent(ranking.getContent().stream().map(result -> modelMapper.map(result, RankingResponse.class)).toList())
                    .build();
            redisService.setHashRedis(KEY, field, redisService.convertToJson(pageCustom));
            return pageCustom;
        }

        return (PageCustom<RankingResponse>) redisService.convertToObject((String) json, PageCustom.class);
    }


}
