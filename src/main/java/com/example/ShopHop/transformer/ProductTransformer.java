package com.example.ShopHop.transformer;

import com.example.ShopHop.Enum.ProductCategory;
import com.example.ShopHop.Enum.ProductStatus;
import com.example.ShopHop.dto.RequestDto.ProductRequestDto;
import com.example.ShopHop.dto.ResponseDto.ProductResponseDto;
import com.example.ShopHop.exception.InvalidProductException;
import com.example.ShopHop.model.Product;

public class ProductTransformer {

    public static Product ProductRequestDtoToProduct(ProductRequestDto productRequestDto) throws InvalidProductException {

        // validating the product category


        Product product = Product.builder()
                        .name(productRequestDto.getName())
                        .price(productRequestDto.getPrice())
                        .quantity(productRequestDto.getQuantity())
                        //.totalQuantityAdded(productRequestDto.getQuantity())
                        .productCategory(productRequestDto.getProductCategory())
                        .productStatus(ProductStatus.AVAILABLE)
                        .build();

        // Setting maxOrderedQuantity & warrantyPeriods attributes of Product
        if(productRequestDto.getProductCategory() == "CLOTHING"){
            product.setMaxOrderedQuantity("15");
        }
        else{
            product.setMaxOrderedQuantity("20");
        }
        return product;
    }
    public static ProductResponseDto ProductToProductResponseDto(Product product){
        return ProductResponseDto.builder()
                .productName(product.getName())
                .id(product.getId())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .productCategory(product.getProductCategory())
                .productStatus(product.getProductStatus())
                .build();
    }
}
