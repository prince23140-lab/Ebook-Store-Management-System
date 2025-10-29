// BookService.java
package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.Book;
import auca.ac.rw.ebook.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    // CRUD Operations
    public String saveBook(Book book) {
        if (!bookRepository.existsByTitleAndAuthor(book.getTitle(), book.getAuthor())) {
            bookRepository.save(book);
            return "Book saved successfully";
        } else {
            return "Book with this title and author already exists";
        }
    }
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> getBookById(UUID id) {
        return bookRepository.findById(id);
    }
    
    public String updateBook(Book book) {
        if (bookRepository.existsById(book.getBook_id())) {
            bookRepository.save(book);
            return "Book updated successfully";
        } else {
            return "Book not found";
        }
    }
    
    public String deleteBook(UUID id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return "Book deleted successfully";
        } else {
            return "Book not found";
        }
    }
    
    // findBy... methods
    public Optional<Book> getBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
    
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
    
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title);
    }
    
    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContaining(author);
    }
    
    public List<Book> getBooksByPriceRange(Double minPrice, Double maxPrice) {
        return bookRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }
    
    public List<Book> getBooksByCategoryId(UUID categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }
    
    public List<Book> getBooksByCategoryName(String categoryName) {
        return bookRepository.findByCategoryName(categoryName);
    }
    
    // existsBy... methods
    public Boolean checkBookExistsByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }
    
    public Boolean checkBookExistsByTitleAndAuthor(String title, String author) {
        return bookRepository.existsByTitleAndAuthor(title, author);
    }
    
    public Boolean checkBookExistsByAuthor(String author) {
        return bookRepository.existsByAuthor(author);
    }
    
    // Sorting and Pagination
    public Page<Book> getBooksByAuthorWithPagination(String author, Pageable pageable) {
        return bookRepository.findByAuthor(author, pageable);
    }
    
    public Page<Book> searchBooksByTitleWithPagination(String title, Pageable pageable) {
        return bookRepository.findByTitleContaining(title, pageable);
    }
    
    public Page<Book> getBooksByPriceRangeWithPagination(Double minPrice, Double maxPrice, Pageable pageable) {
        return bookRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }
    
    public Page<Book> getAllBooksWithPagination(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
    
    // Custom search queries
    public List<Book> searchBooksByTitleOrAuthor(String keyword) {
        return bookRepository.searchByTitleOrAuthor(keyword);
    }
    
    public List<Book> getBooksWithStock() {
        return bookRepository.findByStockQuantityGreaterThan(0);
    }
    
    // Business logic methods
    public String updateBookStock(UUID bookId, Integer newStock) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setStock_quantity(newStock);
            bookRepository.save(book);
            return "Book stock updated successfully";
        } else {
            return "Book not found";
        }
    }
    
    public String decreaseBookStock(UUID bookId, Integer quantity) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (book.getStock_quantity() >= quantity) {
                book.setStock_quantity(book.getStock_quantity() - quantity);
                bookRepository.save(book);
                return "Book stock decreased successfully";
            } else {
                return "Insufficient stock";
            }
        } else {
            return "Book not found";
        }
    }
    
    // Statistics methods
    public Long getTotalBooksCount() {
        return bookRepository.countTotalBooks();
    }
    
    public Long getAvailableBooksCount() {
        return bookRepository.countAvailableBooks();
    }
    
    // Validation methods
    public Boolean validateBookData(Book book) {
        return book.getTitle() != null && !book.getTitle().trim().isEmpty() &&
               book.getAuthor() != null && !book.getAuthor().trim().isEmpty() &&
               book.getPrice() != null && book.getPrice().doubleValue() > 0 &&
               book.getCategory() != null;
    }
}