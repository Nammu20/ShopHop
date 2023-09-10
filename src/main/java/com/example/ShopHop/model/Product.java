package com.example.ShopHop.model;

import com.example.ShopHop.Enum.ProductCategory;
import com.example.ShopHop.Enum.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="product")
@Builder

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    int price;

    @Column(nullable = false)
    int quantity;

//    @Column(nullable = false)
//    //int totalQuantityAdded;

//    @Column(nullable = false)
//    //int totalQuantitySold;

    @Column(nullable = false)
    String maxOrderedQuantity;

    @Column(nullable = false)
    String productCategory;

    String warrantyPeriods;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ProductStatus productStatus;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<Item> items =  new ArrayList<>();



}


