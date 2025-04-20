package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 고유번호를_전달받아_주문의_상세정보를_조회할때_존재하지않으면_NoResultException(){
        //given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        //when//then
        assertThrows(NoResultException.class, ()
                -> orderService.findById(1L));
    }

}
