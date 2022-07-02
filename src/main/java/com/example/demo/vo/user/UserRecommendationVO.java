package com.example.demo.vo.user;

import com.example.demo.po.user.UserRecommendation;
import lombok.Data;

@Data
public class UserRecommendationVO {
    private Integer id;

    private Integer uid;

    private String recommendList;

    public UserRecommendationVO(){}

    public UserRecommendationVO(UserRecommendation userRecommendation){
        this.id=userRecommendation.getId();
        this.uid=userRecommendation.getUid();
        this.recommendList=userRecommendation.getRecommendList();
    }
}
