package logan.tillman.ecom.mapper;

import logan.tillman.ecom.dto.ProductDTO;
import logan.tillman.ecom.entity.Category;
import logan.tillman.ecom.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DtoMapperTest {

    @InjectMocks
    DtoMapper dtoMapper;

    Category category;

    Product product;

    @BeforeEach
    void setup() {
        category = Category.builder()
                .name("Category name")
                .categoryId(1)
                .build();

        product = Product.builder()
                .productId(1)
                .title("Product title")
                .description("A random description")
                .releaseDate(OffsetDateTime.now())
                .categories(List.of(category))
                .build();
    }

    @Test
    @DisplayName("When passed a product, then return an equivalent DTO")
    void mapToProductDtoTest() {
        var productDTO = dtoMapper.mapToProductDTO(product);

        assertThat(productDTO)
                .hasFieldOrPropertyWithValue("productId", product.getProductId())
                .hasFieldOrPropertyWithValue("title", product.getTitle())
                .hasFieldOrPropertyWithValue("description", product.getDescription())
                .hasFieldOrPropertyWithValue("releaseDate", product.getReleaseDate())
                .extracting(ProductDTO::getCategories)
                .asList()
                .first()
                .hasFieldOrPropertyWithValue("name", category.getName())
                .hasFieldOrPropertyWithValue("categoryId", category.getCategoryId());
    }

    @Test
    @DisplayName("When passed a category, then return an equivalent DTO")
    void mapToCategoryDtoTest() {
        var categoryDTO = dtoMapper.mapToCategoryDTO(category);

        assertThat(categoryDTO)
                .hasFieldOrPropertyWithValue("name", category.getName())
                .hasFieldOrPropertyWithValue("categoryId", category.getCategoryId());
    }
}
