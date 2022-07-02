package com.example.demo.controller.user;

import com.example.demo.po.user.User;
import com.example.demo.service.user.UserService;
import com.example.demo.util.Constant;
import com.example.demo.util.jwt.JWTUtil;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.user.AchievementVO;
import com.example.demo.vo.user.UserFormVO;
import com.example.demo.vo.user.UserVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public ResultVO<UserVO> register(@RequestBody UserVO user){
        return userService.userRegister(user);
    }

    @PostMapping("/login")
    public ResultVO<UserFormVO> login(@RequestBody UserFormVO userFormVO){
        return userService.userLogin(userFormVO.getPhone(), userFormVO.getUserpass());
    }

    @GetMapping("/{uid}")
    public UserVO getUser(@PathVariable Integer uid){
        return userService.getUser(uid);
    }

    @GetMapping("/all/{page}")
    public PageInfo<UserVO> getAll(@RequestParam(required = false, defaultValue = "") Integer taskID, @PathVariable Integer page){
        return userService.getAll(taskID,page, Constant.Task_PAGE_SIZE);
    }

    @GetMapping("/achievement/{uid}")
    public List<AchievementVO> getAchievement(@PathVariable Integer uid){
        return userService.getAchievement(uid);
    }

    @RequestMapping("/hello")
    public String sayHello(){
        return userService.userLogin("1","1").getData().getUsername();
    }

    @RequestMapping("/hello2")
    public String sayHello2(){
        return "hello,docker";
    }
}