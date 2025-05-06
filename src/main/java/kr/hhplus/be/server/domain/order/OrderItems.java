package kr.hhplus.be.server.domain.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import kr.hhplus.be.server.domain.product.Price;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderItem> items = new ArrayList<>();

    protected OrderItems() {}

    public OrderItems(List<OrderItem> items) {
        for (OrderItem item : items) {
            add(item);
        }
    }

    public OrderItems(List<OrderItem> items, Order order) {
        for (OrderItem item : items) {
            add(item);
        }
        assignOrder(order);
    }

    public void add(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("OrderItem must not be null");
        }
        items.add(item);
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Price calculateTotalAmount() {
        return items.stream()
                .map(OrderItem::calculateAmount)
                .reduce(Price.ZERO, Price::add);
    }

    public void assignOrder(Order order) {
        for (OrderItem item : items) {
            item.assignToOrder(order);
        }
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
