package logan.tillman.ecom.service;

import logan.tillman.ecom.dao.CategoryRepository;
import logan.tillman.ecom.dao.ProductRepository;
import logan.tillman.ecom.dto.CategoryDTO;
import logan.tillman.ecom.dto.ProductDTO;
import logan.tillman.ecom.entity.Category;
import logan.tillman.ecom.entity.Product;
import logan.tillman.ecom.mapper.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    DtoMapper dtoMapper;

    @InjectMocks
    ProductService productService;

    private Product product;
    private ProductDTO productDTO;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setup() {
        category = Category.builder()
                .categoryId(1)
                .name("Category name")
                .build();

        categoryDTO = CategoryDTO.builder()
                .categoryId(1)
                .name("Category name")
                .build();

        product = Product.builder()
                .title("Product title")
                .productId(1)
                .description("A good product")
                .releaseDate(OffsetDateTime.now())
                .categories(List.of(category))
                .build();

        productDTO = ProductDTO.builder()
                .title("Product title")
                .productId(1)
                .description("A good product")
                .releaseDate(OffsetDateTime.now())
                .categories(List.of(categoryDTO))
                .build();
    }

    @Nested
    @DisplayName("getProduct Test Suite")
    class GetProductTests {

        @Test
        @DisplayName("When the product is fetched, then map it to the DTO")
        void getProductTest() {
            when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
            when(dtoMapper.mapToProductDTO(any(Product.class))).thenCallRealMethod();
            when(dtoMapper.mapToCategoryDTO(any(Category.class))).thenCallRealMethod();

            var foundProduct = productService.getProduct(1);

            verify(productRepository, times(1)).findById(anyInt());
            verifyNoMoreInteractions(productRepository);

            verify(dtoMapper, times(1)).mapToProductDTO(any(Product.class));
            verify(dtoMapper, times(1)).mapToCategoryDTO(any(Category.class));
            verifyNoMoreInteractions(dtoMapper);

            assertThat(foundProduct)
                    .hasFieldOrPropertyWithValue("title", product.getTitle())
                    .hasFieldOrPropertyWithValue("productId", product.getProductId())
                    .hasFieldOrPropertyWithValue("description", product.getDescription())
                    .hasFieldOrPropertyWithValue("releaseDate", product.getReleaseDate())
                    .extracting(ProductDTO::getCategories)
                    .asList()
                    .first()
                    .hasFieldOrPropertyWithValue("name", category.getName())
                    .hasFieldOrPropertyWithValue("categoryId", category.getCategoryId());
        }

        @Test
        @DisplayName("When no product is found, then return null")
        void getProductNotFoundTest() {
            when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

            var foundProduct = productService.getProduct(1);

            verify(productRepository, times(1)).findById(anyInt());
            verifyNoMoreInteractions(productRepository);

            verifyNoInteractions(dtoMapper);

            assertThat(foundProduct).isNull();
        }
    }

    @Nested
    @DisplayName("createProduct Test Suite")
    class CreateProductTests {

        @Test
        @DisplayName("When a product is created, then map it to the DTO")
        void createProductTest() {
            product.setCategories(null);

            when(productRepository.saveAndFlush(any(Product.class))).thenReturn(product);
            when(dtoMapper.mapToProductDTO(any(Product.class))).thenCallRealMethod();

            var createdProduct = productService.createProduct(productDTO);

            verify(productRepository, times(1)).saveAndFlush(any(Product.class));
            verifyNoMoreInteractions(productRepository);

            verify(dtoMapper, times(1)).mapToProductDTO(any(Product.class));
            verifyNoMoreInteractions(dtoMapper);

            assertThat(createdProduct)
                    .hasFieldOrPropertyWithValue("title", productDTO.getTitle())
                    .hasFieldOrPropertyWithValue("description", productDTO.getDescription())
                    .hasFieldOrPropertyWithValue("releaseDate", productDTO.getReleaseDate());
        }

        @Test
        @DisplayName("When creating a product with a null title, then return null")
        void createProductWithNullTitleTest() {
            productDTO.setTitle(null);

            var createdProduct = productService.createProduct(productDTO);

            verifyNoInteractions(productRepository);
            verifyNoInteractions(dtoMapper);

            assertThat(createdProduct).isNull();
        }

        @Test
        @DisplayName("When creating a product with an empty title, then return null")
        void createProductWithEmptyTitleTest() {
            productDTO.setTitle("");

            var createdProduct = productService.createProduct(productDTO);

            verifyNoInteractions(productRepository);
            verifyNoInteractions(dtoMapper);

            assertThat(createdProduct).isNull();
        }
    }

    @Nested
    @DisplayName("updateProduct Test Suite")
    class UpdateProductTests {

        @Test
        @DisplayName("When a product is updated, then map it to the DTO")
        void updateProductTest() {
            when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
            when(categoryRepository.findAllById(anySet())).thenReturn(List.of(category));
            when(dtoMapper.mapToProductDTO(any(Product.class))).thenCallRealMethod();
            when(dtoMapper.mapToCategoryDTO(any(Category.class))).thenCallRealMethod();

            var updatedProduct = productService.updateProduct(1, productDTO);

            verify(productRepository, times(1)).findById(anyInt());
            verify(productRepository, times(1)).saveAndFlush(any(Product.class));
            verifyNoMoreInteractions(productRepository);

            verify(categoryRepository, times(1)).findAllById(anySet());
            verifyNoMoreInteractions(categoryRepository);

            verify(dtoMapper, times(1)).mapToProductDTO(any(Product.class));
            verify(dtoMapper, times(1)).mapToCategoryDTO(any(Category.class));
            verifyNoMoreInteractions(dtoMapper);

            assertThat(updatedProduct)
                    .hasFieldOrPropertyWithValue("title", productDTO.getTitle())
                    .hasFieldOrPropertyWithValue("productId", productDTO.getProductId())
                    .hasFieldOrPropertyWithValue("description", productDTO.getDescription())
                    .hasFieldOrPropertyWithValue("releaseDate", productDTO.getReleaseDate())
                    .extracting(ProductDTO::getCategories)
                    .asList()
                    .first()
                    .hasFieldOrPropertyWithValue("categoryId", category.getCategoryId())
                    .hasFieldOrPropertyWithValue("name", category.getName());
        }

        @Test
        @DisplayName("When updating a product with a null title, then return null")
        void updateProductWithNullTitleTest() {
            productDTO.setTitle(null);

            var updatedProduct = productService.updateProduct(1, productDTO);

            verifyNoInteractions(productRepository);
            verifyNoInteractions(categoryRepository);
            verifyNoInteractions(dtoMapper);

            assertThat(updatedProduct).isNull();
        }

        @Test
        @DisplayName("When updating a product with an empty title, then return null")
        void updateProductWithEmptyTitleTest() {
            productDTO.setTitle("");

            var updatedProduct = productService.updateProduct(1, productDTO);

            verifyNoInteractions(productRepository);
            verifyNoInteractions(categoryRepository);
            verifyNoInteractions(dtoMapper);

            assertThat(updatedProduct).isNull();
        }

        @Test
        @DisplayName("When no product is found to update, then return null")
        void updateProductNotFoundTest() {
            when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

            var updatedProduct = productService.updateProduct(1, productDTO);

            verify(productRepository, times(1)).findById(anyInt());
            verifyNoMoreInteractions(productRepository);

            verifyNoInteractions(categoryRepository);
            verifyNoInteractions(dtoMapper);

            assertThat(updatedProduct).isNull();
        }
    }
}
