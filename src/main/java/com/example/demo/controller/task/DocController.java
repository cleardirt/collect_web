package com.example.demo.controller.task;

import com.example.demo.service.task.DocService;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.task.DocVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/task/doc")
public class DocController {

    @Resource
    DocService docService;

    @PostMapping("/upload")
    public ResultVO<DocVO> upLoadApp(@RequestParam Integer taskId,@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return docService.upLoadDoc(taskId,multipartFile);
    }

    @GetMapping("/get/{taskId}")
    public List<DocVO> getDoc(@PathVariable Integer taskId){
        return docService.getDoc(taskId);
    }

}