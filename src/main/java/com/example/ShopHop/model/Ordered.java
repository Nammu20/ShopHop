package com.example.ShopHop.model;

import com.example.ShopHop.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
//@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="order_info")
@Builder

public class Ordered {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(unique = true, nullable = false)
    String orderNo;

    @Column(nullable = false)
    int noOfItems;

    @Column(nullable = false)
    int totalOrderValue;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @CreationTimestamp
    Date orderDate;

    @Column(nullable = false)
    String cardUsed;

    @ManyToOne
    @JoinColumn
    Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<Item> items = new ArrayList<>();


}
