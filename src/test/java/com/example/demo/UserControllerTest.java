package com.example.demo;

import com.example.demo.controller.user.UserController;
import com.example.demo.enums.UserRole;
import com.example.demo.mapperservice.task.TaskProgressMapper;
import com.example.demo.mapperservice.user.UserDeviceMapper;
import com.example.demo.mapperservice.user.UserMapper;
import com.example.demo.mapperservice.user.UserRecommendationMapper;
import com.example.demo.mapperservice.user.UserSelectMapper;
import com.example.demo.po.task.TaskProgress;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.user.UserFormVO;
import com.example.demo.vo.user.UserVO;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MapperScan(basePackages = {"com.example.demo.mapperservice"})
class UserControllerTest {
	@Resource
	UserMapper userMapper;
	@Resource
	UserSelectMapper userSelectMapper;
	@Resource
	UserDeviceMapper userDeviceMapper;
	@Resource
	UserRecommendationMapper userRecommendationMapper;
	@Resource
	TaskProgressMapper taskProgressMapper;

//	@Autowired
	private UserController UserController;

	@BeforeEach
	public void before(@Autowired UserController userController){
		this.UserController = userController;
	}

	@Test
	void LoginTest() {
		UserFormVO userFormVO = new UserFormVO();
		userFormVO.setUserpass("123456");
		userFormVO.setPhone("10112345678");
		ResultVO<UserFormVO> c=UserController.login(userFormVO);
		assert(c.getData().getUsername().equals("asd"));
	}

	@Test
	void RegisterTest() {
		UserVO userVO = new UserVO();
		userVO.setPhone("123456");
		userVO.setUserpass("10112345678");
		userVO.setUsername("tester");
		userVO.setUserRole(UserRole.TESTER.toString());
		UserVO userVO1 = UserController.register(userVO).getData();
		userRecommendationMapper.deleteByUid(userVO1.getId());
		userSelectMapper.deleteByUid(userVO1.getId());
		userDeviceMapper.deleteByUid(userVO1.getId());
		userMapper.deleteByPrimaryKey(userVO1.getId());
	}

	@Test
	void getAllTest() {
		PageInfo<UserVO> c = UserController.getAll(null,1);
		assert(c.getList().get(0).getUsername().equals("asd"));
	}

	@Test
	void getAllTest1() {
		TaskProgress taskProgress = new TaskProgress();
		taskProgress.setTaskId(2);
		taskProgress.setWorkerId(1);
		taskProgressMapper.insert(taskProgress);
		PageInfo<UserVO> c = UserController.getAll(null,1);
		assert(c.getList().get(0).getUsername().equals("asd"));
		taskProgressMapper.deleteByUidAndTaskId(1,2);
	}
}
