package logan.tillman.ecom.service;

import logan.tillman.ecom.dao.CategoryRepository;
import logan.tillman.ecom.dto.CategoryDTO;
import logan.tillman.ecom.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        var categories = categoryRepository.findAll();
        return categories.stream().map(this::mapToCategoryDTO).collect(Collectors.toList());
    }

    public CategoryDTO getCategory(Integer categoryId) {
        var category = categoryRepository.findById(categoryId);

        if (category.isPresent()) {
            return mapToCategoryDTO(category.get());
        } else {
            log.info("Unable to find product with id {}", categoryId);
            return null;
        }
    }

    private CategoryDTO mapToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .build();
    }
}
