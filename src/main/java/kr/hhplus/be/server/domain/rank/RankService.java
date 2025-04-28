package kr.hhplus.be.server.domain.rank;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankService {

    private final RankRepository rankRepository;

    public RankService(RankRepository rankRepository) {
        this.rankRepository = rankRepository;
    }


    public void saveAll(List<Rank> orders) {
        rankRepository.saveAll(orders);
    }

}
