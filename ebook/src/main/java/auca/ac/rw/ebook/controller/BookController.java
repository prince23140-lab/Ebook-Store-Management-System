// BookController.java
package auca.ac.rw.ebook.controller;

import auca.ac.rw.ebook.model.Book;
import auca.ac.rw.ebook.service.BookService;
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
@RequestMapping("/api/book")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    // CRUD Endpoints
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveBook(@RequestBody Book book) {
        String response = bookService.saveBook(book);
        if (response.equals("Book saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBookById(@PathVariable UUID id) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBook(@RequestBody Book book) {
        String response = bookService.updateBook(book);
        if (response.equals("Book updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable UUID id) {
        String response = bookService.deleteBook(id);
        if (response.equals("Book deleted successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // findBy... Endpoints
    @GetMapping(value = "/title/{title}")
    public ResponseEntity<?> getBookByTitle(@PathVariable String title) {
        Optional<Book> book = bookService.getBookByTitle(title);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping(value = "/author/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
        List<Book> books = bookService.getBooksByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping(value = "/search/{title}")
    public ResponseEntity<List<Book>> searchBooksByTitle(@PathVariable String title) {
        List<Book> books = bookService.searchBooksByTitle(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping(value = "/category/{categoryId}")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable UUID categoryId) {
        List<Book> books = bookService.getBooksByCategoryId(categoryId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping(value = "/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        List<Book> books = bookService.getAvailableBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    // existsBy... Endpoints
    @GetMapping(value = "/exists/title/{title}")
    public ResponseEntity<Boolean> checkBookExistsByTitle(@PathVariable String title) {
        Boolean exists = bookService.checkBookExistsByTitle(title);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Sorting and Pagination Endpoints
    @GetMapping(value = "/author/{author}/page")
    public ResponseEntity<Page<Book>> getBooksByAuthorWithPagination(
            @PathVariable String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Book> books = bookService.getBooksByAuthorWithPagination(author, pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping(value = "/all/page")
    public ResponseEntity<Page<Book>> getAllBooksWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Book> books = bookService.getAllBooksWithPagination(pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    // Search endpoints
    @GetMapping(value = "/search/advanced")
    public ResponseEntity<List<Book>> searchBooksAdvanced(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<Book> books = bookService.searchBooksByTitleOrAuthor(keyword != null ? keyword : "");
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    // Statistics endpoints
    @GetMapping(value = "/stats/count")
    public ResponseEntity<Long> getTotalBooksCount() {
        Long count = bookService.getTotalBooksCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    @GetMapping(value = "/stats/available-count")
    public ResponseEntity<Long> getAvailableBooksCount() {
        Long count = bookService.getAvailableBooksCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}