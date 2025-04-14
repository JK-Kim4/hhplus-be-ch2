package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;


    @Test
    void create_user_test(){
        String username = "test";
        UserCommand.Create command = new UserCommand.Create(username);

        when(userRepository.findByName(username)).thenReturn(Optional.empty());
        when(userRepository.save(new User(command.getName())))
                .thenReturn(new User(command.getName()));

        UserInfo.Create create = userService.create(command);

        assertNotNull(create);
        assertEquals("test", create.getName());
    }

    @Test
    void 사용자_이름은_중복으로_등록할수없다(){
        String username = "test";
        UserCommand.Create command = new UserCommand.Create(username);
        User user = new User(username);

        when(userRepository.findByName(username))
                .thenReturn(Optional.of(user));

        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> userService.create(command));
        assertEquals(illegalArgumentException.getMessage(), "이미 존재하는 회원입니다.");
    }


}
