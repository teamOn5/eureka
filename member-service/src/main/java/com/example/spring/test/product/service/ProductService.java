package com.example.spring.test.product.service;

import com.example.spring.test.common.exception.CustomException;
import com.example.spring.test.common.exception.ErrorCode;
import com.example.spring.test.product.request.PatchRequestProduct;
import com.example.spring.test.product.request.ProductRequest;
import com.example.spring.test.product.request.UpdateProductRequest;
import com.example.spring.test.product.response.ProductResponse;
import com.example.spring.test.product.entity.Product;
import com.example.spring.test.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> productList() {

        //db 조회
        List<Product> productList =  productRepository.findByDeletedFalse();

        return productList;
    }

    @Transactional
    public ProductResponse product(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

//        product.setPrice(30000000L);

//        ProductResponse response = ProductResponse.builder()
//                .id(product.getId())
//                .price(product.getPrice())
//                .productName(product.getName())
//                .build();

        return ProductResponse.from(productRepository.findById(id).get());
    }

    @Transactional
    public void createProduct (ProductRequest productRequest) {
        // Product 객체 생성
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();

        // product repository를 호출해서 save() 함수 호출
        productRepository.save(product);
    }

    @Transactional
    public void updateProduct (UpdateProductRequest updateProductRequest, Long productId) {

        Product oldProduct = productRepository.findById(productId).get();

        Product newProduct = Product.builder()
                .id(oldProduct.getId())
                .name(updateProductRequest.getName())
                .price(updateProductRequest.getPrice())
                .build();

        // product repository에 저장
        productRepository.save(newProduct);
    }

    @Transactional
    public void patchProduct (PatchRequestProduct patchRequestProduct, Long productId) {
        Product oldProduct = productRepository.findById(productId).get();

        oldProduct.changePriceByAdmin(patchRequestProduct.getPrice());
    }

    @Transactional
    public void deleteProduct (Long productId) {
        productRepository.deleteById(productId);
    }

    @Transactional
    public void softDeleteProduct(Long productId) {
        Product product = productRepository.findById(productId).get();
        product.softDeleteByUser();
    }
}
