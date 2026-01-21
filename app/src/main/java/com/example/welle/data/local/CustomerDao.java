package com.example.welle.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CustomerDao {
    @Insert
    void insertCustomer(Customer customer);

    @Query("SELECT * FROM customers WHERE email = :email AND mobile = :mobile LIMIT 1")
    Customer findCustomerByEmailAndMobile(String email, String mobile);

    @Query("SELECT * FROM customers")
    List<Customer> getAllCustomers();
}