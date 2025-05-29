package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public BalanceInfo findByUserId(BalanceCommand command) {
        Balance balance = balanceRepository.findByUserId(command.getUserId())
                .orElse(Balance.create(command.getUserId(), Point.create(BigDecimal.ZERO)));
        return BalanceInfo.from(balance);
    }


    public BalanceInfo.Create create(BalanceCommand.Create command){
        Balance balance = Balance.create(command.getUserId(), Point.create(command.getPoint()));

        balanceRepository.save(balance);
        return BalanceInfo.Create.from(balance);
    }

    public BalanceInfo.Charge charge(BalanceCommand.Charge command){
        Balance balance = balanceRepository.findByUserId(command.getUserId())
                .orElseThrow(NoResultException::new);

        balance.charge(command.getChargePoint());
        balanceRepository.save(balance);
        return BalanceInfo.Charge.from(balance);
    }

    public BalanceInfo.Deduct deduct(BalanceCommand.Deduct command){
        Balance balance = balanceRepository.findByUserId(command.getUserId())
                .orElseThrow(NoResultException::new);

        balance.deduct(command.getDeductPoint());
        balanceRepository.save(balance);
        return BalanceInfo.Deduct.from(balance);
    }
}
