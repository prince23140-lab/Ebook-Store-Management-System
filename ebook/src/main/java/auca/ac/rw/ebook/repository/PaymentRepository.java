package auca.ac.rw.ebook.repository;

import auca.ac.rw.ebook.model.Payment;
import auca.ac.rw.ebook.model.EPaymentStatus;
import auca.ac.rw.ebook.model.EPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    
    // findBy... queries - FIXED: Using @Query for snake_case columns
    @Query("SELECT p FROM Payment p WHERE p.user.user_id = :userId")
    List<Payment> findByUserId(@Param("userId") UUID userId);
    
    List<Payment> findByStatus(EPaymentStatus status);
    List<Payment> findByMethod(EPaymentMethod method);
    
    @Query("SELECT p FROM Payment p WHERE p.payment_date BETWEEN :startDate AND :endDate")
    List<Payment> findByPaymentDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p FROM Payment p WHERE p.amount > :amount")
    List<Payment> findByAmountGreaterThan(@Param("amount") Double amount);
    
    @Query("SELECT p FROM Payment p WHERE p.order.order_id = :orderId")
    Optional<Payment> findByOrderId(@Param("orderId") UUID orderId);
    
    // existsBy... queries
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Payment p WHERE p.order.order_id = :orderId")
    Boolean existsByOrderId(@Param("orderId") UUID orderId);
    
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Payment p WHERE p.user.user_id = :userId AND p.status = :status")
    Boolean existsByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") EPaymentStatus status);
    
    // Sorting and Pagination
    @Query("SELECT p FROM Payment p WHERE p.user.user_id = :userId")
    Page<Payment> findByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    Page<Payment> findByStatus(EPaymentStatus status, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.payment_date BETWEEN :startDate AND :endDate")
    Page<Payment> findByPaymentDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    Page<Payment> findAll(Pageable pageable);
    
    // Custom queries
    @Query("SELECT p FROM Payment p WHERE p.user.user_id = :userId ORDER BY p.payment_date DESC")
    List<Payment> findUserPaymentsByDateDesc(@Param("userId") UUID userId);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'SUCCESSFUL' AND p.payment_date BETWEEN :startDate AND :endDate")
    List<Payment> findSuccessfulPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESSFUL' AND p.payment_date BETWEEN :startDate AND :endDate")
    Double calculateTotalRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p.method, COUNT(p) FROM Payment p GROUP BY p.method")
    List<Object[]> countPaymentsByMethod();
    
    @Query("SELECT p.status, COUNT(p) FROM Payment p GROUP BY p.status")
    List<Object[]> countPaymentsByStatus();
    
    // Statistics
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = 'SUCCESSFUL'")
    Long countSuccessfulPayments();
    
    @Query("SELECT AVG(p.amount) FROM Payment p WHERE p.status = 'SUCCESSFUL'")
    Double calculateAveragePaymentAmount();
}