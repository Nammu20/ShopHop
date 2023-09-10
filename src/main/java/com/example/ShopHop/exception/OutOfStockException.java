package com.example.ShopHop.exception;

import org.springframework.data.jpa.repository.JpaRepository;

public class OutOfStockException extends Exception {
    public OutOfStockException(String message){
        super(message);
    }
}
