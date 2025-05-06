package kr.hhplus.be.server.domain.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Orders {

    private List<Order> orders = new ArrayList<>();

    public Orders(List<Order> orders) {
        this.orders = Objects.requireNonNullElse(orders, List.of());
    }

    public List<Order> values() {
        return Collections.unmodifiableList(orders);
    }

    public int size() {
        return orders.size();
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }

    public static void validateNoPendingOrders(List<Order> orders) {
        boolean hasPending = orders.stream()
                .anyMatch(o -> o.getStatus().isPendingPayment());

        if (hasPending) {
            throw new IllegalStateException("미결제 주문이 존재합니다.");
        }
    }

    public long countPaidOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PAID)
                .count();
    }

    public List<Order> filterByStatus(OrderStatus status) {
        return orders.stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void validateNoPendingOrders() {
        if (orders.stream().anyMatch(o -> o.getStatus().isPendingPayment())) {
            throw new IllegalStateException("미결제 주문이 존재합니다.");
        }
    }

}
