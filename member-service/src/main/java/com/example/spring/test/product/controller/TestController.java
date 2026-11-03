package com.example.spring.test.product.controller;

import com.example.spring.test.common.response.BaseResponseEntity;
import com.example.spring.test.product.request.PatchRequestProduct;
import com.example.spring.test.product.request.UpdateProductRequest;
import com.example.spring.test.product.response.ProductResponse;
import com.example.spring.test.product.entity.Product;
import com.example.spring.test.product.request.ProductRequest;
import com.example.spring.test.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class TestController {

    private final ProductService productService;
    private final Environment environment;

    @GetMapping("/test")
    public String test() {
        return "정상적으로 실행됨";
    }

    @GetMapping
    public BaseResponseEntity<List<Product>> product() {
        String serverName = environment.getProperty("spring.application.name");
        String port = environment.getProperty("local.server.port");
        log.info("GET /product 실행 - server={}, port={}", serverName, port);

        return BaseResponseEntity.ok(productService.productList());
    }

    @GetMapping("/{productId}")
    public BaseResponseEntity<ProductResponse> productById(@PathVariable Long productId) {

        return BaseResponseEntity.ok(productService.product(productId));
    }

    @GetMapping("/list")
    public String productList(@RequestParam Long price,
                              @RequestParam String name
                              ) {
        return name + ":" + price;
    }

    @PostMapping
    public BaseResponseEntity<Void> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        // 프로덕트 서비스의 createProduct 메서드 호출
        productService.createProduct(productRequest);

        return BaseResponseEntity.ok();
        
    }

    @PutMapping("/{productId}")
    public void updateProduct (
            @RequestBody UpdateProductRequest updateProductRequest,
            @PathVariable Long productId
    ) {
        // product service updateProduct라는 메서드 호출
        productService.updateProduct(updateProductRequest, productId);
    }

    @PatchMapping("/{productId}")
    public void patchProduct (@RequestBody PatchRequestProduct patchRequestProduct,
                              @PathVariable Long productId
                              ) {
        // product service patchProduct라는 메서드 호출
        productService.patchProduct(patchRequestProduct, productId);

    }
    
    @DeleteMapping("/{productId}")
    public void deleteProduct (@PathVariable Long productId) {
        // product service에 deleteProduct 호출
        productService.deleteProduct(productId);
    }

    @DeleteMapping("/{productId}/soft")
    public void softDeleteProduct(@PathVariable Long productId) {
        productService.softDeleteProduct(productId);
    }
}
