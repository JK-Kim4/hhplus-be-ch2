package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    BalanceRepository balanceRepository;

    @InjectMocks
    BalanceService balanceService;


    @Test
    void 사용자_고유번호와_잔고정보를_전달받아_잔액객체를_생성한다() {
        // given
        Long userId = 1L;
        BigDecimal point = BigDecimal.valueOf(1000);
        BalanceCommand.Create command = BalanceCommand.Create.builder()
                .userId(userId)
                .point(point)
                .build();

        // when
        BalanceInfo.Create result = balanceService.create(command);

        // then
        verify(balanceRepository).save(any(Balance.class));
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getPoint()).isEqualTo(point);
    }

    @Test
    void 사용자_고유번호만_전달된_경우_초기_잔고액은_0원이다(){
        // given
        Long userId = 1L;
        BalanceCommand.Create command = BalanceCommand.Create
                .builder()
                .userId(userId)
                .build();

        // when
        BalanceInfo.Create result = balanceService.create(command);

        // then
        verify(balanceRepository).save(any(Balance.class));
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getPoint()).isEqualTo(BigDecimal.ZERO);

    }

    @Test
    void 잔액을_충전할수있다() {
        // given
        Long userId = 1L;
        BigDecimal currentPoint = BigDecimal.valueOf(5000);
        BigDecimal chargeAmount = BigDecimal.valueOf(3000);
        Balance balance = Balance.create(userId, Point.of(currentPoint));

        when(balanceRepository.findByUserId(userId)).thenReturn(Optional.of(balance));

        BalanceCommand.Charge command = BalanceCommand.Charge.of(userId, chargeAmount);

        // when
        BalanceInfo.Charge result = balanceService.charge(command);

        // then
        verify(balanceRepository).save(balance);
        assertThat(result.getNowPoint()).isEqualTo(currentPoint.add(chargeAmount));
    }

    @Test
    void 잔액을_차감할수있다() {
        // given
        Long userId = 1L;
        BigDecimal currentPoint = BigDecimal.valueOf(5000);
        BigDecimal deductAmount = BigDecimal.valueOf(2000);
        Balance balance = Balance.create(userId, Point.of(currentPoint));

        when(balanceRepository.findByUserId(userId)).thenReturn(Optional.of(balance));

        BalanceCommand.Deduct command = new BalanceCommand.Deduct(userId, deductAmount);

        // when
        BalanceInfo.Deduct result = balanceService.deduct(command);

        // then
        verify(balanceRepository).save(balance);
        assertThat(result.getNowPoint()).isEqualTo(currentPoint.subtract(deductAmount));
    }

    @Test
    void 잔고_충전시_잔고정보가_존재하지_않을경우_NoResultException_발생() {
        // given
        when(balanceRepository.findByUserId(any())).thenReturn(Optional.empty());

        BalanceCommand.Charge command = BalanceCommand.Charge.of(1L, BigDecimal.valueOf(1000));

        // expect
        assertThatThrownBy(() -> balanceService.charge(command))
                .isInstanceOf(NoResultException.class);
    }

    @Test
    void 잔고_차감시_잔고정보가_존재하지_않을경우_NoResultException_발생() {
        // given
        when(balanceRepository.findByUserId(any())).thenReturn(Optional.empty());

        BalanceCommand.Deduct command = new BalanceCommand.Deduct(1L, BigDecimal.valueOf(1000));

        // expect
        assertThatThrownBy(() -> balanceService.deduct(command))
                .isInstanceOf(NoResultException.class);
    }

}