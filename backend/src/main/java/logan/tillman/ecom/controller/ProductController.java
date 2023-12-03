package logan.tillman.ecom.controller;


import logan.tillman.ecom.dto.ProductDTO;
import logan.tillman.ecom.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        var product = productService.createProduct(productDTO);

        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Integer productId) {
        var product = productService.getProduct(productId);

        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer productId, @RequestBody ProductDTO productDTO) {
        var updatedProduct = productService.updateProduct(productId, productDTO);

        if (updatedProduct != null) {
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
