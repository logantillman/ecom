package logan.tillman.ecom.service;

import logan.tillman.ecom.dao.CategoryRepository;
import logan.tillman.ecom.dao.ProductRepository;
import logan.tillman.ecom.dto.CategoryDTO;
import logan.tillman.ecom.dto.ProductDTO;
import logan.tillman.ecom.entity.Product;
import logan.tillman.ecom.mapper.DtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final DtoMapper dtoMapper;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          DtoMapper dtoMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.dtoMapper = dtoMapper;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productDTO.getTitle() == null || productDTO.getTitle().isEmpty()) {
            log.error("Unable to create product with empty title");
            return null;
        }

        var categoriesToFetch = productDTO.getCategories()
                .stream()
                .map(CategoryDTO::getCategoryId)
                .collect(Collectors.toSet());

        var categories = categoryRepository.findAllById(categoriesToFetch);

        var product = Product.builder()
                .title(productDTO.getTitle())
                .description(productDTO.getDescription())
                .releaseDate(productDTO.getReleaseDate())
                .categories(categories)
                .build();
        return dtoMapper.mapToProductDTO(productRepository.saveAndFlush(product));
    }

    public ProductDTO getProduct(Integer productId) {
        log.info("Finding product with id {}", productId);
        var product = productRepository.findById(productId);

        if (product.isPresent()) {
            return dtoMapper.mapToProductDTO(product.get());
        } else {
            log.info("Unable to find product with id {}", productId);
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDTO updateProduct(Integer productId, ProductDTO updatedProduct) {
        if (updatedProduct.getTitle() == null || updatedProduct.getTitle().isEmpty()) {
            log.error("Unable to update product to have empty title");
            return null;
        }

        var optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            log.info("Updating product with id {}", productId);
            var product = optionalProduct.get();

            product.setTitle(updatedProduct.getTitle());
            product.setDescription(updatedProduct.getDescription());
            product.setReleaseDate(updatedProduct.getReleaseDate());

            var categoryToUpdateIds = updatedProduct.getCategories()
                    .stream()
                    .map(CategoryDTO::getCategoryId)
                    .collect(Collectors.toSet());

            var categories = categoryRepository.findAllById(categoryToUpdateIds);
            product.setCategories(categories);
            productRepository.saveAndFlush(product);

            return dtoMapper.mapToProductDTO(product);
        } else {
            log.info("Unable to find product with id {}", productId);
            return null;
        }
    }
}
