package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/4 21:54
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult page = categoryService.page(categoryPageQueryDTO);
        return Result.success(page);
    }

    @GetMapping("/list")
    @ApiOperation("按类型查询")
    public Result<List<Category>> getByPage(Integer type){
        List<Category> categoryList = categoryService.getByPage(type);
        return Result.success(categoryList);
    }

    @PostMapping()
    @ApiOperation("新增分类")
    public Result addCat(@RequestBody CategoryDTO categoryDTO){
        categoryService.addCat(categoryDTO);
        return Result.success();
    }

    @PutMapping()
    @ApiOperation("修改分类")
    public Result editCat(@RequestBody CategoryDTO categoryDTO){
        categoryService.editCat(categoryDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改状态")
    public Result onOrOffCat(@PathVariable Integer status, Long id){
        categoryService.onOrOffCat(status, id);
        return Result.success();
    }

    @DeleteMapping()
    @ApiOperation("根据ID删除")
    public Result deleteCat(Long id){
        categoryService.deleteCat(id);
        return Result.success();
    }
}
