package com.example.demo;

import com.example.demo.controller.task.AppController;
import com.example.demo.mapperservice.task.AppMapper;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.task.AppVO;
import org.junit.jupiter.api.Assertions;
//import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MapperScan(basePackages = {"com.example.demo.mapperservice"})

public abstract class AppControllerTest {
    private AppController appController;

    @Resource
    AppMapper appMapper;

    @BeforeEach
    public void before(@Autowired AppController appController){
        this.appController = appController;
    }
    @Test
    public void upLoad() throws IOException {
        ResultVO<AppVO> resultVO=uploadTxt();
        assert (resultVO.getData().getFileName()!=null);
        System.out.println(resultVO.getData().getId());
        appMapper.deleteByFileName("apptest.txt");
    }

    private ResultVO<AppVO> uploadTxt() throws IOException {
        AppVO appVO=new AppVO();
        appVO.setTaskId(1);
        File file=new File("src/test/java/com/example/demo/apptest.txt");
        return appController.upLoadApp(1, new MockMultipartFile("apptest.txt","apptest.txt","txt",new FileInputStream(file)));
    }

    @Test
    public void getApp() throws IOException {
        uploadTxt();
        List<AppVO> list = appController.getApp(1);
        System.out.println(list.get(0).getFileName());
        assert (list.get(1).getFileName().equals("apptest.txt"));
        appMapper.deleteByFileName("apptest.txt");
    }
}
