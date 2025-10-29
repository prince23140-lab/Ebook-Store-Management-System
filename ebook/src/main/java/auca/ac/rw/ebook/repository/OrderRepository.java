package auca.ac.rw.ebook.repository;

import auca.ac.rw.ebook.model.Order;
import auca.ac.rw.ebook.model.EOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    // findBy... queries - FIXED: Using @Query for snake_case columns
    @Query("SELECT o FROM Order o WHERE o.user.user_id = :userId")
    List<Order> findByUserId(@Param("userId") UUID userId);
    
    List<Order> findByStatus(EOrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.order_date BETWEEN :startDate AND :endDate")
    List<Order> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.total_amount > :amount")
    List<Order> findByTotalAmountGreaterThan(@Param("amount") Double amount);
    
    @Query("SELECT o FROM Order o WHERE o.user.user_id = :userId AND o.status = :status")
    Optional<Order> findByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") EOrderStatus status);
    
    // existsBy... queries
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o WHERE o.user.user_id = :userId AND o.status = :status")
    Boolean existsByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") EOrderStatus status);
    
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o WHERE o.user.user_id = :userId")
    Boolean existsByUserId(@Param("userId") UUID userId);
    
    // Sorting and Pagination
    @Query("SELECT o FROM Order o WHERE o.user.user_id = :userId")
    Page<Order> findByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    Page<Order> findByStatus(EOrderStatus status, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.order_date BETWEEN :startDate AND :endDate")
    Page<Order> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    Page<Order> findAll(Pageable pageable);
    
    // Custom queries
    @Query("SELECT o FROM Order o WHERE o.user.user_id = :userId ORDER BY o.order_date DESC")
    List<Order> findUserOrdersByDateDesc(@Param("userId") UUID userId);
    
    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.order_date < :date")
    List<Order> findPendingOrdersOlderThan(@Param("date") LocalDateTime date);
    
    @Query("SELECT SUM(o.total_amount) FROM Order o WHERE o.status = 'COMPLETED' AND o.order_date BETWEEN :startDate AND :endDate")
    Double calculateRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countOrdersByStatus(@Param("status") EOrderStatus status);
    
    @Query("SELECT o FROM Order o JOIN o.user u JOIN u.location l WHERE l.province = :province")
    List<Order> findOrdersByProvince(@Param("province") String province);
    
    // Statistics
    @Query("SELECT COUNT(o) FROM Order o")
    Long countTotalOrders();
    
    @Query("SELECT AVG(o.total_amount) FROM Order o WHERE o.status = 'COMPLETED'")
    Double calculateAverageOrderValue();
}