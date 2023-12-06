package logan.tillman.ecom.service;

import logan.tillman.ecom.dao.CategoryRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    DtoMapper dtoMapper;

    @InjectMocks
    CategoryService categoryService;

    private Product product;
    private ProductDTO productDTO;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setup() {
        var currentDate = OffsetDateTime.now();

        product = Product.builder()
                .title("Product title")
                .productId(1)
                .description("A good product")
                .releaseDate(currentDate)
                .build();

        productDTO = ProductDTO.builder()
                .title("Product title")
                .productId(1)
                .description("A good product")
                .releaseDate(currentDate)
                .build();

        category = Category.builder()
                .name("Category name")
                .categoryId(1)
                .products(List.of(product))
                .build();

        categoryDTO = CategoryDTO.builder()
                .name("Category name")
                .categoryId(1)
                .products(List.of(productDTO))
                .build();
    }

    @Nested
    @DisplayName("getCategories Test Suite")
    class GetCategoriesTests {

        @Test
        @DisplayName("When categories are fetched, then map the entities to DTOs")
        void getAllCategoriesTest() {
            when(categoryRepository.findAll()).thenReturn(List.of(category));
            when(dtoMapper.mapToCategoryDTO(any(Category.class))).thenCallRealMethod();
            when(dtoMapper.mapToProductDTO(any(Product.class))).thenCallRealMethod();

            var foundCategories = categoryService.getAllCategories();

            verify(categoryRepository, times(1)).findAll();
            verifyNoMoreInteractions(categoryRepository);

            verify(dtoMapper, times(1)).mapToCategoryDTO(any(Category.class));
            verify(dtoMapper, times(1)).mapToCategoryDTO(any(Category.class));
            verifyNoMoreInteractions(dtoMapper);

            assertThat(foundCategories)
                    .hasSize(1)
                    .first()
                    .hasFieldOrPropertyWithValue("categoryId", category.getCategoryId())
                    .hasFieldOrPropertyWithValue("name", category.getName())
                    .extracting(CategoryDTO::getProducts)
                    .asList()
                    .first()
                    .isEqualTo(productDTO);
        }

        @Test
        @DisplayName("When a category is fetched, then map it to the DTO")
        void getCategoryTest() {
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
            when(dtoMapper.mapToCategoryDTO(any(Category.class))).thenCallRealMethod();
            when(dtoMapper.mapToProductDTO(any(Product.class))).thenCallRealMethod();

            var foundCategory = categoryService.getCategory(1);

            verify(categoryRepository, times(1)).findById(anyInt());
            verifyNoMoreInteractions(categoryRepository);

            verify(dtoMapper, times(1)).mapToCategoryDTO(any(Category.class));
            verify(dtoMapper, times(1)).mapToProductDTO(any(Product.class));
            verifyNoMoreInteractions(dtoMapper);

            assertThat(foundCategory)
                    .hasFieldOrPropertyWithValue("categoryId", category.getCategoryId())
                    .hasFieldOrPropertyWithValue("name", category.getName())
                    .extracting(CategoryDTO::getProducts)
                    .asList()
                    .first()
                    .isEqualTo(productDTO);;
        }

        @Test
        @DisplayName("When no category is found, then return null")
        void getCategoryNotFoundTest() {
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

            var foundCategory = categoryService.getCategory(1);

            verify(categoryRepository, times(1)).findById(anyInt());
            verifyNoMoreInteractions(categoryRepository);

            verifyNoInteractions(dtoMapper);

            assertThat(foundCategory).isNull();
        }
    }

    @Nested
    @DisplayName("createCategory Test Suite")
    class CreateCategoryTests {

        @Test
        @DisplayName("When a category is created, then map the entity to the DTO")
        void createCategoryTest() {
            when(categoryRepository.saveAndFlush(any(Category.class))).thenReturn(category);
            when(dtoMapper.mapToCategoryDTO(any(Category.class))).thenCallRealMethod();
            when(dtoMapper.mapToProductDTO(any(Product.class))).thenCallRealMethod();

            var createdCategory = categoryService.createCategory(categoryDTO);

            verify(categoryRepository, times(1)).saveAndFlush(any(Category.class));
            verifyNoMoreInteractions(categoryRepository);

            verify(dtoMapper, times(1)).mapToCategoryDTO(any(Category.class));
            verify(dtoMapper, times(1)).mapToProductDTO(any(Product.class));
            verifyNoMoreInteractions(dtoMapper);

            assertThat(createdCategory)
                    .hasFieldOrPropertyWithValue("categoryId", categoryDTO.getCategoryId())
                    .hasFieldOrPropertyWithValue("name", categoryDTO.getName())
                    .extracting(CategoryDTO::getProducts)
                    .asList()
                    .first()
                    .isEqualTo(productDTO);
        }

        @Test
        @DisplayName("When a category is created with a null name, then return null")
        void createCategoryWithNullNameTest() {
            categoryDTO.setName(null);

            var createdCategory = categoryService.createCategory(categoryDTO);

            verifyNoInteractions(categoryRepository);
            verifyNoInteractions(dtoMapper);

            assertThat(createdCategory).isNull();
        }

        @Test
        @DisplayName("When a category is created with an empty name, then return null")
        void createCategoryWithEmptyNameTest() {
            categoryDTO.setName("");

            var createdCategory = categoryService.createCategory(categoryDTO);

            verifyNoInteractions(categoryRepository);
            verifyNoInteractions(dtoMapper);

            assertThat(createdCategory).isNull();
        }
    }

    @Nested
    @DisplayName("updateCategory Test Suite")
    class UpdateCategoryTests {

        @Test
        @DisplayName("When a category is updated, then map the entity to the DTO")
        void updateCategoryTest() {
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
            when(dtoMapper.mapToCategoryDTO(any(Category.class))).thenCallRealMethod();
            when(dtoMapper.mapToProductDTO(any(Product.class))).thenCallRealMethod();

            var updatedCategory = categoryService.updateCategory(1, categoryDTO);

            verify(categoryRepository, times(1)).findById(anyInt());
            verify(categoryRepository, times(1)).saveAndFlush(any(Category.class));
            verifyNoMoreInteractions(categoryRepository);

            verify(dtoMapper, times(1)).mapToCategoryDTO(any(Category.class));
            verify(dtoMapper, times(1)).mapToProductDTO(any(Product.class));
            verifyNoMoreInteractions(dtoMapper);

            assertThat(updatedCategory)
                    .hasFieldOrPropertyWithValue("categoryId", category.getCategoryId())
                    .hasFieldOrPropertyWithValue("name", category.getName())
                    .extracting(CategoryDTO::getProducts)
                    .asList()
                    .first()
                    .isEqualTo(productDTO);
        }

        @Test
        @DisplayName("When a category is updated with null name, then return null")
        void updateCategoryWithNullNameTest() {
            categoryDTO.setName(null);

            var updatedCategory = categoryService.updateCategory(1, categoryDTO);

            verifyNoInteractions(categoryRepository);
            verifyNoInteractions(dtoMapper);

            assertThat(updatedCategory).isNull();
        }

        @Test
        @DisplayName("When a category is updated with an empty name, then return null")
        void updateCategoryWithEmptyNameTest() {
            categoryDTO.setName("");

            var updatedCategory = categoryService.updateCategory(1, categoryDTO);

            verifyNoInteractions(categoryRepository);
            verifyNoInteractions(dtoMapper);

            assertThat(updatedCategory).isNull();
        }

        @Test
        @DisplayName("When no category is found to update, then return null")
        void updateCategoryWithNoCategoryFound() {
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

            var updatedCategory = categoryService.updateCategory(1, categoryDTO);

            verify(categoryRepository, times(1)).findById(anyInt());
            verifyNoMoreInteractions(categoryRepository);

            verifyNoInteractions(dtoMapper);

            assertThat(updatedCategory).isNull();
        }
    }
}
