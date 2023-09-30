package com.example.ShopHop.repository;

import com.example.ShopHop.model.Ordered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedRepository extends JpaRepository<Ordered,Integer>{


    Ordered findByOrderNo(String orderNo);

}
