// OrderController.java
package auca.ac.rw.ebook.controller;

import auca.ac.rw.ebook.model.Order;
import auca.ac.rw.ebook.model.EOrderStatus;
import auca.ac.rw.ebook.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    // CRUD Endpoints
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOrder(@RequestBody Order order) {
        // Validate the order before saving
        if (!orderService.validateOrder(order)) {
            return new ResponseEntity<>("Invalid order data", HttpStatus.BAD_REQUEST);
        }
        
        String response = orderService.saveOrder(order);
        if (response.equals("Order saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOrderById(@PathVariable UUID id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateOrder(@RequestBody Order order) {
        // Validate the order before updating
        if (!orderService.validateOrder(order)) {
            return new ResponseEntity<>("Invalid order data", HttpStatus.BAD_REQUEST);
        }
        
        String response = orderService.updateOrder(order);
        if (response.equals("Order updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable UUID id) {
        String response = orderService.deleteOrder(id);
        if (response.equals("Order deleted successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // findBy... Endpoints
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable UUID userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    @GetMapping(value = "/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable EOrderStatus status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    @GetMapping(value = "/date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            List<Order> orders = orderService.getOrdersByDateRange(start, end);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = "/amount-greater-than/{amount}")
    public ResponseEntity<List<Order>> getOrdersByAmountGreaterThan(@PathVariable Double amount) {
        List<Order> orders = orderService.getOrdersByTotalAmountGreaterThan(amount);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    // existsBy... Endpoints
    @GetMapping(value = "/user/{userId}/has-pending")
    public ResponseEntity<Boolean> checkUserHasPendingOrder(@PathVariable UUID userId) {
        Boolean hasPending = orderService.checkUserHasPendingOrder(userId);
        return new ResponseEntity<>(hasPending, HttpStatus.OK);
    }
    
    @GetMapping(value = "/user/{userId}/has-orders")
    public ResponseEntity<Boolean> checkUserHasOrders(@PathVariable UUID userId) {
        Boolean hasOrders = orderService.checkUserHasOrders(userId);
        return new ResponseEntity<>(hasOrders, HttpStatus.OK);
    }
    
    // Sorting and Pagination Endpoints
    @GetMapping(value = "/user/{userId}/page")
    public ResponseEntity<Page<Order>> getOrdersByUserIdWithPagination(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "order_date") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Order> orders = orderService.getOrdersByUserIdWithPagination(userId, pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    @GetMapping(value = "/status/{status}/page")
    public ResponseEntity<Page<Order>> getOrdersByStatusWithPagination(
            @PathVariable EOrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "order_date") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Order> orders = orderService.getOrdersByStatusWithPagination(status, pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    @GetMapping(value = "/date-range/page")
    public ResponseEntity<Page<Order>> getOrdersByDateRangeWithPagination(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "order_date") String sort) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
            Page<Order> orders = orderService.getOrdersByDateRangeWithPagination(start, end, pageable);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = "/all/page")
    public ResponseEntity<Page<Order>> getAllOrdersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "order_date") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Order> orders = orderService.getAllOrdersWithPagination(pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    // Business logic Endpoints
    @PutMapping(value = "/{orderId}/status/{newStatus}")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable UUID orderId,
            @PathVariable EOrderStatus newStatus) {
        String response = orderService.updateOrderStatus(orderId, newStatus);
        if (response.equals("Order status updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable UUID orderId) {
        String response = orderService.cancelOrder(orderId);
        if (response.equals("Order status updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/{orderId}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable UUID orderId) {
        String response = orderService.completeOrder(orderId);
        if (response.equals("Order status updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping(value = "/revenue")
    public ResponseEntity<Double> calculateRevenueBetweenDates(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            Double revenue = orderService.calculateRevenueBetweenDates(start, end);
            return new ResponseEntity<>(revenue, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    // Midterm Requirement: Orders by province - FIXED parameter name
    @GetMapping(value = "/province/{provinceName}")
    public ResponseEntity<List<Order>> getOrdersByProvince(@PathVariable String provinceName) {
        List<Order> orders = orderService.getOrdersByProvince(provinceName);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    @GetMapping(value = "/user/{userId}/sorted")
    public ResponseEntity<List<Order>> getUserOrdersSortedByDate(@PathVariable UUID userId) {
        List<Order> orders = orderService.getUserOrdersSortedByDate(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    @GetMapping(value = "/pending/older-than")
    public ResponseEntity<List<Order>> getPendingOrdersOlderThan(@RequestParam String date) {
        try {
            LocalDateTime olderThanDate = LocalDateTime.parse(date);
            List<Order> orders = orderService.getPendingOrdersOlderThan(olderThanDate);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    // Statistics endpoints
    @GetMapping(value = "/stats/count")
    public ResponseEntity<Long> getTotalOrdersCount() {
        Long count = orderService.getTotalOrdersCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    @GetMapping(value = "/stats/count-by-status/{status}")
    public ResponseEntity<Long> getOrdersCountByStatus(@PathVariable EOrderStatus status) {
        Long count = orderService.getOrdersCountByStatus(status);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    @GetMapping(value = "/stats/average-value")
    public ResponseEntity<Double> getAverageOrderValue() {
        Double average = orderService.getAverageOrderValue();
        return new ResponseEntity<>(average, HttpStatus.OK);
    }
    
    // Validation Endpoint
    @PostMapping(value = "/validate", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> validateOrder(@RequestBody Order order) {
        Boolean isValid = orderService.validateOrder(order);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }
}