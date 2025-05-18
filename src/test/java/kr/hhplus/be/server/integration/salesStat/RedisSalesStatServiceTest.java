package kr.hhplus.be.server.integration.salesStat;


import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.salesStat.RedisSalesStatService;
import kr.hhplus.be.server.domain.salesStat.SalesStatCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class RedisSalesStatServiceTest {

    @Autowired
    RedisSalesStatService redisSalesStatService;

    private static final String TEST_KEY = "test:key";
    private static final String TEST_VALUE = "value";

    @BeforeEach
    void setUp() {
        redisSalesStatService.removeByKey(SalesStatCommand.RedisDeleteKey.of(TEST_KEY));  // 대상 key만 삭제
    }

    @Test
    void ZSET에_새로운_member를_score와_함께_추가한다(){
        // given
        double initialScore = 5.0;
        SalesStatCommand.RedisAddSortedSet command = SalesStatCommand.RedisAddSortedSet.of(TEST_KEY, TEST_VALUE, initialScore);

        // when
        redisSalesStatService.incrementZSetScoreByKeyWithMember(command);

        // then
        assertEquals(initialScore, redisSalesStatService.findSZetScoreByKeyAndMember(TEST_KEY, TEST_VALUE));
    }

    @Test
    void 기존_member에_score를_추가하면_누적된다(){
        // given
        double firstScore = 5.0;
        double additionalScore = 7.0;
        SalesStatCommand.RedisAddSortedSet command = SalesStatCommand.RedisAddSortedSet.of(TEST_KEY, TEST_VALUE, firstScore);
        redisSalesStatService.incrementZSetScoreByKeyWithMember(command);

        // when
        command = SalesStatCommand.RedisAddSortedSet.of(TEST_KEY, TEST_VALUE, additionalScore);
        redisSalesStatService.incrementZSetScoreByKeyWithMember(command);

        // then
        assertEquals(firstScore + additionalScore, redisSalesStatService.findSZetScoreByKeyAndMember(TEST_KEY, TEST_VALUE));
    }

    @Test
    void ZSET_key가_존재할_때_delete호출시_전체_key가_삭제된다(){
        //given
        double initialScore = 5.0;
        SalesStatCommand.RedisAddSortedSet command = SalesStatCommand.RedisAddSortedSet.of(TEST_KEY, TEST_VALUE, initialScore);

        // when
        redisSalesStatService.incrementZSetScoreByKeyWithMember(command);
        assertTrue(redisSalesStatService.hasKey(TEST_KEY));
        redisSalesStatService.removeByKey(SalesStatCommand.RedisDeleteKey.of(TEST_KEY));

        // then
        assertFalse(redisSalesStatService.hasKey(TEST_KEY));
    }

    @Test
    void ZSET에서_존재하지않는_member에대하여_score_조회시_예외를_던진다(){
        //given
        double initialScore = 5.0;
        SalesStatCommand.RedisAddSortedSet command = SalesStatCommand.RedisAddSortedSet.of(TEST_KEY, TEST_VALUE, initialScore);
        redisSalesStatService.incrementZSetScoreByKeyWithMember(command);
        assertNotNull(redisSalesStatService.findSZetScoreByKeyAndMember(TEST_KEY, TEST_VALUE));

        //when
        redisSalesStatService.removeZSetMemberByKey(TEST_KEY, TEST_VALUE);

        //then
        assertThrows(NoResultException.class, ()
                -> redisSalesStatService.findSZetScoreByKeyAndMember(TEST_KEY, TEST_VALUE));
    }

    @Test
    void ZSET에_등록된_key는_TTL이_설정되어있지_않다(){
        //given
        double initialScore = 5.0;
        SalesStatCommand.RedisAddSortedSet command = SalesStatCommand.RedisAddSortedSet.of(TEST_KEY, TEST_VALUE, initialScore);
        redisSalesStatService.incrementZSetScoreByKeyWithMember(command);

        //then
        assertEquals(-1, redisSalesStatService.getExpireTtl(TEST_KEY));
    }

    @Test
    void setExpireTtl_호출시_KEY에_TTL이_정상적으로_설정된다(){
        //given
        double initialScore = 5.0;
        SalesStatCommand.RedisAddSortedSet command = SalesStatCommand.RedisAddSortedSet.of(TEST_KEY, TEST_VALUE, initialScore);
        redisSalesStatService.incrementZSetScoreByKeyWithMember(command);
        redisSalesStatService.setExpireTtl(TEST_KEY, Duration.ofSeconds(100));

        //then
        assertThat(redisSalesStatService.getExpireTtl(TEST_KEY)).isGreaterThan(0);
    }

}
