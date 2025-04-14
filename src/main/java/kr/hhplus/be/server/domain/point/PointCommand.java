package kr.hhplus.be.server.domain.point;

import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.NonNull;

public class PointCommand {

    public static class Point{

        private Long userId;
        private Long pointId;
        private Integer amount;

    }

    public static class Charge{


        private Long userId;

        private Integer amount;

        public static Charge of(@NonNull Long userId, @Positive Integer amount) {
            return new Charge(userId, amount);
        }

        private Charge(Long userId, Integer amount) {
            this.userId = userId;
            this.amount = amount;
        }

        public Long getUserId() {
            return userId;
        }

        public Integer getAmount() {
            return amount;
        }
    }


}
