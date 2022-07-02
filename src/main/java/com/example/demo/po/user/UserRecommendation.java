package com.example.demo.po.user;

import com.example.demo.vo.user.UserRecommendationVO;

public class UserRecommendation {
    private Integer id;

    private Integer uid;

    private String recommendList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(String recommendList) {
        this.recommendList = recommendList == null ? null : recommendList.trim();
    }

    public UserRecommendation(){}

    public UserRecommendation(UserRecommendationVO userRecommendation){
        this.id=userRecommendation.getId();
        this.uid=userRecommendation.getUid();
        this.recommendList=userRecommendation.getRecommendList();
    }
}