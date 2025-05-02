package kr.hhplus.be.server.domain.user;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.application.user.UserCommandService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserCommandService userCommandService;

    @Nested
    class 사용자_포인트_충전_커멘드를_전달받아_포인트를_충전 {

        @Test
        void 사용자_고유번호에_해당하는_사용자가_존재하지않을경우_NoResultException(){
            //given
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            UserCommand.Charge command = UserCommand.Charge.of(1L, 5000);

            //when//then
            assertThrows(NoResultException.class, () ->
                    userCommandService.charge(command));
        }

        @Test
        void 사용자_포인트가_충전된다(){
            //given
            User user = Mockito.mock(User.class);
            when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
            UserCommand.Charge command = UserCommand.Charge.of(1L, 5000);

            //when
            userCommandService.charge(command);
            InOrder inOrder = Mockito.inOrder(user, userRepository);

            //then
            inOrder.verify(userRepository).findById(1L);
            inOrder.verify(user).chargePoint(anyInt());
        }
    }


}
