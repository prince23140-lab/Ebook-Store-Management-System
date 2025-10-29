// PaymentController.java
package auca.ac.rw.ebook.controller;

import auca.ac.rw.ebook.model.Payment;
import auca.ac.rw.ebook.model.EPaymentStatus;
import auca.ac.rw.ebook.service.PaymentService;
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
@RequestMapping("/api/payment")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    // CRUD Endpoints
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePayment(@RequestBody Payment payment) {
        String response = paymentService.savePayment(payment);
        if (response.equals("Payment saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPaymentById(@PathVariable UUID id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        if (payment.isPresent()) {
            return new ResponseEntity<>(payment.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Payment not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePayment(@RequestBody Payment payment) {
        String response = paymentService.updatePayment(payment);
        if (response.equals("Payment updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable UUID id) {
        String response = paymentService.deletePayment(id);
        if (response.equals("Payment deleted successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // findBy... Endpoints
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUserId(@PathVariable UUID userId) {
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    
    @GetMapping(value = "/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable EPaymentStatus status) {
        List<Payment> payments = paymentService.getPaymentsByStatus(status);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    
    @GetMapping(value = "/order/{orderId}")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable UUID orderId) {
        Optional<Payment> payment = paymentService.getPaymentByOrderId(orderId);
        if (payment.isPresent()) {
            return new ResponseEntity<>(payment.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Payment not found for this order", HttpStatus.NOT_FOUND);
        }
    }
    
    // existsBy... Endpoints
    @GetMapping(value = "/exists/order/{orderId}")
    public ResponseEntity<Boolean> checkPaymentExistsForOrder(@PathVariable UUID orderId) {
        Boolean exists = paymentService.checkPaymentExistsForOrder(orderId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Sorting and Pagination Endpoints
    @GetMapping(value = "/user/{userId}/page")
    public ResponseEntity<Page<Payment>> getPaymentsByUserIdWithPagination(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "payment_date") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Payment> payments = paymentService.getPaymentsByUserIdWithPagination(userId, pageable);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    
    // Business logic Endpoints
    @PutMapping(value = "/{paymentId}/process")
    public ResponseEntity<?> processPayment(@PathVariable UUID paymentId) {
        String response = paymentService.processPayment(paymentId);
        if (response.equals("Payment processed successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping(value = "/revenue")
    public ResponseEntity<Double> calculateTotalRevenue(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        Double revenue = paymentService.calculateTotalRevenue(start, end);
        return new ResponseEntity<>(revenue, HttpStatus.OK);
    }
    
    // Statistics endpoints
    @GetMapping(value = "/stats/successful-count")
    public ResponseEntity<Long> getSuccessfulPaymentsCount() {
        Long count = paymentService.getSuccessfulPaymentsCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    @GetMapping(value = "/stats/average-amount")
    public ResponseEntity<Double> getAveragePaymentAmount() {
        Double average = paymentService.getAveragePaymentAmount();
        return new ResponseEntity<>(average, HttpStatus.OK);
    }
    
    @GetMapping(value = "/stats/by-method")
    public ResponseEntity<List<Object[]>> getPaymentsCountByMethod() {
        List<Object[]> methodStats = paymentService.getPaymentsCountByMethod();
        return new ResponseEntity<>(methodStats, HttpStatus.OK);
    }
    
    @GetMapping(value = "/stats/by-status")
    public ResponseEntity<List<Object[]>> getPaymentsCountByStatus() {
        List<Object[]> statusStats = paymentService.getPaymentsCountByStatus();
        return new ResponseEntity<>(statusStats, HttpStatus.OK);
    }
}