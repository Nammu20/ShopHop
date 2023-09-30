package com.example.ShopHop.controller;

import com.example.ShopHop.dto.RequestDto.UpdateProductCountDto;
import com.example.ShopHop.dto.RequestDto.ProductRequestDto;
import com.example.ShopHop.dto.ResponseDto.ProductResponseDto;
import com.example.ShopHop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    //api to add product
    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody ProductRequestDto productRequestDto) {
        try {
            ProductResponseDto productResponseDto = productService.addProduct(productRequestDto);
            return new ResponseEntity(productResponseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //api to update product count using id
    @PutMapping("/update-product-count")
    public ResponseEntity UpdateProductCount(@RequestBody UpdateProductCountDto updateProductCountDto){
        try {
            ProductResponseDto productResponseDto= productService.UpdateProductCount(updateProductCountDto);
            return new ResponseEntity(productResponseDto, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //api to get products by category
    @PostMapping("/get-products-by-category")
    public ResponseEntity getAllProductsBYCategory(@RequestBody String productCategory){
        try {
            List<ProductResponseDto> productResponseList=productService.getAllProductsBYCategory(productCategory);
            String jsonObj="sample obj return";
            return new ResponseEntity(productResponseList, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //api to delete product of particular category
    @DeleteMapping("/delete-product-of-particular-category")
    public ResponseEntity deleteProductOfParticularCategory( @RequestParam("productId") int productId){
        try {
            String str=productService.deleteProductOfParticularCategory(productId);
            return new ResponseEntity<>(str, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
