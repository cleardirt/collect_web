package com.example.demo.serviceiml.user;

import com.example.demo.mapperservice.AchievementMapper;
import com.example.demo.mapperservice.user.UserDeviceMapper;
import com.example.demo.mapperservice.user.UserMapper;
import com.example.demo.po.Achievement;
import com.example.demo.po.user.UserDevice;
import com.example.demo.util.Constant;
import com.example.demo.util.PageInfoUtil;
import com.example.demo.util.jwt.JWTUtil;
import com.example.demo.vo.ResultVO;
import com.example.demo.po.user.User;
import com.example.demo.vo.user.AchievementVO;
import com.example.demo.vo.user.UserFormVO;
import com.example.demo.vo.user.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceIml implements com.example.demo.service.user.UserService {
    @Resource
    UserMapper userMapper;
//    @Resource
//    UserDeviceMapper userDeviceMapper;
    @Resource
    AchievementMapper achievementMapper;

    @Override
    public ResultVO<UserVO> userRegister(UserVO vo) {
        String phone = vo.getPhone();
        String uname = vo.getUsername();
        String password = vo.getUserpass();
        if(userMapper.selectByPhone(phone) == null){
            if(StringUtils.hasText(uname) && StringUtils.hasText(password)){
                vo.setCreateTime(new Date());
                vo.setActivity(0);
                vo.setProfessionalAbility(BigDecimal.ZERO);
                vo.setCredit(BigDecimal.TEN);
                vo.setRatingAbility(BigDecimal.ZERO);
                User user = new User(vo);
                userMapper.insert(user);
                System.out.println(new UserVO(user));
                return new ResultVO<>(Constant.REQUEST_SUCCESS, "账号注册成功！", new UserVO(user));
            }else{
                return new ResultVO<>(Constant.REQUEST_FAIL, "用户名或密码不得为空！");
            }
        }
        return new ResultVO<>(Constant.REQUEST_FAIL, "这个手机号已经注册过账号。");
    }

    @Override
    public ResultVO<UserFormVO> userLogin(String phone, String password) {
        User user = userMapper.selectByPhone(phone);
        if(user == null){
            return new ResultVO<>(Constant.REQUEST_FAIL, "这个手机号尚未注册过账号。");
        }else{
            if(!user.getUserpass().equals(password))
                return new ResultVO<>(Constant.REQUEST_FAIL, "账号或密码错误！");
        }
        UserFormVO userFormVO=new UserFormVO(user);
        String token= JWTUtil.getToken(userFormVO);
        userFormVO.setToken(token);
        return new ResultVO<>(Constant.REQUEST_SUCCESS, "账号登陆成功！", userFormVO);
    }

    @Override
    public UserVO getUser(Integer uid) {
        return new UserVO(userMapper.selectByPrimaryKey(uid));
    }

    @Override
    public PageInfo<UserVO> getAll(Integer taskID,Integer currPage, Integer pageSize) {
        if(currPage==null || currPage<1) currPage=1;
        PageHelper.startPage(currPage, pageSize);
        PageInfo<User> po;
        if (taskID==null)
            po = new PageInfo<>(userMapper.selectAll());
        else
            po=new PageInfo<>(userMapper.selectByTaskId(taskID));
        return getUserVOPageInfo(taskID,po);
    }

    @Override
    public List<AchievementVO> getAchievement(Integer uid) {
        List<Achievement> achievements= achievementMapper.selectByUid(uid);
        List<AchievementVO> res=new ArrayList<>();
        for (Achievement achievement : achievements) {
            res.add(new AchievementVO(achievement));
        }
        return res;
    }



    private PageInfo<UserVO> getUserVOPageInfo(Integer taskID, PageInfo<User> po) {
        PageInfo<UserVO> result = PageInfoUtil.convert(po, UserVO.class);
        return result;
    }
}
