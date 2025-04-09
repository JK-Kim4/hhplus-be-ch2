package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.order.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.order.command.OrderItemCreateCommand;
import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import kr.hhplus.be.server.domain.order.orderItem.OrderItemRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.exception.InvalidPriceException;
import kr.hhplus.be.server.interfaces.exception.NotEnoughStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderItemRepository orderItemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    OrderService orderService;

    List<OrderItemCreateCommand> commands = new ArrayList<>();
    OrderItemCreateCommand command1;
    OrderItemCreateCommand command2;
    OrderItemCreateCommand command3;
    User user;
    Order order;

    @BeforeEach
    void setUp() {
        //given
        user = new User(1L, "test");
        order = new Order(user);
        command1 = new OrderItemCreateCommand(1L, 100, 1);
        command2 = new OrderItemCreateCommand(2L, 100, 2);
        command3 = new OrderItemCreateCommand(3L, 100, 3);
        commands.add(command1);
        commands.add(command2);
        commands.add(command3);

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(new Item(1L, "test", 100, 5)));
        when(itemRepository.findById(2L))
                .thenReturn(Optional.of(new Item(2L, "test", 100, 5)));
        when(itemRepository.findById(3L))
                .thenReturn(Optional.of(new Item(3L, "test", 100, 5)));
    }

    @Test
    @DisplayName("주문 상품 목록 정보를 생성/저장한다.")
    void create_order_item_list_test(){
        //when
        List<OrderItem> orderItems = orderService.saveOrderItems(order, commands);

        //then
        assertAll
        (
            "주문 상품 정보를 생성하고 등록한다",
            () -> verify(itemRepository, times(orderItems.size())).findById(any()),
            () -> verify(orderItemRepository, times(1)).saveList(orderItems)
        );
    }

    @Test
    @DisplayName("주문 상품의 가격 정보가 일치하지 않으면 오류를 반환한다.")
    void create_order_item_test_price_mismatch(){
        //given
        commands.add(new OrderItemCreateCommand(4L, 300, 3));
        when(itemRepository.findById(4L))
                .thenReturn(Optional.of(new Item(4L, "test", 150, 5)));

        //when
        InvalidPriceException invalidPriceException = assertThrows(InvalidPriceException.class,
                () -> orderService.saveOrderItems(order, commands));

        //then
        assertEquals(String.format(InvalidPriceException.ITEM_PRICE_MISMATCH, 4L), invalidPriceException.getMessage());
    }

    @Test
    @DisplayName("상품의 수량이 부족할 경우 오류를 반환한다.")
    void create_order_item_test_not_enough_stock(){
        //given
        commands.add(new OrderItemCreateCommand(4L, 300, 10));
        when(itemRepository.findById(4L))
                .thenReturn(Optional.of(new Item(4L, "test", 300, 5)));

        //when
        NotEnoughStockException notEnoughStockException = assertThrows(NotEnoughStockException.class,
                () -> orderService.saveOrderItems(order, commands));

        //then
        assertEquals(String.format(NotEnoughStockException.NOT_ENOUGH_STOCK, 4L), notEnoughStockException.getMessage());
    }

    @Test
    @DisplayName("주문 정보와 주문 상품 목록 정보가 생성/저장한다.")
    void do_order_process_test(){
        //given
        when(userRepository.findById(user.id()))
                .thenReturn(Optional.of(user));
        when(orderRepository.save(order))
                .thenReturn(order);

        //when
        OrderCreateCommand.Response save =
                orderService.save(new OrderCreateCommand(user.id(), null, commands));

        //then
        assertEquals(order.getOrderUser().id(), save.getUserId());
    }


}
