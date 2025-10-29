// OrderDetailService.java
package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.OrderDetail;
import auca.ac.rw.ebook.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderDetailService {
    
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    
    // CRUD Operations
    public String saveOrderDetail(OrderDetail orderDetail) {
        if (validateOrderDetail(orderDetail)) {
            orderDetailRepository.save(orderDetail);
            return "Order detail saved successfully";
        } else {
            return "Invalid order detail data";
        }
    }
    
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }
    
    public Optional<OrderDetail> getOrderDetailById(UUID id) {
        return orderDetailRepository.findById(id);
    }
    
    public String updateOrderDetail(OrderDetail orderDetail) {
        if (orderDetailRepository.existsById(orderDetail.getDetail_id())) {
            if (validateOrderDetail(orderDetail)) {
                orderDetailRepository.save(orderDetail);
                return "Order detail updated successfully";
            } else {
                return "Invalid order detail data";
            }
        } else {
            return "Order detail not found";
        }
    }
    
    public String deleteOrderDetail(UUID id) {
        if (orderDetailRepository.existsById(id)) {
            orderDetailRepository.deleteById(id);
            return "Order detail deleted successfully";
        } else {
            return "Order detail not found";
        }
    }
    
    // findBy... methods
    public List<OrderDetail> getOrderDetailsByOrderId(UUID orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
    
    public List<OrderDetail> getOrderDetailsByBookId(UUID bookId) {
        return orderDetailRepository.findByBookId(bookId);
    }
    
    public List<OrderDetail> getOrderDetailsWithMinimumQuantity(Integer minQuantity) {
        return orderDetailRepository.findByQuantityGreaterThan(minQuantity);
    }
    
    // existsBy... methods
    public Boolean checkOrderDetailExists(UUID orderId, UUID bookId) {
        return orderDetailRepository.existsByOrderIdAndBookId(orderId, bookId);
    }
    
    // Business logic methods
    public Double calculateOrderTotal(UUID orderId) {
        Double total = orderDetailRepository.calculateOrderTotal(orderId);
        return total != null ? total : 0.0;
    }
    
    public List<OrderDetail> getOrderDetailsSortedByPrice(UUID orderId) {
        return orderDetailRepository.findOrderDetailsByOrderIdSortedByPrice(orderId);
    }
    
    public List<Object[]> getBestSellingBooks() {
        return orderDetailRepository.findBestSellingBooks();
    }
    
    public List<OrderDetail> getOrderDetailsByUserId(UUID userId) {
        return orderDetailRepository.findOrderDetailsByUserId(userId);
    }
    
    public Integer getTotalQuantitySoldForBook(UUID bookId) {
        Integer totalSold = orderDetailRepository.getTotalQuantitySoldForBook(bookId);
        return totalSold != null ? totalSold : 0;
    }
    
    // Batch operations
    public String saveAllOrderDetails(List<OrderDetail> orderDetails) {
        try {
            // Validate all order details before saving
            for (OrderDetail orderDetail : orderDetails) {
                if (!validateOrderDetail(orderDetail)) {
                    return "Invalid order detail in the list";
                }
            }
            orderDetailRepository.saveAll(orderDetails);
            return "All order details saved successfully";
        } catch (Exception e) {
            return "Error saving order details: " + e.getMessage();
        }
    }
    
    public String deleteOrderDetailsByOrderId(UUID orderId) {
        try {
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
            orderDetailRepository.deleteAll(orderDetails);
            return "All order details for order " + orderId + " deleted successfully";
        } catch (Exception e) {
            return "Error deleting order details: " + e.getMessage();
        }
    }
    
    // Validation methods
    public Boolean validateOrderDetail(OrderDetail orderDetail) {
        return orderDetail.getQuantity() != null && 
               orderDetail.getQuantity() > 0 && 
               orderDetail.getPrice() != null && 
               orderDetail.getPrice().doubleValue() > 0 &&
               orderDetail.getOrder() != null && 
               orderDetail.getBook() != null;
    }
    
    // Statistics and analytics
    public List<Object[]> getSalesStatistics() {
        return getBestSellingBooks(); // Can be extended for more statistics
    }
    
    // Helper methods for order processing
    public Double calculateLineTotal(OrderDetail orderDetail) {
        if (orderDetail.getQuantity() != null && orderDetail.getPrice() != null) {
            return orderDetail.getQuantity() * orderDetail.getPrice().doubleValue();
        }
        return 0.0;
    }
}