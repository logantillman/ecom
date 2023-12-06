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
        var productDTO = ProductDTO.builder()
                .productId(product.getProductId())
                .title(product.getTitle())
                .description(product.getDescription())
                .releaseDate(product.getReleaseDate())
                .build();

        if (product.getCategories() != null) {
            product.getCategories().forEach(category -> category.setProducts(null));
            var categories = product.getCategories().stream().map(this::mapToCategoryDTO).toList();
            productDTO.setCategories(categories);
        }

        return productDTO;
    }

    public CategoryDTO mapToCategoryDTO(Category category) {
        var categoryDTO = CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .build();

        if (category.getProducts() != null) {
            category.getProducts().forEach(product -> product.setCategories(null));
            var products = category.getProducts().stream().map(this::mapToProductDTO).toList();
            categoryDTO.setProducts(products);
        }

        return categoryDTO;
    }
}
