package kr.hhplus.be.server.infrastructure.rank;

import kr.hhplus.be.server.domain.rank.Rank;
import kr.hhplus.be.server.domain.rank.RankRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RankRepositoryImpl implements RankRepository {

    private final RankJpaRepository jpaRepository;

    public RankRepositoryImpl(RankJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void saveAll(List<Rank> ranks) {
        jpaRepository.saveAll(ranks);
    }
}
