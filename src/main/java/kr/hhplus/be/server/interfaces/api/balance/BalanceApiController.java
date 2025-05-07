package kr.hhplus.be.server.interfaces.api.balance;

import kr.hhplus.be.server.domain.balance.BalanceInfo;
import kr.hhplus.be.server.domain.balance.BalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/balances")
public class BalanceApiController implements BalanceApiSpec {

    private final BalanceService balanceService;

    public BalanceApiController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @Override
    @PatchMapping("/charge")
    public ResponseEntity<BalanceResponse.Charge> charge(
            @RequestBody BalanceRequest.Charge request) {

        BalanceInfo.Charge charge = balanceService.charge(request.toCommand());
        return ResponseEntity.ok(BalanceResponse.Charge.from(charge));
    }
}
