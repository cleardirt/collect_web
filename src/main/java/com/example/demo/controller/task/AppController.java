package com.example.demo.controller.task;

import com.example.demo.service.task.AppService;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.task.AppVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/task/app")
public class AppController {

    @Resource
    AppService appService;

    @PostMapping("/upload")
    public ResultVO<AppVO> upLoadApp(@RequestParam Integer taskId,@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return appService.upLoadApp(taskId,multipartFile);
    }

    @GetMapping("/get/{taskId}")
    public List<AppVO> getApp(@PathVariable Integer taskId){
        return appService.getApp(taskId);
    }

}
