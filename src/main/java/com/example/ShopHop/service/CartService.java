package com.example.ShopHop.service;

import com.example.ShopHop.dto.RequestDto.ItemRequestDto;
import com.example.ShopHop.dto.ResponseDto.CartResponseDto;
import com.example.ShopHop.dto.ResponseDto.ItemResponseDto;
import com.example.ShopHop.exception.InvalidCartException;
import com.example.ShopHop.exception.InvalidCustomerException;
import com.example.ShopHop.exception.InvalidEmailException;
import com.example.ShopHop.model.Cart;
import com.example.ShopHop.model.Customer;
import com.example.ShopHop.model.Item;
import com.example.ShopHop.model.Product;
import com.example.ShopHop.repository.*;
import com.example.ShopHop.transformer.CartTransformer;
import com.example.ShopHop.transformer.ItemTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

   // @Autowired
  //  OrderService orderService;

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

}
