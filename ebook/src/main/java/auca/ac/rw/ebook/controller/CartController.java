// CartController.java
package auca.ac.rw.ebook.controller;

import auca.ac.rw.ebook.model.Cart;
import auca.ac.rw.ebook.service.CartService;
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
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    // CRUD Endpoints
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCartItem(@RequestBody Cart cart) {
        String response = cartService.saveCartItem(cart);
        if (response.contains("successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cart>> getAllCartItems() {
        List<Cart> cartItems = cartService.getAllCartItems();
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCartItemById(@PathVariable UUID id) {
        Optional<Cart> cartItem = cartService.getCartItemById(id);
        if (cartItem.isPresent()) {
            return new ResponseEntity<>(cartItem.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cart item not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCartItem(@RequestBody Cart cart) {
        String response = cartService.updateCartItem(cart);
        if (response.equals("Cart item updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable UUID id) {
        String response = cartService.deleteCartItem(id);
        if (response.equals("Cart item deleted successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // User-specific endpoints
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<Cart>> getCartItemsByUserId(@PathVariable UUID userId) {
        List<Cart> cartItems = cartService.getCartItemsByUserId(userId);
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/user/{userId}/clear")
    public ResponseEntity<?> clearUserCart(@PathVariable UUID userId) {
        String response = cartService.clearUserCart(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/user/{userId}/book/{bookId}")
    public ResponseEntity<?> removeCartItem(@PathVariable UUID userId, @PathVariable UUID bookId) {
        String response = cartService.removeCartItem(userId, bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PutMapping(value = "/user/{userId}/book/{bookId}/quantity/{newQuantity}")
    public ResponseEntity<?> updateCartItemQuantity(
            @PathVariable UUID userId, 
            @PathVariable UUID bookId,
            @PathVariable Integer newQuantity) {
        String response = cartService.updateCartItemQuantity(userId, bookId, newQuantity);
        if (response.equals("Cart item quantity updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // existsBy... Endpoints
    @GetMapping(value = "/exists/user/{userId}/book/{bookId}")
    public ResponseEntity<Boolean> checkCartItemExists(
            @PathVariable UUID userId, 
            @PathVariable UUID bookId) {
        Boolean exists = cartService.checkCartItemExists(userId, bookId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Sorting and Pagination Endpoints
    @GetMapping(value = "/user/{userId}/page")
    public ResponseEntity<Page<Cart>> getCartItemsByUserIdWithPagination(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "added_at") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Cart> cartItems = cartService.getCartItemsByUserIdWithPagination(userId, pageable);
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }
    
    // Statistics and calculations
    @GetMapping(value = "/user/{userId}/total-items")
    public ResponseEntity<Integer> getTotalItemsInCart(@PathVariable UUID userId) {
        Integer totalItems = cartService.getTotalItemsInCart(userId);
        return new ResponseEntity<>(totalItems, HttpStatus.OK);
    }
    
    @GetMapping(value = "/user/{userId}/total-price")
    public ResponseEntity<Double> calculateCartTotal(@PathVariable UUID userId) {
        Double cartTotal = cartService.calculateCartTotal(userId);
        return new ResponseEntity<>(cartTotal, HttpStatus.OK);
    }
    
    @GetMapping(value = "/user/{userId}/sorted")
    public ResponseEntity<List<Cart>> getUserCartSortedByDate(@PathVariable UUID userId) {
        List<Cart> cartItems = cartService.getUserCartSortedByDate(userId);
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }
}