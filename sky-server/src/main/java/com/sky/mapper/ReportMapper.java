package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface ReportMapper {
    Double turnoverStatistics(HashMap<Object, Object> objectObjectHashMap);
}
