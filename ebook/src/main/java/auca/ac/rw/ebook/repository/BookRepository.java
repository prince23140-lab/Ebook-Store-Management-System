package auca.ac.rw.ebook.repository;

import auca.ac.rw.ebook.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    
    // findBy... queries - FIXED: Using @Query for snake_case columns
    Optional<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByTitleContaining(String title);
    List<Book> findByAuthorContaining(String author);
    
    @Query("SELECT b FROM Book b WHERE b.price BETWEEN :minPrice AND :maxPrice")
    List<Book> findByPriceBetween(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT b FROM Book b WHERE b.stock_quantity > :quantity")
    List<Book> findByStockQuantityGreaterThan(@Param("quantity") Integer quantity);
    
    // existsBy... queries
    Boolean existsByTitle(String title);
    Boolean existsByTitleAndAuthor(String title, String author);
    Boolean existsByAuthor(String author);
    
    // Category-based queries
    @Query("SELECT b FROM Book b WHERE b.category.category_id = :categoryId")
    List<Book> findByCategoryId(@Param("categoryId") UUID categoryId);
    
    @Query("SELECT b FROM Book b WHERE b.category.category_name = :categoryName")
    List<Book> findByCategoryName(@Param("categoryName") String categoryName);
    
    // Sorting and Pagination
    Page<Book> findByAuthor(String author, Pageable pageable);
    Page<Book> findByTitleContaining(String title, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.price BETWEEN :minPrice AND :maxPrice")
    Page<Book> findByPriceBetween(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);
    
    Page<Book> findAll(Pageable pageable);
    
    // Custom search queries
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchByTitleOrAuthor(@Param("keyword") String keyword);
    
    @Query("SELECT b FROM Book b WHERE b.stock_quantity > 0")
    List<Book> findAvailableBooks();
    
    // Statistics queries
    @Query("SELECT COUNT(b) FROM Book b")
    Long countTotalBooks();
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.stock_quantity > 0")
    Long countAvailableBooks();
}