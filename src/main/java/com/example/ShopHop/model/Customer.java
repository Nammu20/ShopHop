package com.example.ShopHop.model;

import com.example.ShopHop.Enum.Gender;
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
@Table(name="customer")
@Builder

public class Customer {

    @Id
   // @Column(name="customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name" , nullable = false)
    String name;

    @Column(name = "dob" , nullable = false)
    String dob;

    @Column(name = "mob_no" , unique = true, nullable = false)
    String mobNo;

    @Column(name = "email_id" , unique = true, nullable = false)
    String emailId;

    @Column(name = "password" , unique = true, nullable = false)
    String password;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(name ="address", nullable = false)
    String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<Card> cards = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<Ordered> orderedList = new ArrayList<>();


}
