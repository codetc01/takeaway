package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/25 21:47
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> objects = new ArrayList<>();
        objects.add(begin);

        System.out.println(begin + "++" + end) ;

        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            objects.add(begin);
        }

        List<Double> moneyList = new ArrayList<>();
        for (LocalDate date : objects) {
            LocalDateTime beginDay = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endDay = LocalDateTime.of(date, LocalTime.MAX);

            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("begin", beginDay);
            objectObjectHashMap.put("end", endDay);
            objectObjectHashMap.put("status", Orders.COMPLETED);

            Double money = reportMapper.turnoverStatistics(objectObjectHashMap);
            if(money == null){
                money = 0.0;
            }
            moneyList.add(money);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(objects, ","))
                .turnoverList(StringUtils.join(moneyList, ","))
                .build();
    }
}
