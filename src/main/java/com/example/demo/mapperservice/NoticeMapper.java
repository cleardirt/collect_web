package com.example.demo.mapperservice;

import com.example.demo.po.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Notice record);

    Notice selectByPrimaryKey(Integer id);

    List<Notice> selectAll();

    int updateByPrimaryKey(Notice record);

    List<Notice> selectByUidType(@Param("uid") Integer uid,@Param("type") String type);

    List<Notice> selectByUidUnsent(@Param("uid") Integer uid);

    List<Notice> selectByUid(Integer uid);
}