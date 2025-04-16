package kr.hhplus.be.server.integration.order.infrastructure;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.infrastructure.order.OrderJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    User user1;
    User user2;

    Item car;
    Item truck;
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @BeforeEach
    void setUp() {
        user1 = new User("user1");
        user2 = new User("user2");

        car = new Item("car", 2000, 50);
        truck = new Item("truck", 1000, 50);

        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(car);
        itemRepository.save(truck);
    }

    @Test
    @Transactional
    void 주문과_주문상품을_저장한다(){
        //given
        Order order = new Order(user1);
        OrderItem orderItem1 = new OrderItem(order, car, 2000, 30);
        OrderItem orderItem2 = new OrderItem(order, truck, 1000, 10);
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);
        order.addOrderItems(orderItems);
        Order save = orderRepository.save(order);

        //when
        Order savedOrder = orderRepository.findById(save.getId()).get();
        List<OrderItem> savedOrderItems = orderRepository.findOrderItemsByOrderId(savedOrder.getId());



        //then
        assertEquals(order, save);
        //assertEquals(savedOrder.getOrderItems(), savedOrderItems); 오 이거 꽤 자주 나오는 테스트 트랩 중 하나예요. 👇 이 오류는 실제로 값이 달라서가 아니라, 컬렉션 타입이 달라서 발생하는 거예요.
        assertThat(save.getOrderItems())
                .containsExactlyInAnyOrderElementsOf(savedOrderItems);
    }

}
