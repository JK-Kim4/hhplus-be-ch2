package kr.hhplus.be.server.domain.balance;

import java.util.Optional;

public interface BalanceRepository {

    void flush();

    Balance save(Balance balance);

    Optional<Balance> findByUserId(Long userId);
}
