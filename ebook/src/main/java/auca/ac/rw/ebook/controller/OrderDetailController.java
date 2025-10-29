// OrderDetailController.java
package auca.ac.rw.ebook.controller;

import auca.ac.rw.ebook.model.OrderDetail;
import auca.ac.rw.ebook.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-detail")
public class OrderDetailController {
    
    @Autowired
    private OrderDetailService orderDetailService;
    
    // CRUD Endpoints
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOrderDetail(@RequestBody OrderDetail orderDetail) {
        // Validate the order detail
        if (!orderDetailService.validateOrderDetail(orderDetail)) {
            return new ResponseEntity<>("Invalid order detail data", HttpStatus.BAD_REQUEST);
        }
        
        String response = orderDetailService.saveOrderDetail(orderDetail);
        if (response.equals("Order detail saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping(value = "/save-all", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveAllOrderDetails(@RequestBody List<OrderDetail> orderDetails) {
        // Validate all order details
        for (OrderDetail orderDetail : orderDetails) {
            if (!orderDetailService.validateOrderDetail(orderDetail)) {
                return new ResponseEntity<>("Invalid order detail in the list", HttpStatus.BAD_REQUEST);
            }
        }
        
        String response = orderDetailService.saveAllOrderDetails(orderDetails);
        if (response.equals("All order details saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDetail>> getAllOrderDetails() {
        List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetails();
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOrderDetailById(@PathVariable UUID id) {
        Optional<OrderDetail> orderDetail = orderDetailService.getOrderDetailById(id);
        if (orderDetail.isPresent()) {
            return new ResponseEntity<>(orderDetail.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Order detail not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateOrderDetail(@RequestBody OrderDetail orderDetail) {
        // Validate the order detail
        if (!orderDetailService.validateOrderDetail(orderDetail)) {
            return new ResponseEntity<>("Invalid order detail data", HttpStatus.BAD_REQUEST);
        }
        
        String response = orderDetailService.updateOrderDetail(orderDetail);
        if (response.equals("Order detail updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable UUID id) {
        String response = orderDetailService.deleteOrderDetail(id);
        if (response.equals("Order detail deleted successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping(value = "/delete/by-order/{orderId}")
    public ResponseEntity<?> deleteOrderDetailsByOrderId(@PathVariable UUID orderId) {
        String response = orderDetailService.deleteOrderDetailsByOrderId(orderId);
        if (response.startsWith("All order details for order")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    // findBy... Endpoints
    @GetMapping(value = "/order/{orderId}")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByOrderId(@PathVariable UUID orderId) {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
    
    @GetMapping(value = "/book/{bookId}")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByBookId(@PathVariable UUID bookId) {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByBookId(bookId);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
    
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByUserId(@PathVariable UUID userId) {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByUserId(userId);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
    
    @GetMapping(value = "/quantity-greater-than/{minQuantity}")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsWithMinimumQuantity(@PathVariable Integer minQuantity) {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsWithMinimumQuantity(minQuantity);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
    
    // existsBy... Endpoints
    @GetMapping(value = "/exists")
    public ResponseEntity<Boolean> checkOrderDetailExists(
            @RequestParam UUID orderId, 
            @RequestParam UUID bookId) {
        Boolean exists = orderDetailService.checkOrderDetailExists(orderId, bookId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Business logic Endpoints
    @GetMapping(value = "/order/{orderId}/total")
    public ResponseEntity<Double> calculateOrderTotal(@PathVariable UUID orderId) {
        Double total = orderDetailService.calculateOrderTotal(orderId);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }
    
    @GetMapping(value = "/order/{orderId}/sorted-by-price")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsSortedByPrice(@PathVariable UUID orderId) {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsSortedByPrice(orderId);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
    
    @GetMapping(value = "/best-selling")
    public ResponseEntity<List<Object[]>> getBestSellingBooks() {
        List<Object[]> bestSellers = orderDetailService.getBestSellingBooks();
        return new ResponseEntity<>(bestSellers, HttpStatus.OK);
    }
    
    @GetMapping(value = "/book/{bookId}/total-sold")
    public ResponseEntity<Integer> getTotalQuantitySoldForBook(@PathVariable UUID bookId) {
        Integer totalSold = orderDetailService.getTotalQuantitySoldForBook(bookId);
        return new ResponseEntity<>(totalSold, HttpStatus.OK);
    }
    
    // Validation Endpoint
    @PostMapping(value = "/validate", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> validateOrderDetail(@RequestBody OrderDetail orderDetail) {
        Boolean isValid = orderDetailService.validateOrderDetail(orderDetail);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }
}