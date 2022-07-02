package com.example.demo.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.example.demo.util.ComparisonIml.FingerPrint;
import com.example.demo.util.Comparson.ImageComparisonStrategies;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.UUID;

public class OssHelper {
    static ImageComparisonStrategies imageComparisonStrategies = new FingerPrint();

    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    static String endpoint = "https://oss-cn-heyuan.aliyuncs.com";
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    static String accessKeyId = "LTAI5tJ6hDmXKCDTYuG7Skrf";
    static String accessKeySecret = "1DOfWoj5szh3hIgzLksRmDeaEPw7Sf";
    // 填写Bucket名称，例如examplebucket。
    static String bucketName = "bonheur";
    // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。

    static byte[] fingerPrint = new byte[32];
    // 创建OSSClient实例。

    public static BufferedImage inputStream2BufferedImage(InputStream inputStream){
        BufferedImage bufferedImage = null;
        try{
            bufferedImage = javax.imageio.ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  bufferedImage;
    }

    public static String upload(MultipartFile multipartFile,Integer taskId,String fileType) throws IOException {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        InputStream multipartFileInputStream = multipartFile.getInputStream();
        String objectName=taskId+"/"+fileType+"/"+UUID.randomUUID()+ LocalDateTime.now()+multipartFile.getOriginalFilename();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,objectName , multipartFileInputStream);
        ossClient.putObject(putObjectRequest);
        objectName= URLEncoder.encode(objectName,"UTF-8");
        String url="https://"+bucketName+"."+"oss-cn-heyuan.aliyuncs.com"+"/"+objectName;
        return url;
    }

    public static String uploadPic(MultipartFile multipartFile,String fileType) throws IOException {
        OSS oss = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        InputStream inputStream=multipartFile.getInputStream();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len=inputStream.read(buffer))>-1){
            byteArrayOutputStream.write(buffer,0,len);
        }
        byteArrayOutputStream.flush();
        InputStream inputStream1 = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        InputStream inputStream2 = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());


        String objectName = "bugscreenshots"+"/"+fileType+"/"+UUID.randomUUID()+LocalDateTime.now()
                +multipartFile.getOriginalFilename();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                objectName,inputStream1);
        oss.putObject(putObjectRequest);
    //    System.out.println(objectName);
        try {
            objectName = URLEncoder.encode(objectName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "https://"+bucketName+"."+"oss-cn-heyuan.aliyuncs.com"+"/"+objectName;
        ;
        BufferedImage bufferedImage = inputStream2BufferedImage(inputStream2);
        fingerPrint = imageComparisonStrategies.getCompactedHashValue(bufferedImage);

        return url;
    }

    public static byte[] getFingerPrint() {
        return fingerPrint;
    }
}
