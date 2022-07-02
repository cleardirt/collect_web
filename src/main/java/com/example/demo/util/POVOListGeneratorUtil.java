package com.example.demo.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class POVOListGeneratorUtil {
    /**
     * POList 和 VOList 互转的工具类
     * 将List对象泛型中的PO对象转化为VO对象 或者 VO转PO 后返回
     * @param <P> PO类型
     * @param <V> VO类型
     */
    public static <P,V> List<V> convert(List<P> poList, Class<V> vClass){
        List<V> voList = new ArrayList<>();
        for (P po:poList){
            V v = null;
            try {
                v = vClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            assert v!=null;
            BeanUtils.copyProperties(po,v);
            voList.add(v);
        }
        return voList;
    }
}
