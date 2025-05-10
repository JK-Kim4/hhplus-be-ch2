package kr.hhplus.be.server.infrastructure.balance;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BalanceRepositoryImpl implements BalanceRepository {

    private final BalanceJpaRepository balanceJpaRepository;

    public BalanceRepositoryImpl(BalanceJpaRepository balanceJpaRepository) {
        this.balanceJpaRepository = balanceJpaRepository;
    }


    @Override
    public void flush() {
        balanceJpaRepository.flush();
    }

    @Override
    public Balance save(Balance balance) {
        return balanceJpaRepository.save(balance);
    }

    @Override
    public Optional<Balance> findByUserId(Long userId) {
        return balanceJpaRepository.findById(userId);
    }
}
