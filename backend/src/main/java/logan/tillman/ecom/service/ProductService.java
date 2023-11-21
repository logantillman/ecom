package logan.tillman.ecom.service;

import logan.tillman.ecom.dao.ProductRepository;
import logan.tillman.ecom.dto.CategoryDTO;
import logan.tillman.ecom.dto.ProductDTO;
import logan.tillman.ecom.entity.Category;
import logan.tillman.ecom.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productDTO.getTitle() == null || productDTO.getTitle().isEmpty()) {
            log.error("Unable to create product with empty title");
            return null;
        }

        var product = Product.builder()
                .title(productDTO.getTitle())
                .description(productDTO.getDescription())
                .releaseDate(productDTO.getReleaseDate())
                .build();
        return mapToProductDTO(productRepository.saveAndFlush(product));
    }

    public ProductDTO getProduct(Integer productId) {
        log.info("Finding product with id {}", productId);
        var product = productRepository.findById(productId);

        if (product.isPresent()) {
            return mapToProductDTO(product.get());
        } else {
            log.info("Unable to find product with id {}", productId);
            return null;
        }
    }

    public ProductDTO updateProduct(Integer productId, Product productToUpdate) {
        var existingProduct = productRepository.findById(productId);

        if (existingProduct.isPresent()) {
            var updatedProduct = existingProduct.map(product -> {
                product.setTitle(productToUpdate.getTitle());
                product.setDescription(productToUpdate.getDescription());
                product.setReleaseDate(productToUpdate.getReleaseDate());

//                var currentCategoriesMap = product.getCategories().stream()
//                        .collect(Collectors.toMap(Category::getCategoryId, category -> category));
//                var modifiedCategories = productDTO.getCategories().stream()
//                        .filter(category -> )
                product.setCategories(product.getCategories()); // TODO do testing

                return productRepository.save(product);
            });
            return mapToProductDTO(updatedProduct.get());
        } else {
            log.info("Unable to find product with id {}", productId);
            return null;
        }
    }

    private ProductDTO mapToProductDTO(Product product) {
        List<CategoryDTO> categories = null;
        if (product.getCategories() != null) {
            categories = product.getCategories().stream().map(this::mapToCategoryDTO).collect(Collectors.toList());
        }

        return ProductDTO.builder()
                .productId(product.getProductId())
                .title(product.getTitle())
                .description(product.getDescription())
                .releaseDate(product.getReleaseDate())
                .categories(categories)
                .build();
    }

    private CategoryDTO mapToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .build();
    }
}
