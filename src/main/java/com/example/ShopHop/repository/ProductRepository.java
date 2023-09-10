package com.example.ShopHop.repository;

import com.example.ShopHop.Enum.ProductCategory;
import com.example.ShopHop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query(value = "SELECT * FROM product p WHERE p.product_category=:productCategory AND p.product_status='AVAILABLE'",
           nativeQuery = true)
    List<Product> findByProductCategory(String productCategory);

//    @Query(value = "SELECT * FROM product p WHERE p.product_category=:productCategory AND p.product_ status='AVAILABLE' ORDER BY p.price DESC LIMIT 1",
//            nativeQuery = true)
//    Product costliestProductOfCategory(String productCategory);
}
