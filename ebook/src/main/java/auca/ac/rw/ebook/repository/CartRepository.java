package auca.ac.rw.ebook.repository;

import auca.ac.rw.ebook.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    
    // findBy... queries - FIXED: Using @Query for snake_case columns
    @Query("SELECT c FROM Cart c WHERE c.user.user_id = :userId")
    List<Cart> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT c FROM Cart c WHERE c.user.user_id = :userId AND c.book.book_id = :bookId")
    Optional<Cart> findByUserIdAndBookId(@Param("userId") UUID userId, @Param("bookId") UUID bookId);
    
    @Query("SELECT c FROM Cart c WHERE c.user.user_id = :userId AND c.quantity > :quantity")
    List<Cart> findByUserIdAndQuantityGreaterThan(@Param("userId") UUID userId, @Param("quantity") Integer quantity);
    
    // existsBy... queries
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cart c WHERE c.user.user_id = :userId AND c.book.book_id = :bookId")
    Boolean existsByUserIdAndBookId(@Param("userId") UUID userId, @Param("bookId") UUID bookId);
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cart c WHERE c.user.user_id = :userId")
    Boolean existsByUserId(@Param("userId") UUID userId);
    
    // Sorting and Pagination
    @Query("SELECT c FROM Cart c WHERE c.user.user_id = :userId")
    Page<Cart> findByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    Page<Cart> findAll(Pageable pageable);
    
    // Custom queries
    @Query("SELECT c FROM Cart c WHERE c.user.user_id = :userId ORDER BY c.added_at DESC")
    List<Cart> findUserCartByDateDesc(@Param("userId") UUID userId);
    
    @Query("SELECT SUM(c.quantity) FROM Cart c WHERE c.user.user_id = :userId")
    Integer countTotalItemsInCart(@Param("userId") UUID userId);
    
    @Query("SELECT SUM(c.quantity * b.price) FROM Cart c JOIN c.book b WHERE c.user.user_id = :userId")
    Double calculateCartTotal(@Param("userId") UUID userId);
    
    @Query("SELECT c FROM Cart c WHERE c.user.user_id = :userId AND c.quantity > :minQuantity")
    List<Cart> findUserCartItemsWithMinimumQuantity(@Param("userId") UUID userId, @Param("minQuantity") Integer minQuantity);
    
    // Delete operations
    @Query("DELETE FROM Cart c WHERE c.user.user_id = :userId AND c.book.book_id = :bookId")
    void deleteByUserIdAndBookId(@Param("userId") UUID userId, @Param("bookId") UUID bookId);
    
    @Query("DELETE FROM Cart c WHERE c.user.user_id = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
}