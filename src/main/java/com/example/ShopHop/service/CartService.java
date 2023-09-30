package com.example.ShopHop.service;

import com.example.ShopHop.dto.RequestDto.CheckOutCartRequestDto;
import com.example.ShopHop.dto.RequestDto.ItemRequestDto;
import com.example.ShopHop.dto.ResponseDto.CartResponseDto;
import com.example.ShopHop.dto.ResponseDto.ItemResponseDto;
import com.example.ShopHop.dto.ResponseDto.OrderedResponseDto;
import com.example.ShopHop.exception.*;
import com.example.ShopHop.model.*;
import com.example.ShopHop.repository.*;
import com.example.ShopHop.transformer.CartTransformer;
import com.example.ShopHop.transformer.ItemTransformer;
import com.example.ShopHop.transformer.OrderedTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderedService orderedService;

    @Autowired
    ProductRepository productRepository;
   // @Autowired
    //private OrderRepository orderRepository;

    public CartResponseDto addToCart(Item item, ItemRequestDto itemRequestDto) throws InvalidCustomerException {


        Product product = productRepository.findById(itemRequestDto.getProductId()).get();
        Customer customer = customerRepository.findByEmailId(itemRequestDto.getCustomerEmailId());

        if(customer==null){
            throw new InvalidCustomerException("Customer doesn't exist");
        }
        if(customer.getCart()==null){
            customer.setCart(new Cart());
        }
        Cart cart = customer.getCart();
        cart.setCustomer(customer);
        //cart.setCartTotal(item.getRequiredQuantity()*product.getPrice());
       // cart.setCartTotal(cart.getCartTotal()+item.getRequiredQuantity()*product.getPrice());

        int cartTotal = cart.getCartTotal() + item.getRequiredQuantity() * product.getPrice();
        cart.setCartTotal(cartTotal);

        cart.getItems().add(item);
        cart.setNumberOfItems(cart.getItems().size());

        item.setCart(cart);
        item.setProduct(product);

        Cart savedCart = cartRepository.save(cart);  // saves both cart and item


        Item savedItem = cart.getItems().get(cart.getItems().size() - 1);
        product.getItems().add(savedItem);
        //prepare response dto
        return CartTransformer.CartToCartResponseDto(savedCart);
    }

    public List<ItemResponseDto> viewItemsOfCart(String customerEmailId) throws InvalidCustomerException, InvalidCartException {

            // Fetching customer object with validating it
            Customer customer= customerRepository.findByEmailId(customerEmailId);
            if (customer==null){
                throw new InvalidCustomerException("Invalid customer email id!");
            }

            // fetching cart of customer
            Cart cart= customer.getCart();
            if(cart.getNumberOfItems()==0){
                throw new InvalidCartException("Your cart is empty!");
            }

            // Converting Item to item response
            List<Item> itemList= cart.getItems();
            List<ItemResponseDto> itemResponseList= new ArrayList<>();
            for (Item item:itemList){
                itemResponseList.add(ItemTransformer.ItemToItemResponseDto(item));
            }

            return itemResponseList;
        }

    public String removeItemFromCart(String customerEmailId, int itemId) throws InvalidEmailException, InvalidCartException {

        Customer customer= customerRepository.findByEmailId(customerEmailId);
        if (customer==null){
            throw new InvalidEmailException("Invalid email id!");
        }
        // Now customer is valid

        // Getting the cart of customer and checking whether the Item belong to the cart or Not
        //Cart cart = new Cart();
        Cart cart= customer.getCart();
        if(cart.getNumberOfItems()==0){
            throw new InvalidCartException("Your cart is already empty!");
        }

        boolean isFound=false;
        for(int i=0; i<cart.getItems().size(); i++){
            Item item= cart.getItems().get(i);
            if(item.getId()==itemId){
                cart.getItems().remove(i);
                cart.setNumberOfItems(cart.getNumberOfItems()-1);
                cart.setCartTotal(cart.getCartTotal()-item.getRequiredQuantity()*item.getProduct().getPrice());
                itemRepository.delete(item);
                isFound=true;
                break;
            }
        }

        if(!isFound) { // it will execute when item not found in cart
            throw new InvalidCartException("Sorry the item isn't present in your cart!");
        }

        return "Item is removed from your cart!";
    }

    public OrderedResponseDto checkOutCart(CheckOutCartRequestDto checkOutCartRequestDto) throws InvalidCustomerException, InvalidCardException, InvalidOrderException {
        // 1st Step:- Fetching the customer object from the DB
        Customer customer= customerRepository.findByEmailId(checkOutCartRequestDto.getCustomerEmailId());
        if(customer==null){
            throw new InvalidCustomerException("Invalid customer email id!");
        }


        // 2nd Step:-  Validating cardNo
        //            a . Validating cardNo length
        if(checkOutCartRequestDto.getCardNo().length()!=16){
            throw new InvalidCardException("Incorrect card no!");
        }
        //            b. validating cardNo
        Card card= cardRepository.findByCardNo(checkOutCartRequestDto.getCardNo());
        if(card==null){
            throw new InvalidCardException("Invalid card no!");
        }
        //            c. validating card and customer
        if(card.getCustomer()!=customer){
            throw new InvalidCardException("Card doesn't belong to you!");
        }
        //            d. validating card expiry date
        LocalDate todayDate= LocalDate.now();
        LocalDate cardExpiryDate= new Date(card.getExpiryDate().getTime()).toLocalDate();
        if(cardExpiryDate.isBefore(todayDate)){
            throw new InvalidCardException("Card is expired!");
        }
        //          e. validating cvv
        if(checkOutCartRequestDto.getCvv().length()!=3){
            throw new InvalidCardException("Incorrect cvv!");
        }
        //          f. validating card & cvv
        if(!card.getCvv().equals(checkOutCartRequestDto.getCvv())){
            throw new InvalidCardException("Invalid cvv!");
        }


        // 3rd Step:- Now placing an Order
        Ordered order;
        try {
            // this will throw exception when either product goes out of stock or when the quantity of product
            // goes lesser than the required quantity
            order= orderedService.placeOrder(customer, card);
        }
        catch (Exception e){
            throw new InvalidOrderException(e.getMessage());
        }
        // Now Order is valid

        // Preparing OrderResponse using Builder through OrderTransformer

        OrderedResponseDto orderedResponseDto= OrderedTransformer.orderToOrderResponse(order);
        // setting itemResponse list attribute of OrderResponse
//        List<ItemResponse> itemResponseList= new ArrayList<>();
//        for (Item item:order.getItems()){
//            //item.setOrder(savedOrder);  // Now we are ordering successfully. So, setting the Order attribute of item
//            itemResponseList.add(ItemTransformer.itemToItemResponse(item));
//        }
//        orderResponse.setItemResponseList(itemResponseList);

        return orderedResponseDto;
    }


}
