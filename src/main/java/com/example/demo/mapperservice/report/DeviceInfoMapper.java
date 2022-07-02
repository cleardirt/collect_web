package com.example.demo.mapperservice.report;

import com.example.demo.po.report.DeviceInfo;


import java.util.List;

public interface DeviceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByTaskId(Integer task_id);

    int insert(DeviceInfo record);

    DeviceInfo selectByPrimaryKey(Integer id);

    List<DeviceInfo> selectAll();

    int updateByPrimaryKey(DeviceInfo record);

    List<DeviceInfo> selectByTaskId(Integer task_id);
}