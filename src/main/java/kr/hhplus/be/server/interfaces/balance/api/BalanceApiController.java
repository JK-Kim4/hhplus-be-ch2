package kr.hhplus.be.server.interfaces.balance.api;

import kr.hhplus.be.server.domain.balance.BalanceInfo;
import kr.hhplus.be.server.domain.balance.BalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/balances")
public class BalanceApiController implements BalanceApiSpec {

    private final BalanceService balanceService;

    public BalanceApiController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<BalanceResponse> findByUserId(BalanceRequest request) {
        BalanceInfo balance = balanceService.findByUserId(request.toCommand());
        return ResponseEntity.ok(BalanceResponse.from(balance));

    }

    @Override
    @PatchMapping("/charge")
    public ResponseEntity<BalanceResponse.Charge> charge(
            @RequestBody BalanceRequest.Charge request) {

        BalanceInfo.Charge charge = balanceService.charge(request.toCommand());
        return ResponseEntity.ok(BalanceResponse.Charge.from(charge));
    }
}
