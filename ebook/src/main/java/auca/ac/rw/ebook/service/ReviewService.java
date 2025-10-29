// ReviewService.java - COMPLETE FIXED VERSION
package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.Review;
import auca.ac.rw.ebook.model.User;
import auca.ac.rw.ebook.model.Book;
import auca.ac.rw.ebook.repository.ReviewRepository;
import auca.ac.rw.ebook.repository.UserRepository;
import auca.ac.rw.ebook.repository.BookRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;
    
    // CRUD Operations - FIXED VERSION
    public String saveReview(Review review) {
        // FIXED: Load actual entities from database
        if (review.getUser() == null || review.getUser().getUser_id() == null) {
            return "User is required for review";
        }
        if (review.getBook() == null || review.getBook().getBook_id() == null) {
            return "Book is required for review";
        }
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            return "Rating must be between 1 and 5";
        }
        
        UUID userId = review.getUser().getUser_id();
        UUID bookId = review.getBook().getBook_id();
        
        // Check if user and book actually exist
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        
        if (userOpt.isEmpty()) {
            return "User not found with ID: " + userId;
        }
        if (bookOpt.isEmpty()) {
            return "Book not found with ID: " + bookId;
        }
        
        // Check if user has already reviewed this book
        if (!reviewRepository.existsByUserIdAndBookId(userId, bookId)) {
            // Set the actual entities, not just IDs
            review.setUser(userOpt.get());
            review.setBook(bookOpt.get());
            review.setCreated_at(LocalDateTime.now());
            reviewRepository.save(review);
            return "Review saved successfully";
        } else {
            return "You have already reviewed this book";
        }
    }

    // NEW: Save review with user and book IDs directly
    public String saveReviewWithIds(UUID userId, UUID bookId, Review review) {
        // Validate rating
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            return "Rating must be between 1 and 5";
        }
        
        // Check if user and book exist
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        
        if (userOpt.isEmpty()) {
            return "User not found with ID: " + userId;
        }
        if (bookOpt.isEmpty()) {
            return "Book not found with ID: " + bookId;
        }
        
        // Check if user has already reviewed this book
        if (!reviewRepository.existsByUserIdAndBookId(userId, bookId)) {
            review.setUser(userOpt.get());
            review.setBook(bookOpt.get());
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
        // FIXED: Load actual entities from database
        if (review.getUser() == null || review.getUser().getUser_id() == null) {
            return "User is required for review";
        }
        if (review.getBook() == null || review.getBook().getBook_id() == null) {
            return "Book is required for review";
        }
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            return "Rating must be between 1 and 5";
        }
        
        UUID userId = review.getUser().getUser_id();
        UUID bookId = review.getBook().getBook_id();
        
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        
        if (userOpt.isEmpty()) {
            return "User not found with ID: " + userId;
        }
        if (bookOpt.isEmpty()) {
            return "Book not found with ID: " + bookId;
        }
        
        if (reviewRepository.existsById(review.getReview_id())) {
            review.setUser(userOpt.get());
            review.setBook(bookOpt.get());
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