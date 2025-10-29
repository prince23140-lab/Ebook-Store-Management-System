// ReviewService.java
package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.Review;
import auca.ac.rw.ebook.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    // CRUD Operations
    public String saveReview(Review review) {
        if (!reviewRepository.existsByUserIdAndBookId(review.getUser().getUser_id(), review.getBook().getBook_id())) {
            review.setCreated_at(LocalDateTime.now());
            reviewRepository.save(review);
            return "Review saved successfully";
        } else {
            return "You have already reviewed this book";
        }
    }
    
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
    
    public Optional<Review> getReviewById(UUID id) {
        return reviewRepository.findById(id);
    }
    
    public String updateReview(Review review) {
        if (reviewRepository.existsById(review.getReview_id())) {
            reviewRepository.save(review);
            return "Review updated successfully";
        } else {
            return "Review not found";
        }
    }
    
    public String deleteReview(UUID id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return "Review deleted successfully";
        } else {
            return "Review not found";
        }
    }
    
    // findBy... methods
    public List<Review> getReviewsByUserId(UUID userId) {
        return reviewRepository.findByUserId(userId);
    }
    
    public List<Review> getReviewsByBookId(UUID bookId) {
        return reviewRepository.findByBookId(bookId);
    }
    
    public List<Review> getReviewsByRating(Integer rating) {
        return reviewRepository.findByRating(rating);
    }
    
    public List<Review> getReviewsByMinimumRating(Integer minRating) {
        return reviewRepository.findByRatingGreaterThanEqual(minRating);
    }
    
    public Optional<Review> getReviewByUserAndBook(UUID userId, UUID bookId) {
        return reviewRepository.findByUserIdAndBookId(userId, bookId);
    }
    
    // existsBy... methods
    public Boolean checkUserHasReviewedBook(UUID userId, UUID bookId) {
        return reviewRepository.existsByUserIdAndBookId(userId, bookId);
    }
    
    public Boolean checkBookHasReviews(UUID bookId) {
        return reviewRepository.existsByBookId(bookId);
    }
    
    // Sorting and Pagination
    public Page<Review> getReviewsByUserIdWithPagination(UUID userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable);
    }
    
    public Page<Review> getReviewsByBookIdWithPagination(UUID bookId, Pageable pageable) {
        return reviewRepository.findByBookId(bookId, pageable);
    }
    
    public Page<Review> getReviewsByRatingWithPagination(Integer rating, Pageable pageable) {
        return reviewRepository.findByRating(rating, pageable);
    }
    
    public Page<Review> getAllReviewsWithPagination(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }
    
    // Business logic methods
    public Double calculateAverageRatingForBook(UUID bookId) {
        Double averageRating = reviewRepository.calculateAverageRatingForBook(bookId);
        return averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0; // Round to 1 decimal
    }
    
    public List<Review> getBookReviewsSorted(UUID bookId) {
        return reviewRepository.findBookReviewsSortedByRatingAndDate(bookId);
    }
    
    public List<Object[]> getBooksWithAverageRatingAbove(Double minRating) {
        return reviewRepository.findBooksWithAverageRatingAbove(minRating);
    }
    
    public List<Object[]> getMostReviewedBooks() {
        return reviewRepository.findMostReviewedBooks();
    }
    
    // Statistics methods
    public Long getReviewsCountForBook(UUID bookId) {
        return reviewRepository.countReviewsForBook(bookId);
    }
    
    public List<Object[]> getReviewsCountByRating() {
        return reviewRepository.countReviewsByRating();
    }
    
    // Validation methods
    public Boolean validateReview(Review review) {
        return review.getUser() != null && 
               review.getBook() != null && 
               review.getRating() != null && 
               review.getRating() >= 1 && 
               review.getRating() <= 5;
    }
    
    // Business logic
    public String updateReviewRating(UUID reviewId, Integer newRating, String newComment) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            review.setRating(newRating);
            if (newComment != null) {
                review.setComment(newComment);
            }
            review.setCreated_at(LocalDateTime.now());
            reviewRepository.save(review);
            return "Review updated successfully";
        } else {
            return "Review not found";
        }
    }
}