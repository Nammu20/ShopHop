package com.example.ShopHop.Enum;

import com.example.ShopHop.exception.InvalidProductException;

public enum ProductCategory {

    CLOTHING,
    FOOTWEAR;

    public static boolean contains(String productCategory) {
        try {
            ProductCategory.valueOf(productCategory);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
