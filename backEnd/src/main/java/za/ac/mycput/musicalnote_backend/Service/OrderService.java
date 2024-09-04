package za.ac.mycput.musicalnote_backend.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.musicalnote_backend.Domain.Order;
import za.ac.mycput.musicalnote_backend.Repository.OrderRepository;

import java.util.List;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }
    public Order createOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        if (order.getUserId() == null) {
            throw new IllegalArgumentException("Order must have an associated user");
        }

        if (order.getOrderDate() == null) {
            throw new IllegalArgumentException("Order date must be set");
        }

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Order addOrder(Order order) {

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(Order order) {
        Order existingOrder = orderRepository.findById(order.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + order.getOrderId()));

        existingOrder.setUserId(order.getUserId());
        existingOrder.setOrderDate(order.getOrderDate());
        existingOrder.setStatus(order.getStatus());

        return orderRepository.save(existingOrder);
    }

    @Transactional
    public Boolean deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
        return true;
    }
}
