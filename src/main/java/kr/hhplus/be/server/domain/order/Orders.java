package kr.hhplus.be.server.domain.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
@Getter @NoArgsConstructor
public class Orders {

    @OneToMany(mappedBy = "user", cascade = CascadeType.DETACH)
    List<Order> orders = new ArrayList<>();

    public static Orders createDefault() {
        return new Orders();
    }

    public static Orders createWithOrder(Order order) {
        Orders orders = new Orders();
        orders.orders.add(order);
        return orders;
    }

    public static Orders createWithOrders(List<Order> orders) {
        return new Orders(orders);
    }

    protected Orders(List<Order> orders) {
        this.orders = new ArrayList<>(orders); // 복사만 하고, 불변처리 안 함
    }

    public void addOrder(Order order) {
        this.orders.add(order); // 기존 리스트에 추가
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(this.orders);
    }

    public boolean areAllOrdersPaymentCompleted(){
        return this.orders.stream()
            .allMatch(order -> order.getOrderStatus().equals(OrderStatus.PAYMENT_COMPLETED));
    }

}
