package kr.hhplus.be.server.domain.salesStat.salesReport;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesReportService {

    private final SalesReportInMemoryRepository salesReportInMemoryRepository;

    public SalesReportService(SalesReportInMemoryRepository salesReportInMemoryRepository) {
        this.salesReportInMemoryRepository = salesReportInMemoryRepository;
    }

    //판매 이력 키 등록 여부
    public boolean existByLocalDate(SalesReportCommand.ReportDate command) {
        return salesReportInMemoryRepository.existByReportDate(command.getReportDate());
    }

    //판매 이력 키 삭제
    public void deleteByLocalDate(SalesReportCommand.ReportDate command) {
        salesReportInMemoryRepository.deleteByReportDate(command.getReportDate());
    }

    //판매 이력 정보 멤버 조회
    public SalesReport findByReportDateAndProductId(SalesReportCommand.SalesReport command) {
        return salesReportInMemoryRepository.findByReportDateAndProductId(command.getReportDate(), command.getProductId());
    }

    //판매 이력 정보 전체 조회
    public List<SalesReport> findAllByReportDate(SalesReportCommand.ReportDate command) {
        return salesReportInMemoryRepository.findAllByReportDate(command.getReportDate());
    }

    //판매 이력 정보 등록(ZADD)
    public void increaseDailySalesReport(SalesReportCommand.IncreaseSalesReport command) {
        salesReportInMemoryRepository.increaseDailySalesReport(
                command.getReportDate(),
                command.getProductId(),
                command.getSalesScore());
    }

    public void setDailySalesReportKeyTTL(SalesReportCommand.SalesReportTTL command) {
        salesReportInMemoryRepository.setDailySalesReportKeyTTL(command.getReportDate(), command.getDuration());
    }

}
