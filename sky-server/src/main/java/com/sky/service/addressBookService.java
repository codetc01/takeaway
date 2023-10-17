package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface addressBookService {
    void addAddressBook(AddressBook addressBook);

    List<AddressBook> showAddressBook();

    void setDefaultAddress(Long id);

    AddressBook getDefaultAddress();

    void editAddressById(AddressBook addressBook);

    AddressBook getAddressById(Long id);

    void deleteById(Long id);
}
