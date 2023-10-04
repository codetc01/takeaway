package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工操作")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("退出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    @ApiOperation("新增员工")
    @PostMapping()
    public Result<String>  addEmp(@RequestBody EmployeeDTO employeeDto){
        // 规则校验:长度不符合规定
        if(employeeDto.getUsername().length() < 3 || employeeDto.getUsername().length() > 15){
            return Result.error("用户名长度不合法");
        }

        if(employeeDto.getName().isEmpty()){
            return Result.error("姓名不能为空");
        }

        if(employeeDto.getPhone().length() != 11){
            return Result.error("请输入正确的手机号");
        }

        if(employeeDto.getIdNumber().length() != 18){
            return Result.error("请检查身份证号");
        }else if(!Pattern.compile("^[0-9]\\d{16}[1-9, x]$").matcher(employeeDto.getIdNumber()).find()){
            return Result.error("身份证号不合法");
        }

        // 检查用户名是否唯一 目前通过捕获异常处理
//        if(employeeService.selectEmp(employeeDto.getUsername()) != null){
//            return Result.error("该身份已存在");
//        }

        employeeService.addEmp(employeeDto);

        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("分页")
    public Result page(EmployeePageQueryDTO employeePageQueryDTO){
        PageResult page = employeeService.page(employeePageQueryDTO);
        return Result.success(page);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用或禁用账号")
    public Result onOrOffAccount(@PathVariable Integer status, Long id){
        Integer integer = employeeService.onOrOffAccount(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询员工信息")
    public Result<Employee> getById(@PathVariable Integer id){
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    @PutMapping()
    @ApiOperation("修改员工信息")
    public Result editEmpInfo(@RequestBody EmployeeDTO employeeDTO){
        employeeService.editEmpInfo(employeeDTO);
        return Result.success();
    }

}
