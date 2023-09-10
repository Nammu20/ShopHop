package com.example.ShopHop.repository;

import com.example.ShopHop.Enum.CardType;
import com.example.ShopHop.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {
    Card findByCardNo(String cardNo);
    List<Card> findByCardType(CardType cardType);

}
