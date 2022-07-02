package com.example.demo.service.task;

import com.example.demo.vo.ResultVO;
import com.example.demo.vo.task.DocVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocService {
    ResultVO<DocVO> upLoadDoc(Integer taskId, MultipartFile multipartFile) throws IOException;
    List<DocVO> getDoc(Integer taskID);
}
