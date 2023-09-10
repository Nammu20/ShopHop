package com.example.ShopHop.repository;

import com.example.ShopHop.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Customer findByEmailId(String emailId);

    Customer findByMobNo(String mobNo);
}
