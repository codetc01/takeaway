package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface addressBookMapper {
    @Insert("insert into address_book(user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default) " +
            "values (#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void addAddressBook(AddressBook addressBook);

    @Select("select * from address_book where user_id = #{currentId}")
    List<AddressBook> showAddressBook(Long currentId);

    @Update("update address_book set is_default = #{isDefault} where id = #{id}")
    void setDefaultAddress(Long id, Integer isDefault);

    @Select("select * from address_book where user_id = #{currentId} and is_default = 1")
    AddressBook getDefaultAddressByUserId(Long currentId);

    void editAddressById(AddressBook addressBook);

    @Select("select * from address_book where id = #{id}")
    AddressBook getAddressById(Long id);

    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);
}
