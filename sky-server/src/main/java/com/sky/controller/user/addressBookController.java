package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.addressBookService;
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
 * @DATE: 2023/10/16 21:52
 */
@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "地址簿接口")
public class addressBookController {

    @Autowired
    private addressBookService addressBookService;

    @PostMapping()
    @ApiOperation("新增地址")
    public Result addAddressBook(@RequestBody AddressBook addressBook){
        addressBookService.addAddressBook(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查看当前用户所有地址信息")
    public Result<List<AddressBook>> showAddressBook(){
        List<AddressBook> addressBooks = addressBookService.showAddressBook();
        return Result.success(addressBooks);
    }

    @PutMapping("/default")
    @ApiOperation("设为默认地址")
    public Result setDefaultAddress(@RequestBody AddressBook addressBook){
        addressBookService.setDefaultAddress(addressBook.getId());
        return Result.success();
    }

    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefaultAddress(){
        AddressBook addressBook = addressBookService.getDefaultAddress();
        return Result.success(addressBook);
    }

    @PutMapping("")
    @ApiOperation("根据Id修改地址")
    public Result editAddressById(@RequestBody AddressBook addressBook){
        addressBookService.editAddressById(addressBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询地址")
    public Result<AddressBook> getAddressById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getAddressById(id);
        return Result.success(addressBook);
    }

    @DeleteMapping()
    @ApiOperation("根据Id删除地址")
    public Result deleteById(Long id){
        addressBookService.deleteById(id);
        return Result.success();
    }
}
