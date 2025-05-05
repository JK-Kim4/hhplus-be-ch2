package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemInfo;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.item.ItemTestFixture;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.UserTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 정상적인_주문이_생성되고_저장된다(){
        //given
        User user = UserTestFixture.createTestUserWithIdAndName(1L, "User");
        Item item = ItemTestFixture.createDefaltItem();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        OrderCommand.CreateV2 command = OrderCommand.CreateV2.of(
                user.getId(), null,
                Arrays.asList(
                        ItemInfo.OrderItem.of(item.getId(), 5000, 10)
                )
        );

        //when
        orderService.createV2(command);

        //then
        verify(orderRepository, times(1))
                .save(any(Order.class));
    }

}
