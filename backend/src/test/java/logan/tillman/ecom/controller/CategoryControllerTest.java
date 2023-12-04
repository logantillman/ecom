package logan.tillman.ecom.controller;

import logan.tillman.ecom.dto.CategoryDTO;
import logan.tillman.ecom.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void setup() {
        categoryDTO = CategoryDTO.builder()
                .categoryId(1)
                .name("A random category")
                .build();
    }

    @Nested
    @DisplayName("getCategories Test Suite")
    class GetAllCategoriesTests {

        @Test
        @DisplayName("When categories are fetched, then send the list in an OK response")
        void getCategoriesTest() {
            when(categoryService.getAllCategories()).thenReturn(List.of(categoryDTO));

            var response = categoryController.getCategories();

            verify(categoryService, times(1)).getAllCategories();
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(List.of(categoryDTO));
        }

        @Test
        @DisplayName("When no categories are found, then send a NOT_FOUND response")
        void getCategoriesWithEmptyResponseTest() {
            when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());

            var response = categoryController.getCategories();

            verify(categoryService, times(1)).getAllCategories();
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNull();
        }
    }

    @Nested
    @DisplayName("createCategory Test Suite")
    class CreateCategoryTests {

        @Test
        @DisplayName("When a new category is created, then send the new category in an OK response")
        void createCategoryTest() {
            when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(categoryDTO);

            var response = categoryController.createCategory(categoryDTO);

            verify(categoryService, times(1)).createCategory(any(CategoryDTO.class));
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(categoryDTO);
        }

        @Test
        @DisplayName("When failing to create a new category, then send a BAD_REQUEST response")
        void createCategoryWithNullResponseTest() {
            when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(null);

            var response = categoryController.createCategory(categoryDTO);

            verify(categoryService, times(1)).createCategory(any(CategoryDTO.class));
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNull();
        }
    }

    @Nested
    @DisplayName("getCategory Test Suite")
    class GetCategoryTests {

        @Test
        @DisplayName("When a category is fetched, then send it in an OK response")
        void getCategoryTest() {
            when(categoryService.getCategory(anyInt())).thenReturn(categoryDTO);

            var response = categoryController.getCategory(1);

            verify(categoryService, times(1)).getCategory(anyInt());
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(categoryDTO);
        }

        @Test
        @DisplayName("When the category is not found, then send a NOT_FOUND response")
        void getCategoryWithNullResponseTest() {
            when(categoryService.getCategory(anyInt())).thenReturn(null);

            var response = categoryController.getCategory(1);

            verify(categoryService, times(1)).getCategory(anyInt());
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNull();
        }
    }

    @Nested
    @DisplayName("updateCategory Test Suite")
    class UpdateCategoryTests {

        @Test
        @DisplayName("When a category is updated, then send the updated category in an OK response")
        void updateCategoryTest() {
            when(categoryService.updateCategory(anyInt(), any(CategoryDTO.class))).thenReturn(categoryDTO);

            var response = categoryController.updateCategory(1, categoryDTO);

            verify(categoryService, times(1)).updateCategory(anyInt(), any(CategoryDTO.class));
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(categoryDTO);
        }

        @Test
        @DisplayName("When a category fails to update, then return a BAD_REQUEST response")
        void updateCategoryWithNullResponseTest() {
            when(categoryService.updateCategory(anyInt(), any(CategoryDTO.class))).thenReturn(null);

            var response = categoryController.updateCategory(1, categoryDTO);

            verify(categoryService, times(1)).updateCategory(anyInt(), any(CategoryDTO.class));
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNull();
        }
    }
}
