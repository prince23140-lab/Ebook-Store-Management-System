package auca.ac.rw.ebook.repository;

import auca.ac.rw.ebook.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    
    // findBy... queries - FIXED: Using @Query for snake_case columns
    @Query("SELECT od FROM OrderDetail od WHERE od.order.order_id = :orderId")
    List<OrderDetail> findByOrderId(@Param("orderId") UUID orderId);
    
    @Query("SELECT od FROM OrderDetail od WHERE od.book.book_id = :bookId")
    List<OrderDetail> findByBookId(@Param("bookId") UUID bookId);
    
    @Query("SELECT od FROM OrderDetail od WHERE od.quantity > :quantity")
    List<OrderDetail> findByQuantityGreaterThan(@Param("quantity") Integer quantity);
    
    // existsBy... queries
    @Query("SELECT CASE WHEN COUNT(od) > 0 THEN true ELSE false END FROM OrderDetail od WHERE od.order.order_id = :orderId AND od.book.book_id = :bookId")
    Boolean existsByOrderIdAndBookId(@Param("orderId") UUID orderId, @Param("bookId") UUID bookId);
    
    // Custom queries
    @Query("SELECT od FROM OrderDetail od WHERE od.order.order_id = :orderId ORDER BY od.price DESC")
    List<OrderDetail> findOrderDetailsByOrderIdSortedByPrice(@Param("orderId") UUID orderId);
    
    @Query("SELECT SUM(od.quantity * od.price) FROM OrderDetail od WHERE od.order.order_id = :orderId")
    Double calculateOrderTotal(@Param("orderId") UUID orderId);
    
    @Query("SELECT od.book, SUM(od.quantity) as totalSold FROM OrderDetail od GROUP BY od.book ORDER BY totalSold DESC")
    List<Object[]> findBestSellingBooks();
    
    @Query("SELECT od FROM OrderDetail od WHERE od.order.user.user_id = :userId")
    List<OrderDetail> findOrderDetailsByUserId(@Param("userId") UUID userId);
    
    // Statistics
    @Query("SELECT SUM(od.quantity) FROM OrderDetail od WHERE od.book.book_id = :bookId")
    Integer getTotalQuantitySoldForBook(@Param("bookId") UUID bookId);
}