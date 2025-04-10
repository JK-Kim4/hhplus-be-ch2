package kr.hhplus.be.server.domain.user.pointHistory;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointHistoryRepository {

    PointHistory save(PointHistory pointHistory);

    List<PointHistory> findByUserId(Long userId);
}
