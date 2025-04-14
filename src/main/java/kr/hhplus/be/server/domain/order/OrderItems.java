package kr.hhplus.be.server.domain.order;

import java.util.ArrayList;
import java.util.List;

public class OrderItems {

    private List<kr.hhplus.be.server.domain.order.OrderItem> orderItems = new ArrayList<>();

    public OrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
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
}
