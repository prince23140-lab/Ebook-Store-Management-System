// CartService.java
package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.Cart;
import auca.ac.rw.ebook.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    // CRUD Operations
    public String saveCartItem(Cart cart) {
        // Check if item already exists in cart
        Optional<Cart> existingCartItem = cartRepository.findByUserIdAndBookId(
            cart.getUser().getUser_id(), cart.getBook().getBook_id());
        
        if (existingCartItem.isPresent()) {
            // Update quantity if item exists
            Cart existingItem = existingCartItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + cart.getQuantity());
            existingItem.setAdded_at(LocalDateTime.now());
            cartRepository.save(existingItem);
            return "Cart item quantity updated successfully";
        } else {
            // Add new item to cart
            cart.setAdded_at(LocalDateTime.now());
            cartRepository.save(cart);
            return "Cart item saved successfully";
        }
    }
    
    public List<Cart> getAllCartItems() {
        return cartRepository.findAll();
    }
    
    public Optional<Cart> getCartItemById(UUID id) {
        return cartRepository.findById(id);
    }
    
    public String updateCartItem(Cart cart) {
        if (cartRepository.existsById(cart.getCart_id())) {
            cart.setAdded_at(LocalDateTime.now());
            cartRepository.save(cart);
            return "Cart item updated successfully";
        } else {
            return "Cart item not found";
        }
    }
    
    public String deleteCartItem(UUID id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return "Cart item deleted successfully";
        } else {
            return "Cart item not found";
        }
    }
    
    // Custom business logic methods
    public List<Cart> getCartItemsByUserId(UUID userId) {
        return cartRepository.findByUserId(userId);
    }
    
    public String clearUserCart(UUID userId) {
        List<Cart> userCartItems = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(userCartItems);
        return "User cart cleared successfully";
    }
    
    public String removeCartItem(UUID userId, UUID bookId) {
        cartRepository.deleteByUserIdAndBookId(userId, bookId);
        return "Cart item removed successfully";
    }
    
    public String updateCartItemQuantity(UUID userId, UUID bookId, Integer newQuantity) {
        Optional<Cart> cartItem = cartRepository.findByUserIdAndBookId(userId, bookId);
        if (cartItem.isPresent()) {
            Cart item = cartItem.get();
            item.setQuantity(newQuantity);
            item.setAdded_at(LocalDateTime.now());
            cartRepository.save(item);
            return "Cart item quantity updated successfully";
        } else {
            return "Cart item not found";
        }
    }
    
    // existsBy... methods
    public Boolean checkCartItemExists(UUID userId, UUID bookId) {
        return cartRepository.existsByUserIdAndBookId(userId, bookId);
    }
    
    public Boolean checkUserHasCart(UUID userId) {
        return cartRepository.existsByUserId(userId);
    }
    
    // Sorting and Pagination
    public Page<Cart> getCartItemsByUserIdWithPagination(UUID userId, Pageable pageable) {
        return cartRepository.findByUserId(userId, pageable);
    }
    
    public Page<Cart> getAllCartItemsWithPagination(Pageable pageable) {
        return cartRepository.findAll(pageable);
    }
    
    // Statistics and calculations
    public Integer getTotalItemsInCart(UUID userId) {
        Integer totalItems = cartRepository.countTotalItemsInCart(userId);
        return totalItems != null ? totalItems : 0;
    }
    
    public Double calculateCartTotal(UUID userId) {
        Double cartTotal = cartRepository.calculateCartTotal(userId);
        return cartTotal != null ? cartTotal : 0.0;
    }
    
    public List<Cart> getUserCartItemsWithMinimumQuantity(UUID userId, Integer minQuantity) {
        return cartRepository.findUserCartItemsWithMinimumQuantity(userId, minQuantity);
    }
    
    public List<Cart> getUserCartSortedByDate(UUID userId) {
        return cartRepository.findUserCartByDateDesc(userId);
    }
    
    // Business logic methods
    public String addToCart(UUID userId, UUID bookId, Integer quantity) {
        // This would require BookService dependency to check stock
        // For now, just return a placeholder response
        return "Add to cart functionality - implement with BookService dependency";
    }
    
    // Validation methods
    public Boolean validateCartItem(Cart cart) {
        return cart.getUser() != null && 
               cart.getBook() != null && 
               cart.getQuantity() != null && 
               cart.getQuantity() > 0;
    }
}