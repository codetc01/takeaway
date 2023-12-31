package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import java.util.List;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    Employee selectEmp(String username);

    void addEmp(EmployeeDTO employeeDto);

    PageResult page(EmployeePageQueryDTO employeePageQueryDTO);

    Integer onOrOffAccount(Integer status, Long id);

    Employee getById(Integer id);

    void editEmpInfo(EmployeeDTO employeeDTO);
}
