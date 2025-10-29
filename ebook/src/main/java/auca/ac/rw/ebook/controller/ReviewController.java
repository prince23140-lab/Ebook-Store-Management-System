// ReviewController.java
package auca.ac.rw.ebook.controller;

import auca.ac.rw.ebook.model.Review;
import auca.ac.rw.ebook.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    // CRUD Endpoints
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveReview(@RequestBody Review review) {
        String response = reviewService.saveReview(review);
        if (response.equals("Review saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getReviewById(@PathVariable UUID id) {
        Optional<Review> review = reviewService.getReviewById(id);
        if (review.isPresent()) {
            return new ResponseEntity<>(review.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Review not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateReview(@RequestBody Review review) {
        String response = reviewService.updateReview(review);
        if (response.equals("Review updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable UUID id) {
        String response = reviewService.deleteReview(id);
        if (response.equals("Review deleted successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // findBy... Endpoints
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable UUID userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
    
    @GetMapping(value = "/book/{bookId}")
    public ResponseEntity<List<Review>> getReviewsByBookId(@PathVariable UUID bookId) {
        List<Review> reviews = reviewService.getReviewsByBookId(bookId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
    
    @GetMapping(value = "/rating/{rating}")
    public ResponseEntity<List<Review>> getReviewsByRating(@PathVariable Integer rating) {
        List<Review> reviews = reviewService.getReviewsByRating(rating);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
    
    // existsBy... Endpoints
    @GetMapping(value = "/exists/user/{userId}/book/{bookId}")
    public ResponseEntity<Boolean> checkUserHasReviewedBook(
            @PathVariable UUID userId, 
            @PathVariable UUID bookId) {
        Boolean exists = reviewService.checkUserHasReviewedBook(userId, bookId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Sorting and Pagination Endpoints
    @GetMapping(value = "/book/{bookId}/page")
    public ResponseEntity<Page<Review>> getReviewsByBookIdWithPagination(
            @PathVariable UUID bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created_at") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Review> reviews = reviewService.getReviewsByBookIdWithPagination(bookId, pageable);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
    
    // Business logic Endpoints
    @GetMapping(value = "/book/{bookId}/average-rating")
    public ResponseEntity<Double> getAverageRatingForBook(@PathVariable UUID bookId) {
        Double averageRating = reviewService.calculateAverageRatingForBook(bookId);
        return new ResponseEntity<>(averageRating, HttpStatus.OK);
    }
    
    @GetMapping(value = "/book/{bookId}/sorted")
    public ResponseEntity<List<Review>> getBookReviewsSorted(@PathVariable UUID bookId) {
        List<Review> reviews = reviewService.getBookReviewsSorted(bookId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
    
    // Statistics endpoints
    @GetMapping(value = "/book/{bookId}/count")
    public ResponseEntity<Long> getReviewsCountForBook(@PathVariable UUID bookId) {
        Long count = reviewService.getReviewsCountForBook(bookId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    @GetMapping(value = "/stats/by-rating")
    public ResponseEntity<List<Object[]>> getReviewsCountByRating() {
        List<Object[]> ratingStats = reviewService.getReviewsCountByRating();
        return new ResponseEntity<>(ratingStats, HttpStatus.OK);
    }
    
    @GetMapping(value = "/best-rated-books")
    public ResponseEntity<List<Object[]>> getBestRatedBooks(
            @RequestParam(defaultValue = "4.0") Double minRating) {
        List<Object[]> bestRatedBooks = reviewService.getBooksWithAverageRatingAbove(minRating);
        return new ResponseEntity<>(bestRatedBooks, HttpStatus.OK);
    }
    
    @GetMapping(value = "/most-reviewed-books")
    public ResponseEntity<List<Object[]>> getMostReviewedBooks() {
        List<Object[]> mostReviewedBooks = reviewService.getMostReviewedBooks();
        return new ResponseEntity<>(mostReviewedBooks, HttpStatus.OK);
    }
}