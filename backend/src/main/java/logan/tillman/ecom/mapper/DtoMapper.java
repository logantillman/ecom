package logan.tillman.ecom.mapper;

import logan.tillman.ecom.dto.CategoryDTO;
import logan.tillman.ecom.dto.ProductDTO;
import logan.tillman.ecom.entity.Category;
import logan.tillman.ecom.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DtoMapper {
    public ProductDTO mapToProductDTO(Product product) {
        List<CategoryDTO> categories = null;
        if (product.getCategories() != null) {
            categories = product.getCategories().stream().map(this::mapToCategoryDTO).toList();
        }

        return ProductDTO.builder()
                .productId(product.getProductId())
                .title(product.getTitle())
                .description(product.getDescription())
                .releaseDate(product.getReleaseDate())
                .categories(categories)
                .build();
    }

    public CategoryDTO mapToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .build();
    }
}
