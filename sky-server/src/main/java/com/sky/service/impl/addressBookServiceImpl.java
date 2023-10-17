package com.sky.service.impl;

import com.alibaba.fastjson.serializer.BeanContext;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.addressBookMapper;
import com.sky.service.addressBookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/16 21:54
 */
@Service
public class addressBookServiceImpl implements addressBookService {

    @Autowired
    private addressBookMapper addressBookMapper;

    @Override
    public void addAddressBook(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.addAddressBook(addressBook);
    }

    @Override
    public List<AddressBook> showAddressBook() {
        List<AddressBook> addressBooks = addressBookMapper.showAddressBook(BaseContext.getCurrentId());
        return addressBooks;
    }

    @Override
    public void setDefaultAddress(Long id) {
        // 设为默认，有个逻辑
        // 当前用户无默认地址，直接将该地址设为默认地址
        // 当前用户有默认地址，先取消原来的默认地址
        // 现根据用户id查询默认地址，查出来要么为null，要么为一个对象
        AddressBook addressBook = addressBookMapper.getDefaultAddressByUserId(BaseContext.getCurrentId());
        Integer is_default;
        if (addressBook != null) {
            // 原来有默认地址,先取消原来的地址
            is_default = 0;
            addressBookMapper.setDefaultAddress(addressBook.getId(), is_default);

            addressBookMapper.setDefaultAddress(id, 1);
        } else {
            addressBookMapper.setDefaultAddress(id, 1);
        }
    }

    @Override
    public AddressBook getDefaultAddress() {
        AddressBook defaultAddressByUserId = addressBookMapper.getDefaultAddressByUserId(BaseContext.getCurrentId());
        return defaultAddressByUserId;
    }

    @Override
    public void editAddressById(AddressBook addressBook) {
        addressBookMapper.editAddressById(addressBook);
    }

    @Override
    public AddressBook getAddressById(Long id) {
        AddressBook addressBook = addressBookMapper.getAddressById(id);
        return addressBook;
    }

    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }


}
