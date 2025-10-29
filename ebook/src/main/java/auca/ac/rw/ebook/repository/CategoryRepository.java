package auca.ac.rw.ebook.repository;

import auca.ac.rw.ebook.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    
    // FIXED: Using @Query for snake_case column
    @Query("SELECT c FROM Category c WHERE c.category_name = :categoryName")
    Optional<Category> findByCategoryName(@Param("categoryName") String categoryName);
    
    @Query("SELECT c FROM Category c WHERE c.category_name LIKE %:name%")
    List<Category> findByCategoryNameContaining(@Param("name") String name);
    
    // FIXED: Using @Query for exists check
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.category_name = :categoryName")
    Boolean existsByCategoryName(@Param("categoryName") String categoryName);
    
    // Sorting and Pagination
    @Query("SELECT c FROM Category c WHERE c.category_name LIKE %:name%")
    Page<Category> findByCategoryNameContaining(@Param("name") String name, Pageable pageable);
    
    Page<Category> findAll(Pageable pageable);
    
    // Custom query to check if category has books
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Book b WHERE b.category.category_id = :categoryId")
    Boolean hasBooks(@Param("categoryId") UUID categoryId);
}