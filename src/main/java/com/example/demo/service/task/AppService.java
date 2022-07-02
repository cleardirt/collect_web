package com.example.demo.service.task;

import com.example.demo.vo.ResultVO;
import com.example.demo.vo.task.AppVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AppService {
    ResultVO<AppVO> upLoadApp(Integer taskId, MultipartFile multipartFile) throws IOException;
    List<AppVO> getApp(Integer taskID);
}
