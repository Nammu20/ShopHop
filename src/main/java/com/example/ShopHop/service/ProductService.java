package com.example.ShopHop.service;

import com.example.ShopHop.Enum.ProductCategory;
import com.example.ShopHop.Enum.ProductStatus;
import com.example.ShopHop.dto.RequestDto.UpdateProductCountDto;
import com.example.ShopHop.dto.RequestDto.ProductRequestDto;
import com.example.ShopHop.dto.ResponseDto.ProductResponseDto;
import com.example.ShopHop.exception.InvalidProductException;
import com.example.ShopHop.model.Item;
import com.example.ShopHop.model.Product;
import com.example.ShopHop.repository.ProductRepository;
import com.example.ShopHop.transformer.ProductTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    //check if the seller exists or not
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) throws InvalidProductException {
        Product product;
        try {
            product = ProductTransformer.ProductRequestDtoToProduct(productRequestDto);
        } catch (Exception e) {
            throw new InvalidProductException("Invalid product category!");
        }

        // Saving in the DB
        Product savedProduct = productRepository.save(product);

        //sellerRepository.save(seller); --No need to save it because updation in java happens in place

        // Creating ProductResponse using Builder through ProductTransformer
        return ProductTransformer.ProductToProductResponseDto(product);
    }

    //creating project obj using builder through transformer

    public ProductResponseDto UpdateProductCount(UpdateProductCountDto updateProductCountDto) throws InvalidProductException {

        // Getting Product Object from the DB and also verifying whether the product exist or not
        Product product;
        try {
            product = productRepository.findById(updateProductCountDto.getProductId()).get();
        } catch (Exception e) {
            throw new InvalidProductException("Invalid Product Id!");
        }
        // Now increasing the count of the product
        product.setQuantity(product.getQuantity() + updateProductCountDto.getCount());

       // product.setTotalQuantityAdded(product.getTotalQuantityAdded() + updateProductCountDto.getCount());
        if (product.getProductStatus() == ProductStatus.OUT_OF_STOCK) {
            product.setProductStatus(ProductStatus.AVAILABLE);
        }

        productRepository.save(product);
        return ProductTransformer.ProductToProductResponseDto(product);

    }


    public List<ProductResponseDto> getAllProductsBYCategory(String productCategory) throws InvalidProductException {
        List<Product> productList=new ArrayList<>();
        if(ProductCategory.contains((productCategory))) {
            productList = productRepository.findByProductCategory(productCategory);
        }
        else {
            throw new InvalidProductException("Invalid product category!");
        }

        // Checking whether the Products of that Category exist in the DB or Not
        if (productList.size() == 0) {
            throw new InvalidProductException("Currently the products of " + productCategory + " is unavailable!");
        }

        List<ProductResponseDto> productResponseList = new ArrayList<>();

        for (Product product : productList) {
            productResponseList.add(ProductTransformer.ProductToProductResponseDto(product));
        }

        return productResponseList;
    }


    public String deleteProductOfParticularCategory(int productId) throws InvalidProductException {

        // Getting the Product Object from the DB
        try {
            boolean ifExist=productRepository.existsById(productId);
            if(ifExist){
                productRepository.deleteById(productId);
            }
        }
        catch (Exception e){
            throw new InvalidProductException("Invalid Product Id!");
        }
        // Now Product is valid

        // Removing The Product for the DB

        return "Product deleted successfully ";
    }


    public void decreaseProductQuantity(Item item) throws InvalidProductException {

        Product product = item.getProduct();
        if (product.getProductStatus() == ProductStatus.OUT_OF_STOCK) {
            throw new InvalidProductException("Currently PProduct is out of stock!");
        }
        if (product.getQuantity() < item.getRequiredQuantity()) {
            throw new InvalidProductException("Product quantity is lesser than the required quantity!");
        }
        // the above condition is for the case when customer added the same product more than once and during
        //  placing the order if for any item the same product quantity becomes lesser than the required
        // quantity than in that case the above Exception will be thrown
        // in CHECK OUT CART

        product.setQuantity(product.getQuantity() - item.getRequiredQuantity());

        if (product.getQuantity() == 0) {
            product.setProductStatus(ProductStatus.OUT_OF_STOCK);
        }
    }
}





