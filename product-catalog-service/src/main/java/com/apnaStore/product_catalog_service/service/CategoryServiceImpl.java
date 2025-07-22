package com.apnaStore.product_catalog_service.service;

import com.apnaStore.product_catalog_service.dto.request.CategoryRequest;
import com.apnaStore.product_catalog_service.dto.response.CategoryResponse;
import com.apnaStore.product_catalog_service.entities.Category;
import com.apnaStore.product_catalog_service.exception.DataAlreadyExist;
import com.apnaStore.product_catalog_service.exception.DataNotFoundException;
import com.apnaStore.product_catalog_service.helper.EntityToResponse;
import com.apnaStore.product_catalog_service.helper.RequestToEntity;
import com.apnaStore.product_catalog_service.messages.ExceptionMessages;
import com.apnaStore.product_catalog_service.repository.CategoryRepository;
import com.apnaStore.product_catalog_service.service.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        categoryRepository.findByName(categoryRequest.getName())
                .ifPresent(ex -> {
                    throw new DataAlreadyExist(ExceptionMessages.CATEGORY_ALREADY_PRESENT);
                });
        Category category = RequestToEntity.requestToCategory(categoryRequest);
        category = categoryRepository.save(category);
        log.info("Saved Category :: {}", category);
        return EntityToResponse.categoryToResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategory(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Category> content = categoryRepository.findAll(pageable).getContent();
        if (content.isEmpty())
            throw new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND);
        return content
                .stream()
                .map(EntityToResponse::categoryToResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        return EntityToResponse.categoryToResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));

        log.info("Updated id :: {}", id);
        category.setName(categoryRequest.getName() != null
                ? categoryRequest.getName()
                : category.getName());
        category.setDescription(categoryRequest.getDescription() != null
                ? categoryRequest.getDescription()
                : category.getDescription());
        return EntityToResponse.categoryToResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        log.info("Category Deleted Successfully :: {}", category);
        // Here if i delete the category product will delete as well and also inventory should be delete of those products.(kafka)
        categoryRepository.delete(category);
    }
}
