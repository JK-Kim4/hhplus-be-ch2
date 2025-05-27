package kr.hhplus.be.server.application.salesstat;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class SalesStatCriteria {

    @Getter
    public static class TargetDate {
        LocalDate targetDate;

        public static TargetDate of(LocalDate targetDate) {
            return TargetDate.builder().targetDate(targetDate).build();
        }

        @Builder
        private TargetDate(LocalDate targetDate) {
            this.targetDate = targetDate;
        }

    }
}
