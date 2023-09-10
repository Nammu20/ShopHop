package com.example.ShopHop.Enum;

public enum CardType {
    VISA,
    RUPAY,
    MASTERCARD;

    public static boolean contains(String cardType) {
        try {
            CardType.valueOf(cardType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
