package kr.hhplus.be.server.domain.orderStatistics;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderStatisticsServiceTest {

    @Mock
    private OrderStatisticsRepository orderStatisticsRepository;

    @InjectMocks
    private OrderStatisticsService orderStatisticsService;

}
