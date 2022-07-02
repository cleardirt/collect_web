package com.example.demo.vo.user;

import com.example.demo.po.user.UserRating;
import lombok.Data;

@Data
public class UserRatingVO {
    private Integer id;

    private Integer reportId;

    private Integer userId;

    private Integer rating;

    private String comment;

    public UserRatingVO(){}

    public UserRatingVO(UserRating userRating){
        this.id=userRating.getId();
        this.rating=userRating.getRating();
        this.reportId=userRating.getReportId();
        this.userId=userRating.getUserId();
        this.comment=userRating.getComment();
    }
}
