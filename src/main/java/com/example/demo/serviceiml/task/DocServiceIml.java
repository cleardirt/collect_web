package com.example.demo.serviceiml.task;

import com.example.demo.mapperservice.task.DocMapper;
import com.example.demo.po.task.Doc;
import com.example.demo.service.task.DocService;
import com.example.demo.util.Constant;
import com.example.demo.util.OssHelper;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.task.DocVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DocServiceIml implements DocService {
    @Resource
    DocMapper docMapper;


    @Override
    public ResultVO<DocVO> upLoadDoc(Integer taskId, MultipartFile multipartFile) throws IOException {
        DocVO docVO=new DocVO();

        docVO.setTaskId(taskId);

        String url= OssHelper.upload(multipartFile,docVO.getTaskId(),"doc");

        docVO.setFileName(multipartFile.getOriginalFilename());

        docVO.setFileSize(String.format("%.2f", (multipartFile.getSize() * 1.0 / 1024 / 1024)) + " MB");

        LocalDateTime localDateTime=LocalDateTime.now();

        Date date = Date.from(localDateTime.atZone( ZoneId.systemDefault()).toInstant());

        docVO.setUploadTime(date);

        docVO.setUrl(url);

        Doc doc=new Doc(docVO);

        docMapper.insert(doc);

        docVO=new DocVO(doc);

        return new ResultVO<>(Constant.REQUEST_SUCCESS,"文件上传成功",docVO);
    }

    @Override
    public List<DocVO> getDoc(Integer taskID) {
        List<Doc> list=docMapper.selectByTaskId(taskID);
        List<DocVO> res=new ArrayList<>();
        for (Doc doc :
                list) {
            res.add(new DocVO(doc));
        }
        return res;
    }
}
