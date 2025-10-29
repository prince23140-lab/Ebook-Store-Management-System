// OrderService.java
package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.Order;
import auca.ac.rw.ebook.model.EOrderStatus;
import auca.ac.rw.ebook.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    // CRUD Operations
    public String saveOrder(Order order) {
        if (!validateOrder(order)) {
            return "Invalid order data";
        }
        
        order.setOrder_date(LocalDateTime.now());
        if (order.getStatus() == null) {
            order.setStatus(EOrderStatus.PENDING);
        }
        orderRepository.save(order);
        return "Order saved successfully";
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }
    
    public String updateOrder(Order order) {
        if (!validateOrder(order)) {
            return "Invalid order data";
        }
        
        if (orderRepository.existsById(order.getOrder_id())) {
            orderRepository.save(order);
            return "Order updated successfully";
        } else {
            return "Order not found";
        }
    }
    
    public String deleteOrder(UUID id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return "Order deleted successfully";
        } else {
            return "Order not found";
        }
    }
    
    // findBy... methods
    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepository.findByUserId(userId);
    }
    
    public List<Order> getOrdersByStatus(EOrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    public List<Order> getOrdersByTotalAmountGreaterThan(Double amount) {
        return orderRepository.findByTotalAmountGreaterThan(amount);
    }
    
    public Optional<Order> getOrderByUserIdAndStatus(UUID userId, EOrderStatus status) {
        return orderRepository.findByUserIdAndStatus(userId, status);
    }
    
    // existsBy... methods
    public Boolean checkUserHasPendingOrder(UUID userId) {
        return orderRepository.existsByUserIdAndStatus(userId, EOrderStatus.PENDING);
    }
    
    public Boolean checkUserHasOrders(UUID userId) {
        return orderRepository.existsByUserId(userId);
    }
    
    // Sorting and Pagination
    public Page<Order> getOrdersByUserIdWithPagination(UUID userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }
    
    public Page<Order> getOrdersByStatusWithPagination(EOrderStatus status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }
    
    public Page<Order> getOrdersByDateRangeWithPagination(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return orderRepository.findByOrderDateBetween(startDate, endDate, pageable);
    }
    
    public Page<Order> getAllOrdersWithPagination(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
    
    // Business logic methods
    public String updateOrderStatus(UUID orderId, EOrderStatus newStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(newStatus);
            
            // Update order date if completing the order
            if (newStatus == EOrderStatus.COMPLETED || newStatus == EOrderStatus.DELIVERED) {
                order.setOrder_date(LocalDateTime.now());
            }
            
            orderRepository.save(order);
            return "Order status updated successfully";
        } else {
            return "Order not found";
        }
    }
    
    public Double calculateRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        Double revenue = orderRepository.calculateRevenueBetweenDates(startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }
    
    // FIXED: Changed parameter name to match repository method
    public List<Order> getOrdersByProvince(String provinceName) {
        return orderRepository.findOrdersByProvince(provinceName);
    }
    
    public List<Order> getUserOrdersSortedByDate(UUID userId) {
        return orderRepository.findUserOrdersByDateDesc(userId);
    }
    
    public List<Order> getPendingOrdersOlderThan(LocalDateTime date) {
        return orderRepository.findPendingOrdersOlderThan(date);
    }
    
    // Statistics methods
    public Long getTotalOrdersCount() {
        return orderRepository.countTotalOrders();
    }
    
    public Long getOrdersCountByStatus(EOrderStatus status) {
        return orderRepository.countOrdersByStatus(status);
    }
    
    public Double getAverageOrderValue() {
        Double average = orderRepository.calculateAverageOrderValue();
        return average != null ? average : 0.0;
    }
    
    // Validation methods
    public Boolean validateOrder(Order order) {
        return order.getUser() != null && 
               order.getTotal_amount() != null && 
               order.getTotal_amount().doubleValue() >= 0;
    }
    
    // Business workflow methods
    public String cancelOrder(UUID orderId) {
        return updateOrderStatus(orderId, EOrderStatus.CANCELLED);
    }
    
    public String completeOrder(UUID orderId) {
        return updateOrderStatus(orderId, EOrderStatus.COMPLETED);
    }
    
    public String shipOrder(UUID orderId) {
        return updateOrderStatus(orderId, EOrderStatus.SHIPPED);
    }
    
    public String deliverOrder(UUID orderId) {
        return updateOrderStatus(orderId, EOrderStatus.DELIVERED);
    }
    
    // Additional business methods
    public Double calculateUserTotalSpent(UUID userId) {
        List<Order> userOrders = orderRepository.findByUserId(userId);
        return userOrders.stream()
                .filter(order -> order.getStatus() == EOrderStatus.COMPLETED || order.getStatus() == EOrderStatus.DELIVERED)
                .mapToDouble(order -> order.getTotal_amount().doubleValue())
                .sum();
    }
    
    public Integer getUserOrderCount(UUID userId) {
        List<Order> userOrders = orderRepository.findByUserId(userId);
        return userOrders.size();
    }
    
    // Bulk operations
    public String updateMultipleOrderStatuses(List<UUID> orderIds, EOrderStatus newStatus) {
        try {
            for (UUID orderId : orderIds) {
                updateOrderStatus(orderId, newStatus);
            }
            return "Multiple order statuses updated successfully";
        } catch (Exception e) {
            return "Error updating multiple order statuses: " + e.getMessage();
        }
    }
}