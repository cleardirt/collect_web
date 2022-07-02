package com.example.demo.mapperservice.task;

import com.example.demo.po.task.Rule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Rule record);

    Rule selectByPrimaryKey(Integer id);

    Rule selectByStatus(@Param("p") Boolean p,@Param("s") Boolean s,@Param("t") Boolean t);

    Rule selectCurrentRule();

    List<Rule> selectAll();

    int updateByPrimaryKey(Rule record);
}