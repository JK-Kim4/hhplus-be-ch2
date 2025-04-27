package kr.hhplus.be.server.domain.rank;

import kr.hhplus.be.server.domain.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankService {

    private final OrderRepository orderRepository;
    private final RankRepository rankRepository;

    public RankService(OrderRepository orderRepository, RankRepository rankRepository) {
        this.orderRepository = orderRepository;
        this.rankRepository = rankRepository;
    }


    public void saveAll(List<Rank> orders) {
        rankRepository.saveAll(orders);
    }



}
