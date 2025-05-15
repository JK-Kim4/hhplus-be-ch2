package kr.hhplus.be.server.domain.salesStat;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesStatService {

    private final SalesStatRepository salesStatRepository;

    public SalesStatService(
            SalesStatRepository salesStatRepository) {
        this.salesStatRepository = salesStatRepository;
    }

    public void createAll(SalesStatCommand.Creates command){
        List<SalesStat> salesStats = command.getCreates().stream()
                .map((create) -> SalesStat.create(
                        create.getProductId(),
                        create.getSalesDate(),
                        create.getSalesAmount())
                )
                .toList();

        salesStatRepository.saveAll(salesStats);
    }

}
