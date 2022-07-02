package com.example.demo.util.Comparson;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.awt.image.BufferedImage;

public interface ImageComparisonStrategies {

    /**
     * 获取图片特征
     */
    byte[] getCompactedHashValue(BufferedImage src);

    /**
     * 获取比较结果
     */
    float getResult(byte[] f1, byte[] f2);

}
