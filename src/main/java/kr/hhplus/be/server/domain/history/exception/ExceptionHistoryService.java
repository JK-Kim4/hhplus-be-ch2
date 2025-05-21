package kr.hhplus.be.server.domain.history.exception;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class ExceptionHistoryService {

    private final ExceptionHistoryRepository exceptionHistoryRepository;

    public ExceptionHistoryService(ExceptionHistoryRepository exceptionHistoryRepository) {
        this.exceptionHistoryRepository = exceptionHistoryRepository;
    }

    public void saveExceptionHistory(LocalDate exceptionDate, ExceptionHistory exceptionHistory) {
        exceptionHistoryRepository.save(exceptionDate, exceptionHistory);
    }

    public Set<ExceptionHistory> findByExceptionDate(LocalDate exceptionDate){
        return exceptionHistoryRepository.findByDate(exceptionDate);
    }
}
