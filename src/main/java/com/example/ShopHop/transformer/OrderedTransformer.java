package com.example.ShopHop.transformer;

import com.example.ShopHop.Enum.OrderStatus;
import com.example.ShopHop.dto.ResponseDto.ItemResponseDto;
import com.example.ShopHop.dto.ResponseDto.OrderedResponseDto;
import com.example.ShopHop.model.Cart;
import com.example.ShopHop.model.Item;
import com.example.ShopHop.model.Ordered;
import com.example.ShopHop.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderedTransformer {
    public static Ordered cartToOrder(Cart cart, String cardNo){
        return Ordered.builder()
                .orderNo(String.valueOf(UUID.randomUUID()))
                .noOfItems(cart.getNumberOfItems())
                .totalOrderValue(cart.getCartTotal())
                .orderStatus(OrderStatus.PLACED)
                .cardUsed(cardNo)
                .build();

    }

    public static OrderedResponseDto orderToOrderResponse(Ordered order){

        List<ItemResponseDto> itemResponseList= new ArrayList<>();
        for (Item item:order.getItems()){
            itemResponseList.add(ItemTransformer.ItemToItemResponseDto(item));
        }

        OrderedResponseDto orderedResponseDto= OrderedResponseDto.builder()
                .customerName(order.getCustomer().getName())
                .orderNo(order.getOrderNo())
                .noOfItems(order.getNoOfItems())
                .totalOrderValue(order.getTotalOrderValue())
                .orderDate(order.getOrderDate())
                .cardUsed(CardTransformer.maskedCard(order.getCardUsed()))
                .itemResponseList(itemResponseList)
                .orderStatus(order.getOrderStatus())
                .build();

        return orderedResponseDto;
    }

    public static Ordered directItemToOrder(Customer customer, Item item, String cardNo){
        return Ordered.builder()
                .orderNo(String.valueOf(UUID.randomUUID()))
                .noOfItems(1)
                .totalOrderValue(item.getProduct().getPrice()* item.getRequiredQuantity())
                .orderStatus(OrderStatus.PLACED)
                .cardUsed(cardNo)
                .customer(customer)
                .build();
    }
}
