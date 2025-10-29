// ReviewController.java - COMPLETE FIXED VERSION
package auca.ac.rw.ebook.controller;

import auca.ac.rw.ebook.model.Review;
import auca.ac.rw.ebook.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    // Save review with entity (your original approach - now fixed)
    @PostMapping("/save")
    public String saveReview(@RequestBody Review review) {
        return reviewService.saveReview(review);
    }
    
    // Alternative: Save review with path variables for IDs
    @PostMapping("/save/user/{userId}/book/{bookId}")
    public String saveReviewWithIds(@PathVariable UUID userId, @PathVariable UUID bookId, @RequestBody Review review) {
        return reviewService.saveReviewWithIds(userId, bookId, review);
    }
    
    // Get all reviews
    @GetMapping("/all")
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }
    
    // Get review by ID
    @GetMapping("/{id}")
    public Optional<Review> getReviewById(@PathVariable UUID id) {
        return reviewService.getReviewById(id);
    }
    
    // Update review
    @PutMapping("/update")
    public String updateReview(@RequestBody Review review) {
        return reviewService.updateReview(review);
    }
    
    // Delete review
    @DeleteMapping("/delete/{id}")
    public String deleteReview(@PathVariable UUID id) {
        return reviewService.deleteReview(id);
    }
    
    // Get reviews by user ID
    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUserId(@PathVariable UUID userId) {
        return reviewService.getReviewsByUserId(userId);
    }
    
    // Get reviews by book ID
    @GetMapping("/book/{bookId}")
    public List<Review> getReviewsByBookId(@PathVariable UUID bookId) {
        return reviewService.getReviewsByBookId(bookId);
    }
    
    // Get reviews by rating
    @GetMapping("/rating/{rating}")
    public List<Review> getReviewsByRating(@PathVariable Integer rating) {
        return reviewService.getReviewsByRating(rating);
    }
    
    // Get reviews by minimum rating
    @GetMapping("/min-rating/{minRating}")
    public List<Review> getReviewsByMinimumRating(@PathVariable Integer minRating) {
        return reviewService.getReviewsByMinimumRating(minRating);
    }
    
    // Get specific review by user and book
    @GetMapping("/user/{userId}/book/{bookId}")
    public Optional<Review> getReviewByUserAndBook(@PathVariable UUID userId, @PathVariable UUID bookId) {
        return reviewService.getReviewByUserAndBook(userId, bookId);
    }
    
    // Check if user has reviewed a book
    @GetMapping("/exists/user/{userId}/book/{bookId}")
    public Boolean checkUserHasReviewedBook(@PathVariable UUID userId, @PathVariable UUID bookId) {
        return reviewService.checkUserHasReviewedBook(userId, bookId);
    }
    
    // Check if book has reviews
    @GetMapping("/exists/book/{bookId}")
    public Boolean checkBookHasReviews(@PathVariable UUID bookId) {
        return reviewService.checkBookHasReviews(bookId);
    }
    
    // Pagination endpoints
    @GetMapping("/user/{userId}/page")
    public Page<Review> getReviewsByUserIdWithPagination(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return reviewService.getReviewsByUserIdWithPagination(userId, pageable);
    }
    
    @GetMapping("/book/{bookId}/page")
    public Page<Review> getReviewsByBookIdWithPagination(
            @PathVariable UUID bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return reviewService.getReviewsByBookIdWithPagination(bookId, pageable);
    }
    
    @GetMapping("/all/page")
    public Page<Review> getAllReviewsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return reviewService.getAllReviewsWithPagination(pageable);
    }
    
    // Business logic endpoints
    @GetMapping("/book/{bookId}/average-rating")
    public Double getAverageRatingForBook(@PathVariable UUID bookId) {
        return reviewService.calculateAverageRatingForBook(bookId);
    }
    
    @GetMapping("/book/{bookId}/sorted")
    public List<Review> getBookReviewsSorted(@PathVariable UUID bookId) {
        return reviewService.getBookReviewsSorted(bookId);
    }
    
    @GetMapping("/book/{bookId}/count")
    public Long getReviewsCountForBook(@PathVariable UUID bookId) {
        return reviewService.getReviewsCountForBook(bookId);
    }
    
    @GetMapping("/stats/rating-distribution")
    public List<Object[]> getReviewsCountByRating() {
        return reviewService.getReviewsCountByRating();
    }
    
    @GetMapping("/stats/books-above-rating/{minRating}")
    public List<Object[]> getBooksWithAverageRatingAbove(@PathVariable Double minRating) {
        return reviewService.getBooksWithAverageRatingAbove(minRating);
    }
    
    @GetMapping("/stats/most-reviewed-books")
    public List<Object[]> getMostReviewedBooks() {
        return reviewService.getMostReviewedBooks();
    }
    
    // Update review rating and comment
    @PutMapping("/update-rating/{reviewId}")
    public String updateReviewRating(
            @PathVariable UUID reviewId,
            @RequestParam Integer newRating,
            @RequestParam(required = false) String newComment) {
        return reviewService.updateReviewRating(reviewId, newRating, newComment);
    }
}