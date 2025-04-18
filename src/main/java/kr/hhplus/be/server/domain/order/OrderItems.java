package kr.hhplus.be.server.domain.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import kr.hhplus.be.server.interfaces.exception.OrderMismatchException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable @Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class OrderItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public OrderItems(List<OrderItem> values, Order expectedOrder) {
        if (values.stream().anyMatch(item -> !item.belongsTo(expectedOrder))) {
            throw new OrderMismatchException();
        }
        this.orderItems = List.copyOf(values);
    }

    public OrderItems(Order order, List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        this.setOrder(order);
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
        orderItems.stream()
                .forEach(orderItem -> orderItem.setOrder(order));
    }

    public Integer size(){
        return orderItems.size();
    }

    public void deductStock() {
        orderItems.forEach(OrderItem::deductItemQuantity);
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "orderItems=" + orderItems +
                '}';
    }
}
