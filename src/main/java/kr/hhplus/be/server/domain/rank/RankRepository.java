package kr.hhplus.be.server.domain.rank;

import java.util.List;

public interface RankRepository {

    void saveAll(List<Rank> ranks);
}
