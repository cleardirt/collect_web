package com.example.demo;

import com.example.demo.mapperservice.report.BugScreenshotMapper;
import com.example.demo.serviceiml.report.ReportServiceImpl;
import com.example.demo.vo.report.BugScreenshotVO;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MapperScan(basePackages = {"com.example.demo.mapperservice"})
public class PicUploadTest {
    @Autowired
    private ReportServiceImpl reportService;
    @Resource
    BugScreenshotMapper bugScreenshotMapper;

    @Test
    public void uploadPic() {
        BugScreenshotVO bugScreenshotVO = uploadLocalPic();
        bugScreenshotMapper.deleteByPrimaryKey(bugScreenshotVO.getId());
    }

    public BugScreenshotVO uploadLocalPic(){
        BugScreenshotVO bugScreenshotVO = null;
        File file = new File(
                "src/test/java/com/example/demo/GreatCormorants_ZH-CN6811149253_1920x1080.jpg");
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        MultipartFile mulFile = null;
        try {
            mulFile = new MockMultipartFile(
                    "butterflies.jpg", //文件名
                    "butterflies.jpg", //originalName 相当于上传文件在客户机上的文件名
                    ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
                    fileInputStream //文件流
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bugScreenshotVO = reportService.uploadShot(mulFile).getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bugScreenshotVO;
    }
}
