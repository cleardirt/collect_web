package com.example.demo.mapperservice.user;

import com.example.demo.po.user.UserRecommendation;
import java.util.List;

public interface UserRecommendationMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByUid(Integer user_id);

    int insert(UserRecommendation record);

    UserRecommendation selectByPrimaryKey(Integer id);

    UserRecommendation selectByUserId(Integer id);

    List<UserRecommendation> selectAll();

    int updateByPrimaryKey(UserRecommendation record);
}