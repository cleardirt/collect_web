package com.example.demo.po.user;

import com.example.demo.vo.user.UserRatingVO;

public class UserRating {
    private Integer id;

    private Integer reportId;

    private Integer userId;

    private Integer rating;

    private String comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public UserRating(){}

    public UserRating(UserRatingVO userRating){
        this.id=userRating.getId();
        this.rating=userRating.getRating();
        this.reportId=userRating.getReportId();
        this.userId=userRating.getUserId();
        this.comment= userRating.getComment();
    }
}