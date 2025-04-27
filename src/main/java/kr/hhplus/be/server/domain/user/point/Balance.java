package kr.hhplus.be.server.domain.user.point;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Balance {

    public BigDecimal balance;

    
}
