package com.sky.service.impl;

import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.entity.Setmeal;
import com.sky.mapper.*;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private WorkspaceService workspaceService;

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

    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> localDates = new ArrayList<>();

        localDates.add(begin);
        while (!begin.equals(end)){

            begin = begin.plusDays(1);
            localDates.add(begin);

        }

        List<Integer> newUserNumber = new ArrayList<>();

        List<Integer> countUserNumber = new ArrayList<>();
        // 查询当天新注册人数
        for (LocalDate localDate : localDates) {
            LocalDateTime start = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime last = LocalDateTime.of(localDate, LocalTime.MAX);
            Integer number = userMapper.getUserNumberInDay(start, last);
            Integer totalNumber = userMapper.getTotalNumber(last);
            newUserNumber.add(number);
            countUserNumber.add(totalNumber);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(localDates, ","))
                .newUserList(StringUtils.join(newUserNumber, ","))
                .totalUserList(StringUtils.join(countUserNumber, ","))
                .build();
    }

    @Override
    public OrderReportVO orderStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> localDates = new ArrayList<>();

        localDates.add(begin);
        while (!begin.equals(end)){

            begin = begin.plusDays(1);
            localDates.add(begin);

        }
        // 有效
        List<Integer> totalNumber = new ArrayList<>();
        List<Integer> effectiveNumber = new ArrayList<>();

        for (LocalDate localDate : localDates) {
            LocalDateTime start = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime last = LocalDateTime.of(localDate, LocalTime.MAX);

            Integer totalnumber = orderMapper.getTotalNumber(start, last);
            totalNumber.add(totalnumber);

            Integer effectivenumber = orderMapper.getEffectiveNumber(start, last, Orders.COMPLETED);
            effectiveNumber.add(effectivenumber);
        }

        IntSummaryStatistics summaryTotal = totalNumber.stream().mapToInt((s) -> s).summaryStatistics();

        IntSummaryStatistics summaryEffective = effectiveNumber.stream().mapToInt((s) -> s).summaryStatistics();


        return OrderReportVO.builder()
                .dateList(StringUtils.join(localDates, ","))
                .orderCompletionRate(((double)summaryEffective.getSum() / summaryTotal.getSum()))
                .orderCountList(StringUtils.join(totalNumber, ","))
                .validOrderCountList(StringUtils.join(effectiveNumber, ","))
                .totalOrderCount((int) summaryTotal.getSum())
                .validOrderCount((int) summaryEffective.getSum())
                .build();
    }

    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {

        // SQL优化，学学别人的SQL
        // SELECT od.name, sum(od.number) as total FROM order_detail AS od, orders WHERE od.order_id = orders.id and orders.`status` = 5 and order_time < "2023-10-30 10:10:10" and order_time > "2023-10-01 10:10:10" GROUP BY od.`name` ORDER BY total DESC LIMIT 0, 10

        LocalDateTime start = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime last = LocalDateTime.of(end, LocalTime.MAX);

        // 先查询orders表，查询这个时间内有效订单数
        List<Integer> effectiveId = orderMapper.getEffectiveId(start, last, Orders.COMPLETED);
        System.out.println("flag" + effectiveId);

        List<Top10VO> getInOrderId = orderDetailMapper.getInOrderId(effectiveId);
        System.out.println(getInOrderId);

        int count = 0;
        List<String> name = new ArrayList<>();
        List<Integer> total = new ArrayList<>();
        for (Top10VO top10VO : getInOrderId) {
            if(count < 10){
                if(top10VO.getDishId() != null){
                    Dish byId = dishMapper.getById(top10VO.getDishId());
                    name.add(byId.getName());
                }else {
                    Setmeal byId = setmealMapper.getById(top10VO.getSetmealId());
                    name.add(byId.getName());
                }
                total.add(top10VO.getTotal());
                count ++;
            }
        }
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(name, ","))
                .numberList(StringUtils.join(total, ","))
                .build();
    }

    @Override
    public void export(HttpServletResponse httpServletResponse) {

        // 先写出相关时间
        LocalDate now = LocalDate.now();

        LocalDateTime start = LocalDateTime.of(now.minusDays(30), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(now.minusDays(1), LocalTime.MAX);

        LocalDateTime endDay = LocalDateTime.of(now.minusDays(30), LocalTime.MAX);

        // 查询出相关数据
        BusinessDataVO businessDataVO = workspaceService.businessData(start, end);

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/template.xlsx");

        try {
            XSSFWorkbook sheets = new XSSFWorkbook(in);

            XSSFSheet sheetAt = sheets.getSheetAt(0);

            sheetAt.getRow(1).getCell(1).setCellValue(start.toLocalDate() + "-" + end.toLocalDate());

            XSSFRow row3 = sheetAt.getRow(3);
            row3.getCell(2).setCellValue(businessDataVO.getTurnover());
            row3.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row3.getCell(6).setCellValue(businessDataVO.getNewUsers());

            XSSFRow row4 = sheetAt.getRow(4);
            row4.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row4.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            for (int i = 0; i < 30; i++) {

                BusinessDataVO businessDataVO1 = workspaceService.businessData(start.plusDays(i), endDay.plusDays(i));

                XSSFRow row = sheetAt.getRow(7 + i);
                row.getCell(1).setCellValue(start.plusDays(i).toLocalDate().toString());
                row.getCell(2).setCellValue(businessDataVO1.getTurnover());
                row.getCell(3).setCellValue(businessDataVO1.getValidOrderCount());
                row.getCell(4).setCellValue(businessDataVO1.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessDataVO1.getUnitPrice());
                row.getCell(6).setCellValue(businessDataVO1.getNewUsers());

            }

            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            sheets.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
