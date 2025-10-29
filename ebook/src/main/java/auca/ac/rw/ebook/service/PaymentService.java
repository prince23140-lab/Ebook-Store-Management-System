// PaymentService.java
package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.Payment;
import auca.ac.rw.ebook.model.EPaymentStatus;
import auca.ac.rw.ebook.model.EPaymentMethod;
import auca.ac.rw.ebook.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    // CRUD Operations
    public String savePayment(Payment payment) {
        payment.setPayment_date(LocalDateTime.now());
        if (payment.getStatus() == null) {
            payment.setStatus(EPaymentStatus.PENDING);
        }
        paymentRepository.save(payment);
        return "Payment saved successfully";
    }
    
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    public Optional<Payment> getPaymentById(UUID id) {
        return paymentRepository.findById(id);
    }
    
    public String updatePayment(Payment payment) {
        if (paymentRepository.existsById(payment.getPayment_id())) {
            paymentRepository.save(payment);
            return "Payment updated successfully";
        } else {
            return "Payment not found";
        }
    }
    
    public String deletePayment(UUID id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return "Payment deleted successfully";
        } else {
            return "Payment not found";
        }
    }
    
    // findBy... methods
    public List<Payment> getPaymentsByUserId(UUID userId) {
        return paymentRepository.findByUserId(userId);
    }
    
    public List<Payment> getPaymentsByStatus(EPaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }
    
    public List<Payment> getPaymentsByMethod(EPaymentMethod method) {
        return paymentRepository.findByMethod(method);
    }
    
    public List<Payment> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }
    
    public List<Payment> getPaymentsByAmountGreaterThan(Double amount) {
        return paymentRepository.findByAmountGreaterThan(amount);
    }
    
    public Optional<Payment> getPaymentByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
    
    // existsBy... methods
    public Boolean checkPaymentExistsForOrder(UUID orderId) {
        return paymentRepository.existsByOrderId(orderId);
    }
    
    public Boolean checkUserHasPaymentsWithStatus(UUID userId, EPaymentStatus status) {
        return paymentRepository.existsByUserIdAndStatus(userId, status);
    }
    
    // Sorting and Pagination
    public Page<Payment> getPaymentsByUserIdWithPagination(UUID userId, Pageable pageable) {
        return paymentRepository.findByUserId(userId, pageable);
    }
    
    public Page<Payment> getPaymentsByStatusWithPagination(EPaymentStatus status, Pageable pageable) {
        return paymentRepository.findByStatus(status, pageable);
    }
    
    public Page<Payment> getPaymentsByDateRangeWithPagination(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate, pageable);
    }
    
    public Page<Payment> getAllPaymentsWithPagination(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }
    
    // Business logic methods
    public String processPayment(UUID paymentId) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(EPaymentStatus.SUCCESSFUL);
            payment.setPayment_date(LocalDateTime.now());
            paymentRepository.save(payment);
            return "Payment processed successfully";
        } else {
            return "Payment not found";
        }
    }
    
    public String failPayment(UUID paymentId) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(EPaymentStatus.FAILED);
            paymentRepository.save(payment);
            return "Payment marked as failed";
        } else {
            return "Payment not found";
        }
    }
    
    public Double calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        Double revenue = paymentRepository.calculateTotalRevenueBetweenDates(startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }
    
    public List<Payment> getUserPaymentsSortedByDate(UUID userId) {
        return paymentRepository.findUserPaymentsByDateDesc(userId);
    }
    
    public List<Payment> getSuccessfulPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findSuccessfulPaymentsBetweenDates(startDate, endDate);
    }
    
    // Statistics methods
    public Long getSuccessfulPaymentsCount() {
        return paymentRepository.countSuccessfulPayments();
    }
    
    public Double getAveragePaymentAmount() {
        Double average = paymentRepository.calculateAveragePaymentAmount();
        return average != null ? average : 0.0;
    }
    
    public List<Object[]> getPaymentsCountByMethod() {
        return paymentRepository.countPaymentsByMethod();
    }
    
    public List<Object[]> getPaymentsCountByStatus() {
        return paymentRepository.countPaymentsByStatus();
    }
    
    // Validation methods
    public Boolean validatePayment(Payment payment) {
        return payment.getOrder() != null && 
               payment.getUser() != null && 
               payment.getAmount() != null && 
               payment.getAmount().doubleValue() > 0 &&
               payment.getMethod() != null;
    }
}