package com.example.ShopHop.service;

import com.example.ShopHop.dto.RequestDto.ItemRequestDto;
import com.example.ShopHop.exception.InsufficientQuantityException;
import com.example.ShopHop.exception.InvalidCustomerException;
import com.example.ShopHop.exception.InvalidProductException;
import com.example.ShopHop.exception.OutOfStockException;
import com.example.ShopHop.model.Customer;
import com.example.ShopHop.model.Item;
import com.example.ShopHop.model.Product;
import com.example.ShopHop.repository.CustomerRepository;
import com.example.ShopHop.repository.ProductRepository;
import com.example.ShopHop.transformer.ItemTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CustomerRepository customerRepository;

    public Item createItem(ItemRequestDto itemRequestDto) throws  InsufficientQuantityException, OutOfStockException, InvalidCustomerException, InvalidProductException {

        Optional<Product> productOptional = productRepository.findById(itemRequestDto.getProductId());
        if(productOptional.isEmpty()){
            throw new InvalidProductException("Product doesn't exist");
        }

        Customer customer = customerRepository.findByEmailId(itemRequestDto.getCustomerEmailId());
        if(customer==null){
            throw new InvalidCustomerException("Customer doesn't exist");
        }

        Product product = productOptional.get();
        if(product.getQuantity()==0){
            throw new OutOfStockException("Product is out of stock");
        }
        if(product.getQuantity()<itemRequestDto.getRequiredQuantity()){
            throw new InsufficientQuantityException("Sorry! The required quantity is not avaiable");
        }

        Item item = ItemTransformer.ItemRequestDtoToItem(itemRequestDto.getRequiredQuantity());
        return item;
    }
}
