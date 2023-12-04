package logan.tillman.ecom.controller;

import logan.tillman.ecom.dto.ProductDTO;
import logan.tillman.ecom.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    private ProductDTO productDTO;

    @BeforeEach
    void setup() {
        productDTO = ProductDTO.builder()
                .title("A random product")
                .productId(1)
                .description("A really cool product")
                .releaseDate(OffsetDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("createProduct Test Suite")
    class CreateProductTests {

        @Test
        @DisplayName("When a new product is created, then send it in a CREATED response")
        void createProductTest() {
            when(productService.createProduct(any(ProductDTO.class))).thenReturn(productDTO);

            var response = productController.createProduct(productDTO);

            verify(productService, times(1)).createProduct(any(ProductDTO.class));
            verifyNoMoreInteractions(productService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isEqualTo(productDTO);
        }

        @Test
        @DisplayName("When failing to create a new product, then send a BAD_REQUEST response")
        void createProductWithNullResponseTest() {
            when(productService.createProduct(any(ProductDTO.class))).thenReturn(null);

            var response = productController.createProduct(productDTO);

            verify(productService, times(1)).createProduct(any(ProductDTO.class));
            verifyNoMoreInteractions(productService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNull();
        }
    }

    @Nested
    @DisplayName("getProduct Test Suite")
    class GetProductTests {

        @Test
        @DisplayName("When a product is fetched, then send it in an OK response")
        void getProductTest() {
            when(productService.getProduct(anyInt())).thenReturn(productDTO);

            var response = productController.getProduct(1);

            verify(productService, times(1)).getProduct(anyInt());
            verifyNoMoreInteractions(productService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(productDTO);
        }

        @Test
        @DisplayName("When the product is not found, then send a NOT_FOUND response")
        void getProductWithNullResponseTest() {
            when(productService.getProduct(anyInt())).thenReturn(null);

            var response = productController.getProduct(1);

            verify(productService, times(1)).getProduct(anyInt());
            verifyNoMoreInteractions(productService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNull();
        }
    }

    @Nested
    @DisplayName("updateProduct Test Suite")
    class UpdateProductTests {

        @Test
        @DisplayName("When a product is updated, then return it in an OK response")
        void updateProductTest() {
            when(productService.updateProduct(anyInt(), any(ProductDTO.class))).thenReturn(productDTO);

            var response = productController.updateProduct(1, productDTO);

            verify(productService, times(1)).updateProduct(anyInt(), any(ProductDTO.class));
            verifyNoMoreInteractions(productService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(productDTO);
        }

        @Test
        @DisplayName("When a product fails to update, then return a BAD_REQUEST response")
        void updateProductWithNullResponseTest() {
            when(productService.updateProduct(anyInt(), any(ProductDTO.class))).thenReturn(null);

            var response = productController.updateProduct(1, productDTO);

            verify(productService, times(1)).updateProduct(anyInt(), any(ProductDTO.class));
            verifyNoMoreInteractions(productService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNull();
        }
    }
}
