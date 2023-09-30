package com.example.ShopHop.service;

import com.example.ShopHop.Enum.OrderStatus;
import com.example.ShopHop.Enum.ProductStatus;
import com.example.ShopHop.dto.RequestDto.DirectOrderRequestDto;
import com.example.ShopHop.dto.RequestDto.OrderListRequestDto;

import com.example.ShopHop.dto.RequestDto.ItemRequestDto;
import com.example.ShopHop.dto.ResponseDto.OrderedResponseDto;
import com.example.ShopHop.dto.ResponseDto.ProductResponseDto;
import com.example.ShopHop.exception.*;
import com.example.ShopHop.model.*;
import com.example.ShopHop.repository.CardRepository;
import com.example.ShopHop.repository.CustomerRepository;
import com.example.ShopHop.repository.OrderedRepository;
import com.example.ShopHop.repository.ProductRepository;
import com.example.ShopHop.transformer.ItemTransformer;
import com.example.ShopHop.transformer.OrderedTransformer;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderedService {

    @Autowired
    ProductService productService;

    @Autowired
    ItemService itemService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderedRepository orderedRepository;

    @Autowired
    JavaMailSender mailSender;

    public Ordered placeOrder(Customer customer, Card card) throws InvalidProductException, InvalidCartException {

        Cart cart= customer.getCart();
        if(cart.getNumberOfItems()==0){
            throw new InvalidCartException("Your cart is empty!");
        }

        // Creating Order object using Builder through Order Transformer
        Ordered order= OrderedTransformer.cartToOrder(cart, card.getCardNo());
        //Ordered order= OrderedTransformer.directItemToOrder(customer, item, cardNo);


        // setting items list attribute of order
        List<Item> orderedItems= new ArrayList<>();
        for(Item item: cart.getItems()){
            try {
                productService.decreaseProductQuantity(item);
                orderedItems.add(item);
            }
            catch (Exception e){
                throw new InvalidProductException(e.getMessage());
            }
        }
        order.setItems(orderedItems);
        order.setCustomer(customer);

        // setting Order attribute of Item
        //orderedItems.forEach(item -> item.setOrder(order));  // using forEach
        for (Item item:orderedItems){
            item.setOrder(order);
            item.setCart(null);
        }

        // setting parameter of cart
//        for(int i=0; i<cart.getItems().size(); i++){
//            cart.getItems().remove(i);
//        }
        cart.getItems().clear();
        cart.setNumberOfItems(0);
        cart.setCartTotal(0);

        // setting parameter of customer
        customer.getOrderedList().add(order);


        // Saving order in the DB
        Ordered saveOrder= orderedRepository.save(order);

        // sending confirmation mail to customer
        String text="Congrats!. " + customer.getName()+ " You have successfully placed the order!";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("backendproject@gmail.com");
        message.setTo(customer.getEmailId());
        message.setSubject("ORDER PLACED");
        message.setText(text);
        mailSender.send(message);

        return saveOrder;
    }

    public List<OrderedResponseDto> directPlaceOrder(OrderListRequestDto orderListRequestDto) throws InvalidOrderException, InvalidCardException {

        String emailId=orderListRequestDto.getCustomerEmailId();
        String cardNo=orderListRequestDto.getCardNo();
        String cvv=orderListRequestDto.getCvv();

        // 1st Step:- Preparing ItemRequest to generate an Item
       // ItemRequestDto itemRequest= ItemTransformer.directOrderRequestDtoToItemRequestDto(directOrderRequestDto);


        // 2nd Step:- Creating an Item and validate the respective parameter

            // Now Customer & Product are valid and hence Item created.

            // 3rd Step:- fetching customer
            Customer customer= customerRepository.findByEmailId(emailId);

            // 4th Step:- Fetching Card after validating cardNo
            //            a . Validating cardNo length
            if(cardNo.length()!=16){
                throw new InvalidCardException("Incorrect card no!");
            }
            //            b. validating cardNo
            Card card= cardRepository.findByCardNo(cardNo);
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
            if(cvv.length()!=3){
                throw new InvalidCardException("Incorrect cvv!");
            }
            //          f. validating card & cvv
            if(!card.getCvv().equals(cvv)){
                throw new InvalidCardException("Invalid cvv!");
            }
            // Now cardNo, cvv both are valid and hebce Card is valid

        List<OrderedResponseDto> orderedResponseDtoList=new ArrayList<>();

        int size=orderListRequestDto.getOrderItems().size();
        for(int i=0; i< size; i++){
            int productId=orderListRequestDto.getOrderItems().get(i).getProductId();
            int requiredQuantity=orderListRequestDto.getOrderItems().get(i).getRequiredQuantity();

            ItemRequestDto itemRequest= ItemTransformer.directOrderRequestDtoToItemRequestDto(emailId,productId,requiredQuantity);

            try {
                Item  item= itemService.createItem(itemRequest);
                OrderedResponseDto orderedResponseDto=orderItems(customer,cardNo ,item,productId,requiredQuantity);
                orderedResponseDtoList.add(orderedResponseDto);
            } catch (Exception e){
                throw new InvalidOrderException(e.getMessage());
            }
        }

            // 5th Step:- Now creating Order using Builder through Order Transformer and setting it's all attributes

        return orderedResponseDtoList;

        }

        public OrderedResponseDto  orderItems(Customer customer, String cardNo,Item item,int productId,int requiredQuantity){
            Ordered order= OrderedTransformer.directItemToOrder(customer, item, cardNo);
            // setting items(list of item) of ordered
            List<Item> items= new ArrayList<>();
            items.add(item);
            order.setItems(items);

            // 6th Step:- Setting attribute of Item
            item.setOrder(order);

            // 7th Step:- setting attribute of customer
            customer.getOrderedList().add(order);

            // 8th Step:- Now fetching the product and decreasing the product count and
            //            if it's quantity becomes 0 then setting the product status to out of stock.
            //            And setting attributes of product
            Product product= productRepository.findById(productId).get();
            product.setQuantity(product.getQuantity()-requiredQuantity);
            if(product.getQuantity()==0) {
                product.setProductStatus(ProductStatus.OUT_OF_STOCK);
            }

            //   product.setTotalQuantitySold(product.getTotalQuantitySold() + directOrderRequest.getRequiredQuantity());
            product.getItems().add(item);

             /* for(Item item: items){
            product.getItems().add(item);
        }*/
            product.setItems(items);


            // 9th Step:- saving the ordered in the DB
            Ordered savedOrder= orderedRepository.save(order);


            // 10th Step:- Sending email
            String text="Congrats!. " + customer.getName()+ " You have successfully placed the order!";
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("backendproject20@gmail.com");
            message.setTo(customer.getEmailId());
            message.setSubject("ORDER PLACED");
            message.setText(text);
            mailSender.send(message);



            // 11th Step:- Preparing OrderResponse
            OrderedResponseDto orderedResponseDto= OrderedTransformer.orderToOrderResponse(savedOrder);
//        ItemResponse itemResponse= ItemTransformer.itemToItemResponse(item);
//        List<ItemResponse> itemResponseList=new ArrayList<>();
//        itemResponseList.add(itemResponse);
//        orderResponse.setItemResponseList(itemResponseList);

            return orderedResponseDto;
        }


        public String cancelOrder(String customerEmailId, String orderNo) throws InvalidOrderException, InvalidEmailException, InvalidCustomerException {

            // Validating customerEmailId and fetching the Customer
            Customer customer= customerRepository.findByEmailId(customerEmailId);
            if(customer==null){
                throw new InvalidEmailException("Invalid email id!");
            }
            // Now the Customer is valid



            // Validating orderNo And fetching the Ordered Object
            Ordered order= orderedRepository.findByOrderNo(orderNo);
            if(order==null){
                throw new InvalidOrderException("Invalid order no!");
            }
            // checking whether the order belong to same customer or not
            if(order.getCustomer()!=customer){
                throw new InvalidCustomerException("This isn't your order!");
            }
            // Now the order is valid


            // Getting item list of order
            List<Item> itemList= order.getItems();

            // fetching the product and setting its attribute accordingly
            for(Item item: itemList){
                Product product= item.getProduct();
                product.setQuantity(product.getQuantity() + item.getRequiredQuantity());
                //product.setTotalQuantitySold(product.getTotalQuantitySold() - item.getRequiredQuantity());
            }

            order.setOrderStatus(OrderStatus.CANCEL);

            List<Ordered> orderedList= customer.getOrderedList();
            for (int i=0; i<orderedList.size(); i++){
                if(orderedList.get(i).getOrderNo()==orderNo){
                    orderedList.remove(i);
                }
            }

            customer.setOrderedList(orderedList);

            customerRepository.save(customer);

            return "Order cancelled successfully!";
        }


    public List<OrderedResponseDto> getAllOrdersOfCustomer(String emailId) throws InvalidCustomerException {

        // fetching customer details and validating it
        Customer customer= customerRepository.findByEmailId(emailId);
        if(customer==null){
            throw new InvalidCustomerException("Invalid email id!");
        }
        // Now customer is valid

        List<Ordered>orderedList= customer.getOrderedList();
        List<OrderedResponseDto> orderResponseList= new ArrayList<>();

        for (Ordered order: orderedList){
            orderResponseList.add(OrderedTransformer.orderToOrderResponse(order));
        }

        return orderResponseList;
    }
}