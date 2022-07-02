package com.example.demo.serviceiml.task;

import com.example.demo.mapperservice.task.AppMapper;
import com.example.demo.po.task.App;
import com.example.demo.service.task.AppService;
import com.example.demo.util.Constant;
import com.example.demo.util.OssHelper;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.task.AppVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AppServiceIml implements AppService {
    @Resource
    AppMapper appMapper;

    @Override
    public ResultVO<AppVO> upLoadApp(Integer taskId, MultipartFile multipartFile) throws IOException {
        AppVO appVO=new AppVO();

        appVO.setTaskId(taskId);

        String url= OssHelper.upload(multipartFile,appVO.getTaskId(),"app");

        appVO.setFileName(multipartFile.getOriginalFilename());

        appVO.setFileSize(String.format("%.2f", (multipartFile.getSize() * 1.0 / 1024 / 1024)) + " MB");

        LocalDateTime localDateTime=LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone( ZoneId.systemDefault()).toInstant());

        appVO.setUploadTime(date);

        appVO.setUrl(url);//type暂时用来当url

        App app=new App(appVO);
        appMapper.insert(new App(appVO));
        appVO=new AppVO(app);

        return new ResultVO<>(Constant.REQUEST_SUCCESS,"文件上传成功",appVO);
    }

    @Override
    public List<AppVO> getApp(Integer taskID) {
        List<App> list=appMapper.selectByTaskId(taskID);
        List<AppVO> res=new ArrayList<>();
        for (App app :
                list) {
            res.add(new AppVO(app));
        }
        return res;
    }
}
