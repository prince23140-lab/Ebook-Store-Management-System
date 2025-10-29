// CategoryService.java
package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.Category;
import auca.ac.rw.ebook.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    // CRUD Operations
    public String saveCategory(Category category) {
        if (!categoryRepository.existsByCategoryName(category.getCategory_name())) {
            categoryRepository.save(category);
            return "Category saved successfully";
        } else {
            return "Category with this name already exists";
        }
    }
    
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }
    
    public String updateCategory(Category category) {
        if (categoryRepository.existsById(category.getCategory_id())) {
            categoryRepository.save(category);
            return "Category updated successfully";
        } else {
            return "Category not found";
        }
    }
    
    public String deleteCategory(UUID id) {
        if (categoryRepository.existsById(id)) {
            // Check if category has books before deleting
            if (categoryRepository.hasBooks(id)) {
                return "Cannot delete category: It has associated books";
            }
            categoryRepository.deleteById(id);
            return "Category deleted successfully";
        } else {
            return "Category not found";
        }
    }
    
    // findBy... methods
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByCategoryName(name);
    }
    
    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByCategoryNameContaining(name);
    }
    
    // existsBy... methods
    public Boolean checkCategoryExists(String name) {
        return categoryRepository.existsByCategoryName(name);
    }
    
    // Sorting and Pagination
    public Page<Category> searchCategoriesByNameWithPagination(String name, Pageable pageable) {
        return categoryRepository.findByCategoryNameContaining(name, pageable);
    }
    
    public Page<Category> getAllCategoriesWithPagination(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    
    // Business logic methods
    public Boolean checkCategoryHasBooks(UUID categoryId) {
        return categoryRepository.hasBooks(categoryId);
    }
    
    public Long getTotalCategoriesCount() {
        return categoryRepository.count();
    }
    
    // Validation methods
    public Boolean validateCategoryName(String categoryName) {
        return categoryName != null && !categoryName.trim().isEmpty() && categoryName.length() >= 2;
    }
}