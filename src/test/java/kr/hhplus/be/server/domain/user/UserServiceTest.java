package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.FakeUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    String name = "tester";
    User user = new FakeUser(1L, name);

    @Test
    void 동일한_이름의_사용자_등록요청시_IllegalArgumentException를_반환한다(){
        //given
        UserCommand.Create command = new UserCommand.Create(name);
        when(userRepository.findByName(name))
                .thenReturn(Optional.of(user));

        //when
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> userService.save(command));

        //then
        assertEquals("이미 존재하는 회원입니다." ,illegalArgumentException.getMessage());
    }

    @Test
    void 사용자는_포인트를_충전할수있다(){
        //given
        User user = mock(User.class);
        UserCommand.Charge command =
                new UserCommand.Charge(10L, 5_000);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        //when
        userService.charge(command);

        //then
        verify(user, times(1)).chargePoint(command.getAmount());
    }


}
