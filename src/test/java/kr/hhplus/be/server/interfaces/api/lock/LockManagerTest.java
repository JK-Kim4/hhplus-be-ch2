package kr.hhplus.be.server.interfaces.api.lock;

import kr.hhplus.be.server.common.exception.DistributedLockException;
import kr.hhplus.be.server.infrastructure.lock.PubSubLockManager;
import kr.hhplus.be.server.support.LockManagerSupporter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class LockManagerTest {


    @Mock
    RLock rLock;

    @AfterEach
    void tearDown() {
        // 트랜잭션 컨텍스트 클리어
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void 락_획득성공_트랜잭션_비활성화_즉시락반납() throws Exception {
        // given
        given(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).willReturn(true);
        given(rLock.isHeldByCurrentThread()).willReturn(true);

        // when
        PubSubLockManager manager = LockManagerSupporter.기본_LOCK_MANAGER_생성(rLock);

        // then
        assertTrue(manager.isLocked());
        manager.close(); // 트랜잭션이 없으므로 바로 해제

        then(rLock).should().unlock();
    }

    @Test
    void 락_획득실패시_예외발생() throws InterruptedException {
        // given
        given(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).willReturn(false);

        // expect
        assertThrows(DistributedLockException.class, () -> {
            LockManagerSupporter.기본_LOCK_MANAGER_생성(rLock);
        });

    }
}
