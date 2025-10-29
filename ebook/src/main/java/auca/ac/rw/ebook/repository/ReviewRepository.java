package auca.ac.rw.ebook.repository;

import auca.ac.rw.ebook.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    
    // findBy... queries - FIXED: Using @Query for snake_case columns
    @Query("SELECT r FROM Review r WHERE r.user.user_id = :userId")
    List<Review> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT r FROM Review r WHERE r.book.book_id = :bookId")
    List<Review> findByBookId(@Param("bookId") UUID bookId);
    
    List<Review> findByRating(Integer rating);
    
    @Query("SELECT r FROM Review r WHERE r.rating >= :minRating")
    List<Review> findByRatingGreaterThanEqual(@Param("minRating") Integer minRating);
    
    @Query("SELECT r FROM Review r WHERE r.user.user_id = :userId AND r.book.book_id = :bookId")
    Optional<Review> findByUserIdAndBookId(@Param("userId") UUID userId, @Param("bookId") UUID bookId);
    
    // existsBy... queries
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Review r WHERE r.user.user_id = :userId AND r.book.book_id = :bookId")
    Boolean existsByUserIdAndBookId(@Param("userId") UUID userId, @Param("bookId") UUID bookId);
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Review r WHERE r.book.book_id = :bookId")
    Boolean existsByBookId(@Param("bookId") UUID bookId);
    
    // Sorting and Pagination
    @Query("SELECT r FROM Review r WHERE r.user.user_id = :userId")
    Page<Review> findByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.book.book_id = :bookId")
    Page<Review> findByBookId(@Param("bookId") UUID bookId, Pageable pageable);
    
    Page<Review> findByRating(Integer rating, Pageable pageable);
    
    Page<Review> findAll(Pageable pageable);
    
    // Custom queries
    @Query("SELECT r FROM Review r WHERE r.book.book_id = :bookId ORDER BY r.rating DESC, r.created_at DESC")
    List<Review> findBookReviewsSortedByRatingAndDate(@Param("bookId") UUID bookId);
    
    @Query("SELECT r.book, AVG(r.rating) as avgRating FROM Review r GROUP BY r.book HAVING AVG(r.rating) >= :minRating ORDER BY avgRating DESC")
    List<Object[]> findBooksWithAverageRatingAbove(@Param("minRating") Double minRating);
    
    @Query("SELECT r.book, COUNT(r) as reviewCount FROM Review r GROUP BY r.book ORDER BY reviewCount DESC")
    List<Object[]> findMostReviewedBooks();
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.book_id = :bookId")
    Double calculateAverageRatingForBook(@Param("bookId") UUID bookId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.book.book_id = :bookId")
    Long countReviewsForBook(@Param("bookId") UUID bookId);
    
    // Statistics
    @Query("SELECT r.rating, COUNT(r) FROM Review r GROUP BY r.rating ORDER BY r.rating DESC")
    List<Object[]> countReviewsByRating();
}