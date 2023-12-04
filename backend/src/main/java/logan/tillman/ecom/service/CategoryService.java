package logan.tillman.ecom.service;

import logan.tillman.ecom.dao.CategoryRepository;
import logan.tillman.ecom.dto.CategoryDTO;
import logan.tillman.ecom.entity.Category;
import logan.tillman.ecom.mapper.DtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DtoMapper dtoMapper;

    public CategoryService(CategoryRepository categoryRepository,
                           DtoMapper dtoMapper) {
        this.categoryRepository = categoryRepository;
        this.dtoMapper = dtoMapper;
    }

    public List<CategoryDTO> getAllCategories() {
        var categories = categoryRepository.findAll();
        return categories.stream().map(dtoMapper::mapToCategoryDTO).toList();
    }

    public CategoryDTO getCategory(Integer categoryId) {
        var category = categoryRepository.findById(categoryId);

        if (category.isPresent()) {
            return dtoMapper.mapToCategoryDTO(category.get());
        } else {
            log.info("Unable to find product with id {}", categoryId);
            return null;
        }
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            log.error("Unable to create category with empty name");
            return null;
        }

        var category = Category.builder()
                .name(categoryDTO.getName())
                .build();

        return dtoMapper.mapToCategoryDTO(categoryRepository.saveAndFlush(category));
    }

    public CategoryDTO updateCategory(Integer categoryId, CategoryDTO categoryDTO) {
        if (categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            log.error("Unable to update category to have empty name");
            return null;
        }

        var optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isPresent()) {
            log.info("Updating category with id {}", categoryId);
            var category = optionalCategory.get();

            category.setName(categoryDTO.getName());

            categoryRepository.saveAndFlush(category);

            return dtoMapper.mapToCategoryDTO(category);
        } else {
            log.info("Unable to find category with id {}", categoryId);
            return null;
        }
    }
}
