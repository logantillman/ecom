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

    CategoryDTO categoryDTO;

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
        @DisplayName("When categoryService returns a list, then send the list in a response entity")
        void getCategoriesTest() {
            when(categoryService.getAllCategories()).thenReturn(List.of(categoryDTO));

            var response = categoryController.getCategories();

            verify(categoryService, times(1)).getAllCategories();
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(List.of(categoryDTO));
        }

        @Test
        @DisplayName("When categoryService returns an empty list, then return a bad request")
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
        @DisplayName("When categoryService returns the new category, then send it in a response entity")
        void createCategoryTest() {
            when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(categoryDTO);

            var response = categoryController.createCategory(categoryDTO);

            verify(categoryService, times(1)).createCategory(any(CategoryDTO.class));
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(categoryDTO);
        }

        @Test
        @DisplayName("When categoryService returns null, then return a bad request")
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
        @DisplayName("When categoryService returns the category, then send it in a response entity")
        void getCategoryTest() {
            when(categoryService.getCategory(anyInt())).thenReturn(categoryDTO);

            var response = categoryController.getCategory(1);

            verify(categoryService, times(1)).getCategory(anyInt());
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(categoryDTO);
        }

        @Test
        @DisplayName("When categoryService returns null, then return not found")
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
        @DisplayName("When categoryService returns the updated category, then send it in a response entity")
        void updateCategoryTest() {
            when(categoryService.updateCategory(anyInt(), any(CategoryDTO.class))).thenReturn(categoryDTO);

            var response = categoryController.updateCategory(1, categoryDTO);

            verify(categoryService, times(1)).updateCategory(anyInt(), any(CategoryDTO.class));
            verifyNoMoreInteractions(categoryService);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(categoryDTO);
        }

        @Test
        @DisplayName("When categoryService returns null, then return a bad request")
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
