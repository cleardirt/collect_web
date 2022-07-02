package com.example.demo.mapperservice.user;

import com.example.demo.po.user.UserRating;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRatingMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByRUid(@Param("report_id") Integer report_id,@Param("user_id") Integer user_id);

    int insert(UserRating record);

    UserRating selectByPrimaryKey(Integer id);

    List<UserRating> selectAll();

    int updateByPrimaryKey(UserRating record);

    UserRating selectByRidUid(@Param("report_id") Integer reportId, @Param("user_id") Integer testerId);

    List<Integer> selectByUid(Integer user_id);
}