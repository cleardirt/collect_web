package com.example.demo.service.user;


import com.example.demo.po.Achievement;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.user.AchievementVO;
import com.example.demo.vo.user.UserFormVO;
import com.example.demo.vo.user.UserVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    ResultVO<UserVO> userRegister(UserVO user);
    ResultVO<UserFormVO> userLogin(String phone, String password);
    UserVO getUser(Integer uid);
    PageInfo<UserVO> getAll(Integer taskID, Integer currPage, Integer pageSize);
    List<AchievementVO> getAchievement(Integer uid);
}
