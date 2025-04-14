package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.interfaces.exception.OrderMismatchException;

import java.util.ArrayList;
import java.util.List;

public class OrderItems {

    private List<OrderItem> orderItems = new ArrayList<>();

    public OrderItems(List<OrderItem> values, Order expectedOrder) {
        if (values.stream().anyMatch(item -> !item.belongsTo(expectedOrder))) {
            throw new OrderMismatchException();
        }
        this.orderItems = List.copyOf(values);
    }

    public OrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public boolean empty(){
        return orderItems.isEmpty();
    }

    public Integer calculateTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::calculatePrice)
                .sum();
    }

    public void setOrder(Order order) {
        orderItems.stream().forEach(orderItem -> orderItem.setOrder(order));
    }

    public Integer size(){
        return orderItems.size();
    }

    public void deductStock() {
        orderItems.forEach(OrderItem::deductItemQuantity);
    }
}
